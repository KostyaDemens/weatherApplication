package by.bsuir.kostyademens.weatherapplication.service;

import by.bsuir.kostyademens.weatherapplication.api.LocationApiResponse;
import by.bsuir.kostyademens.weatherapplication.api.WeatherApiResponse;
import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.dto.WeatherDto;
import by.bsuir.kostyademens.weatherapplication.exception.NoSuchCountryException;
import by.bsuir.kostyademens.weatherapplication.exception.OpenWeatherException;
import by.bsuir.kostyademens.weatherapplication.util.PropertyReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OpenWeatherService {

  private final String API_KEY = PropertyReader.getProperty("API_KEY");
  private final String WEATHER_API_URL = "https://api.openweathermap.org/";
  private final ObjectMapper objectMapper = new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  protected HttpURLConnection createConnection(URL url) throws IOException {
    return (HttpURLConnection) url.openConnection();
  }

  public List<LocationDto> getLocationsByName(String locationName) {
    List<LocationDto> locationDtos = new ArrayList<>();
    try {
      String urlString = String.format(
              "%sgeo/1.0/direct?q=%s&limit=5&appid=%s",
              WEATHER_API_URL,
              locationName,
              API_KEY
      );

      URL url = new URL(urlString);
      HttpURLConnection connection = createConnection(url);
      connection.setRequestMethod("GET");

      List<LocationApiResponse> locations = objectMapper.readValue(connection.getInputStream(), new TypeReference<>() {});

      if (connection.getResponseCode() >= 400) {
          throw new OpenWeatherException("OpenWeatherApi exception");
      }

      for (LocationApiResponse location : locations) {
        LocationDto locationDto =
            LocationDto.builder()
                .name(location.getName())
                .latitude(location.getLat())
                .longitude(location.getLon())
                .build();
        locationDtos.add(locationDto);
      }

      if (locations.isEmpty()) {
        throw new NoSuchCountryException(
            "Error: The location you are searching for does not exist");
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return locationDtos;
  }

  public WeatherDto getWeatherForecast(BigDecimal latitude, BigDecimal longitude) {
    try {

      String urlString = String.format(
              "%sdata/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
              WEATHER_API_URL,
              latitude,
              longitude,
              API_KEY
      );

      URL url = new URL(urlString);
      HttpURLConnection connection = createConnection(url);
      connection.setRequestMethod("GET");


      WeatherApiResponse weatherApiResponse;

      try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
        weatherApiResponse = objectMapper.readValue(reader, WeatherApiResponse.class);
      } catch (Exception jsonExc) {
        throw new RuntimeException(jsonExc);
      }

      return WeatherDto.builder()
          .description(weatherApiResponse.getWeather().get(0).getDescription())
          .iconName(weatherApiResponse.getWeather().get(0).getMain())
          .temperature(weatherApiResponse.getMain().getTemperature())
          .country(weatherApiResponse.getSys().getCountry())
          .build();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
