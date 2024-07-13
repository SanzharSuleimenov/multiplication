package microservices.book.multiplication.challenge;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import microservices.book.multiplication.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(ChallengeAttemptController.class)
public class ChallengeAttemptControllerTest {

  @MockBean
  ChallengeService challengeService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JacksonTester<ChallengeAttemptDTO> jsonRequestAttempt;
  @Autowired
  JacksonTester<ChallengeAttempt> jsonResponseAttempt;
  @Autowired
  JacksonTester<List<ChallengeAttempt>> jsonResponseAttemptList;

  @Test
  void postValidResult() throws Exception {
    // given
    User user = new User(1L, "jackie");
    long attemptId = 5L;
    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 70, "jackie", 3500);
    ChallengeAttempt expectedResponse =
        new ChallengeAttempt(attemptId, user, 50, 70, 3500, true);
    given(challengeService.verifyAttempt(eq(attemptDTO)))
        .willReturn(expectedResponse);

    // when
    MockHttpServletResponse response = mockMvc.perform(
            post("/attempts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestAttempt.write(attemptDTO).getJson()))
        .andReturn().getResponse();

    // then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(
        jsonResponseAttempt.write(expectedResponse).getJson());
  }

  @Test
  void postInvalidResult() throws Exception {
    // given an attempt with invalid input data
    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(-50, 700, "jackie", 35);

    // when
    MockHttpServletResponse response = mockMvc.perform(
            post("/attempts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestAttempt.write(attemptDTO).getJson()))
        .andReturn().getResponse();

    // then
    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void getListOfChallengeAttempts() throws Exception {
    // given
    User user = new User(1L, "jackie");
    ChallengeAttempt attempt1 = new ChallengeAttempt(5L, user, 50, 60, 3000, true);
    ChallengeAttempt attempt2 = new ChallengeAttempt(6L, user, 50, 70, 3500, true);
    given(challengeService.getLastAttempts(eq("jackie")))
        .willReturn(List.of(attempt2, attempt1));

    // when
    MockHttpServletResponse response = mockMvc.perform(
        get("/attempts?alias=jackie")).andReturn().getResponse();

    // then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(
        jsonResponseAttemptList.write(List.of(attempt2, attempt1)).getJson());
  }
}
