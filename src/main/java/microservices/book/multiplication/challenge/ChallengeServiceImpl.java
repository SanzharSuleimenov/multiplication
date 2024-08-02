package microservices.book.multiplication.challenge;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {

  private final UserRepository userRepository;
  private final ChallengeAttemptRepository challengeAttemptRepository;
  private final ChallengeEventPub challengeEventPub;

  @Override
  public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt) {
    User user = userRepository.findByAlias(resultAttempt.userAlias())
        .orElseGet(() -> {
          log.info("Creating new user with alias {}", resultAttempt.userAlias());

          return userRepository.save(new User(resultAttempt.userAlias()));
        });

    boolean isCorrect = resultAttempt.guess() == resultAttempt.factorA() * resultAttempt.factorB();

    ChallengeAttempt checkedAttempt = new ChallengeAttempt(null,
        user,
        resultAttempt.factorA(),
        resultAttempt.factorB(),
        resultAttempt.guess(),
        isCorrect);

    ChallengeAttempt storedAttempt = challengeAttemptRepository.save(checkedAttempt);

    challengeEventPub.challengeSolved(storedAttempt);

    return storedAttempt;
  }

  @Override
  public List<ChallengeAttempt> getLastAttempts(String alias) {
    return challengeAttemptRepository.lastAttempts(alias);
  }

  @Override
  public List<ChallengeAttempt> getLast10Attempts(String alias) {
    return challengeAttemptRepository.findTop10ByUserAliasOrderByIdDesc(alias);
  }
}
