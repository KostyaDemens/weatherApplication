package by.bsuir.kostyademens.weatherapplication.controller.auth;

import by.bsuir.kostyademens.weatherapplication.controller.BaseServlet;
import by.bsuir.kostyademens.weatherapplication.exception.EmailInvalidException;
import by.bsuir.kostyademens.weatherapplication.exception.PasswordMismatchException;
import by.bsuir.kostyademens.weatherapplication.exception.UserAlreadyExistsException;
import by.bsuir.kostyademens.weatherapplication.model.User;
import by.bsuir.kostyademens.weatherapplication.validator.EmailValidator;
import by.bsuir.kostyademens.weatherapplication.validator.ParameterValidator;
import by.bsuir.kostyademens.weatherapplication.validator.PasswordValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/templates/registration.html").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String confirmedPassword = req.getParameter("confirmedPassword");

    User user = new User(email, password);

    Map<String, String> errors = new HashMap<>();

    if (ParameterValidator.areNotNull(email, password, confirmedPassword)) {
      try {
        PasswordValidator.validatePasswordMatch(password, confirmedPassword);
      } catch (PasswordMismatchException e) {
        errors.put("passwordError", e.getMessage());
      }

      try {
        EmailValidator.isValidEmail(email);
      } catch (EmailInvalidException e) {
        errors.put("emailError", e.getMessage());
      }

      if (errors.isEmpty()) {
        try {
          registerService.register(user);
          resp.sendRedirect(req.getContextPath() + "/authorization");
          return;
        } catch (UserAlreadyExistsException e) {
          errors.put("emailError", e.getMessage());
        }
      }

      errors.forEach(context::setVariable);
      engine.process("registration", context, resp.getWriter());
    }
  }
}
