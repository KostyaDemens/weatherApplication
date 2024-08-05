package by.bsuir.kostyademens.weatherapplication.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.dto.WeatherDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

  @InjectMocks private LocationService locationService;
  @Mock private OpenWeatherService weatherService;

  @Test
  void shouldRemoveLocationIfItHasTheSameCountry() {
    List<LocationDto> mockLocations = new ArrayList<>();
    mockLocations.add(new LocationDto("MINSK", BigDecimal.valueOf(123), BigDecimal.valueOf(123)));
    mockLocations.add(new LocationDto("MINSK", BigDecimal.valueOf(123), BigDecimal.valueOf(123)));
    mockLocations.add(new LocationDto("MINSK", BigDecimal.valueOf(1234), BigDecimal.valueOf(1234)));

    when(weatherService.getLocationsByName(anyString())).thenReturn(mockLocations);

    when(weatherService.getWeatherForecast(BigDecimal.valueOf(123), BigDecimal.valueOf(123)))
        .thenReturn(new WeatherDto("description", "iconName", 1L, "BY"));
    when(weatherService.getWeatherForecast(BigDecimal.valueOf(1234), BigDecimal.valueOf(1234)))
        .thenReturn(new WeatherDto("description", "iconName", 1L, "RU"));

    List<LocationDto> result = locationService.findLocationsByName("test");

    Assertions.assertEquals(2, result.size());
  }
}
