package by.bsuir.kostyademens.weatherapplication.validator;

import by.bsuir.kostyademens.weatherapplication.exception.EmailInvalidException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {
  public static void isValidEmail(String email) {
    try {
      new InternetAddress(email).validate();
    } catch (Exception exception) {
      throw new EmailInvalidException("Please, enter a valid email address");
    }
  }
}
