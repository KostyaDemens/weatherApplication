package by.bsuir.kostyademens.weatherapplication.validator;

import by.bsuir.kostyademens.weatherapplication.exception.PasswordMismatchException;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordValidator {
  public static boolean checkPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  public static void validatePasswordMatch(String password, String confirmedPassword) {
    if (!password.equals(confirmedPassword)) {
      throw new PasswordMismatchException("Please, ensure passwords are the same");
    }
  }
}
