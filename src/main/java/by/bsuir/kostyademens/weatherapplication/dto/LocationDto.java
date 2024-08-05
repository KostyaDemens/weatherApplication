package by.bsuir.kostyademens.weatherapplication.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
  private boolean hasLocation;
  private String name;
  private WeatherDto weather;
  private BigDecimal latitude;
  private BigDecimal longitude;

  public LocationDto(String name, BigDecimal latitude, BigDecimal longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
