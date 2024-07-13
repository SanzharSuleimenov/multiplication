package microservices.book.multiplication.challenge;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChallengeGeneratorServiceTest {

  private ChallengeGeneratorService challengeGeneratorService;

  @Spy
  private Random random;

  @BeforeEach
  public void setUp() {
    challengeGeneratorService = new ChallengeGeneratorServiceImpl(random);
  }

  @Test
  void generateRandomFactorIsBetweenExpectedLimits() {
    // 89 is max - min range
    given(random.nextInt(89)).willReturn(30, 20);

    // when we generate a challenge
    Challenge challenge = challengeGeneratorService.randomChallenge();

    // then the challenge contains factors as expected
    then(challenge).isEqualTo(new Challenge(41, 31));
  }
}
