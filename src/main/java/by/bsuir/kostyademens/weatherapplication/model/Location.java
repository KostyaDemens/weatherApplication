package by.bsuir.kostyademens.weatherapplication.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Locations")
public class Location {

  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToOne()
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "latitude", columnDefinition = "numeric")
  private BigDecimal latitude;

  @Column(name = "longitude", columnDefinition = "numeric")
  private BigDecimal longitude;

  public Location(String name, User user, BigDecimal latitude, BigDecimal longitude) {
    this.name = name;
    this.user = user;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
