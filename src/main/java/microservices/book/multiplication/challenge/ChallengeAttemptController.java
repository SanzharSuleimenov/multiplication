package microservices.book.multiplication.challenge;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attempts")
public class ChallengeAttemptController {

  private final ChallengeService challengeService;

  @PostMapping
  ResponseEntity<ChallengeAttempt> postResult(
      @RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) {
    return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
  }

  @GetMapping
  ResponseEntity<List<ChallengeAttempt>> getResults(@RequestParam("alias") String alias) {
    return ResponseEntity.ok(challengeService.getLastAttempts(alias));
  }
}
