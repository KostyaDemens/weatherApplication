package by.bsuir.kostyademens.weatherapplication.service;

import by.bsuir.kostyademens.weatherapplication.dao.LocationDao;
import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.model.Location;
import by.bsuir.kostyademens.weatherapplication.model.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserService {
  private final LocationDao locationDao;
  private final OpenWeatherService weatherService;

  public List<LocationDto> getUserLocations(User user) {
    List<Location> locations = locationDao.findUserLocations(user);
    List<LocationDto> userLocations = new ArrayList<>();
    for (Location location : locations) {
      LocationDto locationDto =
          LocationDto.builder()
              .name(location.getName())
              .weather(
                  weatherService.getWeatherForecast(
                      location.getLatitude(), location.getLongitude()))
              .hasLocation(true)
              .latitude(location.getLatitude())
              .longitude(location.getLongitude())
              .build();
      userLocations.add(locationDto);
    }

    return userLocations;
  }

  public boolean isUserHasLocation(User user, LocationDto locationDto) {
    List<Location> locations = locationDao.findUserLocations(user);
    return locations.stream()
        .anyMatch(
            location ->
                location.getLatitude().compareTo(locationDto.getLatitude()) == 0
                    && location.getLongitude().compareTo(locationDto.getLongitude()) == 0);
  }
}
