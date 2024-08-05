package by.bsuir.kostyademens.weatherapplication.controller;

import by.bsuir.kostyademens.weatherapplication.dao.LocationDao;
import by.bsuir.kostyademens.weatherapplication.dao.SessionDao;
import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.service.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@Slf4j
public class BaseServlet extends HttpServlet {

  private final LocationDao locationDao = new LocationDao();
  private final SessionDao sessionDao = new SessionDao();
  private final UserDao userDao = new UserDao();
  protected final AuthorizationService authService = new AuthorizationService(sessionDao, userDao);
  protected final RegistrationService registerService = new RegistrationService(userDao);
  private final OpenWeatherService openWeatherService = new OpenWeatherService();
  protected final UserService userService = new UserService(locationDao, openWeatherService);
  protected final LocationService locationService =
      new LocationService(locationDao, openWeatherService);

  protected TemplateEngine engine;

  protected WebContext context;
  protected IWebExchange webExchange;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    webExchange =
        JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
    context = new WebContext(webExchange);
    try {
      super.service(req, resp);
    } catch (Exception e) {
      log.info("An error occurred during request processing", e);
      context.setVariable("error", "Произошла ошибка, попробуйте позже");
      engine.process("error", context, resp.getWriter());
    }
  }
}
