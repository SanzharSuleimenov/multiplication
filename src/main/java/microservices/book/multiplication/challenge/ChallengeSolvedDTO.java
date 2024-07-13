package microservices.book.multiplication.challenge;

public record ChallengeSolvedDTO(
    long attemptId,
    boolean correct,
    int factorA,
    int factorB,
    long userId,
    String userAlias
) {

}
