package by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sys {
  @JsonProperty("country")
  private String country;
}
