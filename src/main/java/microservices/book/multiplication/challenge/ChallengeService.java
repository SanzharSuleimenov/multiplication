package microservices.book.multiplication.challenge;

import java.util.List;

public interface ChallengeService {

  /**
   * Verifies if an attempt coming from the presentation layer is correct or not.
   *
   * @return the resulting ChallengeAttempt object
   */
  ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt);

  List<ChallengeAttempt> getLastAttempts(String alias);

  List<ChallengeAttempt> getLast10Attempts(String alias);
}
