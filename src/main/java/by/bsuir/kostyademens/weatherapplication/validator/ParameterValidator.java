package by.bsuir.kostyademens.weatherapplication.validator;

public class ParameterValidator {

  public static boolean areNotNull(String... strings) {
    for (String str : strings) {
      if (str == null) {
        return false;
      }
    }
    return true;
  }
}
