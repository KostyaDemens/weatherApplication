package by.bsuir.kostyademens.weatherapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User {

  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Id
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
