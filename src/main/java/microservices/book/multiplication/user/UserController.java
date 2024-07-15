package microservices.book.multiplication.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;

  @GetMapping("/{idList}")
  public List<User> getUserByIdList(@PathVariable final List<Long> idList) {
    return userRepository.findAllByIdIn(idList);
  }
}
