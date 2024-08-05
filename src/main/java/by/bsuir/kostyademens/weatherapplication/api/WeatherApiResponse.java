package by.bsuir.kostyademens.weatherapplication.api;

import by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes.Main;
import by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes.Sys;
import by.bsuir.kostyademens.weatherapplication.api.weatherApiAttributes.Weather;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherApiResponse {
  @JsonProperty("weather")
  private List<Weather> weather;

  @JsonProperty("main")
  private Main main;

  @JsonProperty("sys")
  private Sys sys;
}
