package microservices.book.multiplication.challenge;

/**
 * The naming convention shown here is a good practice for events. They represent a
 * fact that already happened, so the name should use the past tense.
 *
 * @param attemptId
 * @param correct
 * @param factorA
 * @param factorB
 * @param userId
 * @param userAlias
 */
public record ChallengeSolvedEvent(
    long attemptId,
    boolean correct,
    int factorA,
    int factorB,
    long userId,
    String userAlias
) {

}
