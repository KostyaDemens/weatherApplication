package by.bsuir.kostyademens.weatherapplication.controller.auth;

import by.bsuir.kostyademens.weatherapplication.controller.BaseServlet;
import by.bsuir.kostyademens.weatherapplication.dto.UserDto;
import by.bsuir.kostyademens.weatherapplication.exception.AuthorizationException;
import by.bsuir.kostyademens.weatherapplication.model.Session;
import by.bsuir.kostyademens.weatherapplication.validator.ParameterValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/authorization")
public class AuthorizationServlet extends BaseServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/templates/authorization.html").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    UserDto userDto = new UserDto(email, password);

    try {
      if (ParameterValidator.areNotNull(email, password)) {
        Session session = authService.login(userDto);
        resp.addCookie(authService.getNewCookie(session));
        resp.sendRedirect(req.getContextPath() + "/home-page");
      }
    } catch (AuthorizationException e) {
      context.setVariable("authError", e.getMessage());
      engine.process("authorization", context, resp.getWriter());
    }
  }
}
