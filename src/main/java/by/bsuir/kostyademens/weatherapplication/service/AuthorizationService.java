package by.bsuir.kostyademens.weatherapplication.service;

import by.bsuir.kostyademens.weatherapplication.dao.SessionDao;
import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.dto.UserDto;
import by.bsuir.kostyademens.weatherapplication.exception.AuthorizationException;
import by.bsuir.kostyademens.weatherapplication.model.Session;
import by.bsuir.kostyademens.weatherapplication.model.User;
import by.bsuir.kostyademens.weatherapplication.util.PropertyReader;
import by.bsuir.kostyademens.weatherapplication.validator.PasswordValidator;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthorizationService {

  private final SessionDao sessionDao;
  private final UserDao userDao;

  public Session login(UserDto userDto) {
    Optional<User> user = userDao.findByLogin(userDto.getEmail());
    if (user.isPresent()
        && PasswordValidator.checkPassword((userDto.getPassword()), user.get().getPassword())) {
      Session session = getNewSession(user.get());
      sessionDao.save(session);
      return session;
    } else {
      throw new AuthorizationException("Invalid username or password");
    }
  }

  public Cookie getNewCookie(Session session) {
    Cookie cookie = new Cookie("session_id", session.getId());
    cookie.setMaxAge(Integer.parseInt(PropertyReader.getProperty("COOKIE_LIFETIME")));
    return cookie;
  }

  public Session getNewSession(User user) {
    Session session = new Session();
    session.setUser(user);
    session.setExpiresAt(
        LocalDateTime.now()
            .plusMinutes(Long.parseLong(PropertyReader.getProperty("SESSION_LIFETIME"))));
    return session;
  }

  public Optional<Session> getSession(String uuid) {
    return sessionDao.findById(uuid);
  }
}
