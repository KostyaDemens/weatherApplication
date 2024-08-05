package by.bsuir.kostyademens.weatherapplication.filter;

import by.bsuir.kostyademens.weatherapplication.dao.SessionDao;
import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.model.Session;
import by.bsuir.kostyademens.weatherapplication.service.AuthorizationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(urlPatterns = {"/*"})
public class UserSessionFilter implements Filter {

  private final SessionDao sessionDao = new SessionDao();
  private final UserDao userDao = new UserDao();
  private AuthorizationService authService;

  @Override
  public void init(FilterConfig filterConfig) {
    authService = new AuthorizationService(sessionDao, userDao);
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    Cookie[] cookies = httpRequest.getCookies();

    if (httpRequest.getRequestURI().equals("/authorization")
        || httpRequest.getRequestURI().equals("/registration")) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    if (cookies != null) {

      Optional<Cookie> cookie =
          Arrays.stream(cookies).filter(n -> n.getName().equals("session_id")).findFirst();
      if (cookie.isPresent()) {
        Optional<Session> session = authService.getSession(cookie.get().getValue());
        if (session.isPresent()) {
          if (session.get().getUser() != null) {
            if (session.get().getExpiresAt().isBefore(LocalDateTime.now())) {
              sessionDao.delete(session.get());
              httpRequest
                  .getRequestDispatcher("/templates/sessionExpired.html")
                  .forward(httpRequest, httpResponse);
            } else {
              servletRequest.setAttribute("user", session.get().getUser());
              filterChain.doFilter(servletRequest, servletResponse);
              return;
            }
          } else {
            sessionDao.delete(session.get());
          }
        }
      }
    }

    httpResponse.sendRedirect(httpRequest.getContextPath() + "/authorization");
  }
}
