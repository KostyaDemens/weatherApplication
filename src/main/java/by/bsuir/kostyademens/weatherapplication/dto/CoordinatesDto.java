package by.bsuir.kostyademens.weatherapplication.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CoordinatesDto {
  private BigDecimal lat;
  private BigDecimal lon;
}
