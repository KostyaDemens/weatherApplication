package by.bsuir.kostyademens.weatherapplication.dao;

import by.bsuir.kostyademens.weatherapplication.model.Session;
import by.bsuir.kostyademens.weatherapplication.util.SessionFactoryUtil;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.hibernate.Transaction;

public class SessionDao {

  private final SessionFactoryUtil sessionFactoryUtil;

  public SessionDao() {
    this.sessionFactoryUtil = SessionFactoryUtil.getInstance();
  }

  public void save(Session entity) {
    try (org.hibernate.Session session = sessionFactoryUtil.getSession()) {
      Transaction transaction = session.getTransaction();
      try {
        transaction.begin();
        session.persist(entity);
        transaction.commit();
      } catch (Exception e) {
        transaction.rollback();
        throw new RuntimeException(e);
      }
    }
  }

  public Optional<Session> findById(String uuid) {
    try (org.hibernate.Session session = sessionFactoryUtil.getSession()) {
      session.beginTransaction();
      Session entity =
          session
              .createQuery("FROM Session s WHERE s.id = :uuid", Session.class)
              .setParameter("uuid", uuid)
              .getSingleResult();
      session.getTransaction().commit();
      return Optional.of(entity);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public void delete(Session entity) {
    try (org.hibernate.Session session = sessionFactoryUtil.getSession()) {
      Transaction transaction = session.getTransaction();
      try {
        transaction.begin();
        session.remove(entity);
        transaction.commit();
      } catch (Exception e) {
        transaction.rollback();
        throw new RuntimeException(e);
      }
    }
  }
}
