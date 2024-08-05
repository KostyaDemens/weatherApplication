package by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Main {

  @JsonProperty("temp")
  private double temperature;

  public Long getTemperature() {
    return (long) temperature;
  }
}
