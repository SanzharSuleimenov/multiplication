package microservices.book.multiplication.challenge;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

  private ChallengeService challengeService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private ChallengeAttemptRepository challengeAttemptRepository;
  @Mock
  private ChallengeEventPub challengeEventPub;

  @BeforeEach
  public void setUp() {
    challengeService = new ChallengeServiceImpl(
        userRepository,
        challengeAttemptRepository,
        challengeEventPub
    );
  }

  @Test
  void checkCorrectAttemptTest() {
    // given
    User user = new User("bruce_lee");
    given(userRepository.save(any()))
        .will(returnsFirstArg());
    given(challengeAttemptRepository.save(any()))
        .will(returnsFirstArg());
    ChallengeAttemptDTO challengeAttemptDTO =
        new ChallengeAttemptDTO(20, 30, "bruce_lee", 600);
    ChallengeAttempt resultAttempt = new ChallengeAttempt(
        null,
        user,
        20,
        30,
        600,
        true);

    // when
    ChallengeAttempt challengeAttempt =
        challengeService.verifyAttempt(challengeAttemptDTO);

    // then
    then(challengeAttempt.isCorrect()).isTrue();
    verify(userRepository).save(user);
    verify(challengeAttemptRepository).save(challengeAttempt);
    verify(challengeEventPub).challengeSolved(resultAttempt);
  }

  @Test
  void checkWrongAttemptTest() {
    // given
    given(challengeAttemptRepository.save(any()))
        .will(returnsFirstArg());
    ChallengeAttemptDTO challengeAttemptDTO =
        new ChallengeAttemptDTO(20, 30, "bruce_lee", 6000);

    // when
    ChallengeAttempt resultAttempt =
        challengeService.verifyAttempt(challengeAttemptDTO);

    // then
    then(resultAttempt.isCorrect()).isFalse();
  }

  @Test
  void checkExistingUserTest() {
    // given
    given(challengeAttemptRepository.save(any()))
        .will(returnsFirstArg());
    User existingUser = new User(1L, "bruce_lee");
    given(userRepository.findByAlias("bruce_lee"))
        .willReturn(Optional.of(existingUser));
    ChallengeAttemptDTO attemptDTO =
        new ChallengeAttemptDTO(50, 60, "bruce_lee", 3000);

    // when
    ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

    // then
    then(resultAttempt.isCorrect()).isTrue();
    then(resultAttempt.getUser()).isEqualTo(existingUser);
    verify(userRepository, never()).save(any());
    verify(challengeAttemptRepository).save(resultAttempt);
  }

  @Test
  void checkGetLastAttempts() {
    // given
    ChallengeAttempt challengeAttempt = new ChallengeAttempt(1L, new User(1L, "bruce_lee"), 50, 60,
        3000, true);
    given(challengeAttemptRepository.lastAttempts(eq("bruce_lee")))
        .willReturn(List.of(challengeAttempt));

    // when
    List<ChallengeAttempt> lastAttempts = challengeService.getLastAttempts("bruce_lee");

    // then
    verify(challengeAttemptRepository, atMostOnce()).lastAttempts(eq("bruce_lee"));
    then(lastAttempts).isNotEmpty();
    then(lastAttempts.get(0)).isEqualTo(challengeAttempt);
  }

  @Test
  void checkGetLast10AttemptsOfUser() {
    // given
    ChallengeAttempt challengeAttempt = new ChallengeAttempt(1L, new User(1L, "bruce_lee"), 50, 60,
        3000, true);
    given(challengeAttemptRepository.findTop10ByUserAliasOrderByIdDesc(eq("bruce_lee")))
        .willReturn(List.of(challengeAttempt));

    // when
    List<ChallengeAttempt> lastAttempts = challengeService.getLast10Attempts("bruce_lee");

    // then
    verify(challengeAttemptRepository, atMostOnce()).lastAttempts(eq("bruce_lee"));
    then(lastAttempts).isNotEmpty();
    then(lastAttempts.get(0)).isEqualTo(challengeAttempt);
  }
}
