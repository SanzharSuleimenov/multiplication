package microservices.book.multiplication.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByAlias(String alias);

  List<User> findAllByIdIn(List<Long> ids);
}
