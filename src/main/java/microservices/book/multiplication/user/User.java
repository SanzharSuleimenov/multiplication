package microservices.book.multiplication.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stores information to identify the user.
 */
@Entity
@Table(name = "my_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long id;
  private String alias;

  public User(final String alias) {
    this(null, alias);
  }
}
