package by.bsuir.kostyademens.weatherapplication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Sessions")
public class Session {

  @Id
  @GeneratedValue(generator = "GUID")
  @GenericGenerator(name = "GUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", nullable = false)
  private String id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;
}
