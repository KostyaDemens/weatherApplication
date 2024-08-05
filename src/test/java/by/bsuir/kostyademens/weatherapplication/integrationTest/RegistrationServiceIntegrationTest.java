package by.bsuir.kostyademens.weatherapplication.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import by.bsuir.kostyademens.weatherapplication.dao.UserDao;
import by.bsuir.kostyademens.weatherapplication.exception.UserAlreadyExistsException;
import by.bsuir.kostyademens.weatherapplication.model.User;
import by.bsuir.kostyademens.weatherapplication.service.RegistrationService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

public class RegistrationServiceIntegrationTest {

  private static SessionFactory sessionFactory;
  private User user;
  private RegistrationService registrationService;

  @AfterAll
  static void tearDown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @BeforeAll
  static void createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
    sessionFactory = configuration.buildSessionFactory();
  }

  @BeforeEach
  void setUp() {
    user = new User("user@gmail.com", "123");
    UserDao userDao = new UserDao();
    registrationService = new RegistrationService(userDao);
  }

  @AfterEach
  void cleanUp() {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.createQuery("DELETE FROM User").executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Test
  void userShouldBeSavedInDataBase() {
    registrationService.register(user);

    User savedUser = findById(user.getId());

    assertNotNull(savedUser);
    assertEquals(savedUser.getEmail(), user.getEmail());
  }

  @Test
  void shouldThrowAnExceptionIfUserAlreadyExists() {
    registrationService.register(user);

    assertNotNull(findById(user.getId()));

    Assertions.assertThrows(
        UserAlreadyExistsException.class, () -> registrationService.register(user));
  }

  private User findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      User user =
          session
              .createQuery("FROM User u WHERE u.id = :id", User.class)
              .setParameter("id", id)
              .getSingleResult();
      session.getTransaction().commit();
      return user;
    }
  }
}
