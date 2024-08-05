package by.bsuir.kostyademens.weatherapplication.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {

  private static final PropertyReader instance = new PropertyReader();
  private final Properties props;

  private PropertyReader() {
    String propsFileName = System.getenv("propsFileName");
    if (propsFileName == null) {
      propsFileName = "application.properties";
    }
    this.props = substituteEnvVars(readProps(propsFileName));
  }

  public static String getProperty(String key) {
    return instance.props.getProperty(key);
  }

  private Properties readProps(String filename) {
    Properties properties = new Properties();
    try (InputStream is = PropertyReader.class.getClassLoader().getResourceAsStream(filename)) {
      properties.load(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return properties;
  }

  private Properties substituteEnvVars(Properties props) {
    Properties updatedProps = new Properties();
    for (Map.Entry<Object, Object> pair : props.entrySet()) {
      String key = (String) pair.getKey();
      String value = (String) pair.getValue();
      if (value != null && value.matches("^\\$\\{.+}$")) {
        String envVar = value.substring(2, value.length() - 1);
        try {
          value = System.getenv(envVar);
          if (value == null) {
            System.out.printf("No env variable %s set%n", envVar);
          }
        } catch (SecurityException exc) {
          System.out.printf(
              "Failed accessing env variable %s due to security restrictions%n", envVar);
          value = null;
        }
      }
      updatedProps.put(key, value);
    }
    return updatedProps;
  }
}
