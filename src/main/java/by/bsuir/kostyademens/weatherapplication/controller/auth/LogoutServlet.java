package by.bsuir.kostyademens.weatherapplication.controller.auth;

import by.bsuir.kostyademens.weatherapplication.controller.BaseServlet;
import by.bsuir.kostyademens.weatherapplication.dao.SessionDao;
import by.bsuir.kostyademens.weatherapplication.model.Session;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

  private final SessionDao sessionDao = new SessionDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Cookie[] cookies = req.getCookies();
    Cookie cookie =
        Arrays.stream(cookies)
            .filter(n -> n.getName().equals("session_id"))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Session cookie not found"));
    Optional<Session> session = authService.getSession(cookie.getValue());
    sessionDao.delete(session.orElseThrow());
    resp.sendRedirect(req.getContextPath() + "/authorization");
  }
}
