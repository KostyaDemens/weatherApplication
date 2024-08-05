package by.bsuir.kostyademens.weatherapplication.integrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.exception.NoSuchCountryException;
import by.bsuir.kostyademens.weatherapplication.exception.OpenWeatherException;
import by.bsuir.kostyademens.weatherapplication.service.OpenWeatherService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherServiceIntegrationTest {

  @Mock private HttpURLConnection mockConnection;

  @InjectMocks private OpenWeatherService openWeatherService;

  @BeforeEach
  void setUp() {
    openWeatherService =
        new OpenWeatherService() {
          @Override
          protected HttpURLConnection createConnection(URL url) {
            return mockConnection;
          }
        };
  }

  @Test
  void testGetLocationsByNameSuccess() throws Exception {
    String jsonResponse = "[{\"name\":\"London\",\"lat\":51.51,\"lon\":-0.13}]";
    InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

    when(mockConnection.getInputStream()).thenReturn(inputStream);

    List<LocationDto> result = openWeatherService.getLocationsByName("London");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("London", result.get(0).getName());
    assertEquals(BigDecimal.valueOf(51.51), result.get(0).getLatitude());
    assertEquals(BigDecimal.valueOf(-0.13), result.get(0).getLongitude());
  }

  @Test
  void testGetLocationsByNameNoLocations() throws Exception {
    String jsonResponse = "[]";
    InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

    when(mockConnection.getInputStream()).thenReturn(inputStream);

    assertThrows(NoSuchCountryException.class, () -> openWeatherService.getLocationsByName("test"));
  }

  @Test
  void testGetLocationsByNameHttpError() throws Exception {
    when(mockConnection.getInputStream()).thenThrow(new IOException("HTTP error"));

    assertThrows(RuntimeException.class, () -> openWeatherService.getLocationsByName("London"));
  }

  @Test
  void testGetLocationsByNameOpenWeatherError() throws IOException {
    String jsonResponse = "[]";
    InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

    when(mockConnection.getInputStream()).thenReturn(inputStream);

    when(mockConnection.getResponseCode()).thenReturn(400);

    assertThrows(OpenWeatherException.class, () -> openWeatherService.getLocationsByName("Test"));
  }
}
