package by.bsuir.kostyademens.weatherapplication.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationApiResponse {
  @JsonProperty("name")
  private String name;

  @JsonProperty("country")
  private String country;

  @JsonProperty("lon")
  private BigDecimal lon;

  @JsonProperty("lat")
  private BigDecimal lat;
}
