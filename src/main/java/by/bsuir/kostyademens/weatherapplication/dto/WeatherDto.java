package by.bsuir.kostyademens.weatherapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class WeatherDto {
  private String description;
  private String iconName;
  private Long temperature;
  private String country;
}
