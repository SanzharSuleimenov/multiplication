package microservices.book.multiplication.challenge;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Attempt coming from the user.
 * @param factorA
 * @param factorB
 * @param userAlias
 * @param guess
 */
public record ChallengeAttemptDTO(
    @Min(1) @Max(99)
    int factorA,
    @Min(1) @Max(99)
    int factorB,
    @NotBlank
    String userAlias,
    @Positive(message = "You can get only positive result. Try again.")
    int guess) {

}
