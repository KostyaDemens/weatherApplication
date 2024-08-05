package by.bsuir.kostyademens.weatherapplication.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import by.bsuir.kostyademens.weatherapplication.dao.LocationDao;
import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.dto.WeatherDto;
import by.bsuir.kostyademens.weatherapplication.model.Location;
import by.bsuir.kostyademens.weatherapplication.model.User;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks private UserService userService;

  @Mock private LocationDao locationDao;

  @Mock private OpenWeatherService weatherService;

  private User user;
  private List<Location> mockLocations;

  @BeforeEach
  void setUp() {
    user = new User("Kostya", "123");

    mockLocations =
        Arrays.asList(
            new Location("location1", user, BigDecimal.valueOf(1), BigDecimal.valueOf(1)),
            new Location("location2", user, BigDecimal.valueOf(1), BigDecimal.valueOf(1)));
  }

  @Test
  void userShouldGetLocations() {

    when(locationDao.findUserLocations(user)).thenReturn(mockLocations);
    when(weatherService.getWeatherForecast(any(BigDecimal.class), any(BigDecimal.class)))
        .thenReturn(new WeatherDto("description", "iconName", 2L, "country"));

    List<LocationDto> result = userService.getUserLocations(user);

    assertThat(
        result,
        containsInAnyOrder(
            hasProperty("name", is("location1")), hasProperty("name", is(("location2")))));

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(locationDao, times(1)).findUserLocations(user);
    verify(weatherService, times(2))
        .getWeatherForecast(any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  void shouldReturnTrueIfUserHasLocation() {

    when(locationDao.findUserLocations(user)).thenReturn(mockLocations);

    LocationDto locationDto =
        LocationDto.builder()
            .name("name")
            .hasLocation(true)
            .weather(new WeatherDto("description", "iconName", 2L, "country"))
            .latitude(BigDecimal.valueOf(1))
            .longitude(BigDecimal.valueOf(1))
            .build();

    boolean result = userService.isUserHasLocation(user, locationDto);

    assertTrue(result);
    verify(locationDao, times(1)).findUserLocations(user);
  }
}
