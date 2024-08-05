package by.bsuir.kostyademens.weatherapplication.dao;

import by.bsuir.kostyademens.weatherapplication.model.Location;
import by.bsuir.kostyademens.weatherapplication.model.User;
import by.bsuir.kostyademens.weatherapplication.util.SessionFactoryUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class LocationDao {

  private final SessionFactoryUtil sessionFactoryUtil;

  public LocationDao() {
    this.sessionFactoryUtil = SessionFactoryUtil.getInstance();
  }

  public void save(Location location) {
    try (Session session = sessionFactoryUtil.getSession()) {
      Transaction transaction = session.getTransaction();
      try {
        transaction.begin();
        session.persist(location);
        transaction.commit();
      } catch (Exception e) {
        transaction.rollback();
        throw new RuntimeException(e);
      }
    }
  }

  public List<Location> findUserLocations(User user) {
    try (Session session = sessionFactoryUtil.getSession()) {
      session.beginTransaction();
      List<Location> location =
          session
              .createQuery("FROM Location l WHERE l.user = :user", Location.class)
              .setParameter("user", user)
              .getResultList();
      session.getTransaction().commit();
      return location;
    }
  }

  public void delete(Location location) {
    try (Session session = sessionFactoryUtil.getSession()) {
      Transaction transaction = session.getTransaction();
      try {
        transaction.begin();
        Query<?> query =
            session
                .createQuery(
                    "DELETE FROM Location l WHERE l.user = :user AND l.latitude = :lat AND l.longitude = :lon")
                .setParameter("user", location.getUser())
                .setParameter("lat", location.getLatitude())
                .setParameter("lon", location.getLongitude());
        query.executeUpdate();
        transaction.commit();
      } catch (Exception e) {
        transaction.rollback();
        throw new RuntimeException(e);
      }
    }
  }
}
