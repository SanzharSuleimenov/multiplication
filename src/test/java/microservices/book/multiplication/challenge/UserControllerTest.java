package microservices.book.multiplication.challenge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserController;
import microservices.book.multiplication.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<User> userJson;

  @MockBean
  UserRepository userRepository;

  @Test
  void validGetUserListRequest() throws Exception {
    // given
    User user = new User("jackie_chan");
    given(userRepository.findAllByIdIn(any()))
        .willReturn(List.of(user));

    // when
    MockHttpServletResponse response = mockMvc.perform(
        get("/users/1,2,3")
    ).andReturn().getResponse();

    // then
    verify(userRepository, atLeastOnce()).findAllByIdIn(any());
    verify(userRepository).findAllByIdIn(List.of(1L, 2L, 3L));
  }

}
