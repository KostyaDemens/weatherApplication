package by.bsuir.kostyademens.weatherapplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.exception.UserAlreadyExistsException;
import by.bsuir.kostyademens.weatherapplication.model.User;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

  private final User testUser = new User("Ivan", "123");
  @Mock private UserDao userDao;
  @InjectMocks private RegistrationService registrationService;

  @Test
  void shouldThrowAnExceptionIfUserAlreadyExists() {
    when(userDao.findByLogin(testUser.getEmail())).thenReturn(Optional.of(testUser));

    assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(testUser));
    verify(userDao, times(1)).findByLogin(testUser.getEmail());
  }

  @Test
  void shouldSaveUserIfUserDoesNotExists() {
    when(userDao.findByLogin(testUser.getEmail())).thenReturn(Optional.empty());

    assertTrue(userDao.findByLogin(testUser.getEmail()).isEmpty());

    registrationService.register(testUser);

    verify(userDao, times(1)).save(testUser);
  }
}
