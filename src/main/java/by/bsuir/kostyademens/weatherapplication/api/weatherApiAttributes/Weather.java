package by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Weather {
  @JsonProperty("description")
  private String description;

  @JsonProperty("main")
  private String main;

  public String getMain() {
    return main.toUpperCase();
  }
}
