package by.bsuir.kostyademens.weatherapplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import by.bsuir.kostyademens.weatherapplication.dao.SessionDao;
import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.dto.UserDto;
import by.bsuir.kostyademens.weatherapplication.exception.AuthorizationException;
import by.bsuir.kostyademens.weatherapplication.model.Session;
import by.bsuir.kostyademens.weatherapplication.model.User;
import by.bsuir.kostyademens.weatherapplication.validator.PasswordValidator;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  private User validUser;
  private UserDto validUserDto;

  @InjectMocks private AuthorizationService authorizationService;

  @Mock private UserDao userDao;

  @Mock private SessionDao sessionDao;

  @BeforeEach
  void prepare() {
    validUserDto = new UserDto("valid@example.com", "validPassword");
    validUser = new User("valid@example.com", "hashedPassword");
  }

  @Test
  void shouldThrowAnExceptionIfLoginOrPasswordAreNotCorrect() {
    when(userDao.findByLogin(anyString())).thenReturn(Optional.of(validUser));

    try (MockedStatic<PasswordValidator> mockStatic = mockStatic(PasswordValidator.class)) {
      mockStatic
          .when(() -> PasswordValidator.checkPassword(anyString(), anyString()))
          .thenReturn(false);

      assertThrows(AuthorizationException.class, () -> authorizationService.login(validUserDto));
    }
  }

  @Test
  void shouldThrowAnExceptionIfUserDoesNotExists() {
    when(userDao.findByLogin(anyString())).thenReturn(Optional.empty());

    assertThrows(AuthorizationException.class, () -> authorizationService.login(validUserDto));
  }

  @Test
  void shouldReturnSessionIfCredentialsAreValid() {
    when(userDao.findByLogin(anyString())).thenReturn(Optional.of(validUser));

    try (MockedStatic<PasswordValidator> mockStatic = mockStatic(PasswordValidator.class)) {
      mockStatic
          .when(() -> PasswordValidator.checkPassword(anyString(), anyString()))
          .thenReturn(true);

      Session returnedSession = authorizationService.login(validUserDto);

      verify(sessionDao, times(1)).save(returnedSession);

      assertNotNull(returnedSession);

      assertEquals(validUser, returnedSession.getUser());
      assertTrue(returnedSession.getExpiresAt().isAfter(LocalDateTime.now()));
    }
  }

  @Test
  void shouldCreateCookieWithCorrectAttributes() {
    LocalDateTime expiration = LocalDateTime.now().plusHours(5);
    Session session = new Session("id", validUser, expiration);

    Cookie cookie = authorizationService.getNewCookie(session);

    assertNotNull(cookie);
    assertEquals(session.getId(), cookie.getValue());
    assertEquals("session_id", cookie.getName());
    assertTrue(cookie.getMaxAge() >= 5 * 60 * 60);
  }
}
