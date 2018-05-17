package com.irvil.textclassifier.dao.jpa;

import com.irvil.textclassifier.dao.StorageCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class HibernateDBCreator implements StorageCreator {
  private EntityManagerFactory entityManagerFactory;

  public HibernateDBCreator(EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory == null) {
      throw new IllegalArgumentException();
    }

    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void createStorage() {
    List<String> sqlQueries = new ArrayList<>();

    // clear all tables
    //

    sqlQueries.add("DELETE FROM Vocabulary");
    sqlQueries.add("DELETE FROM ClassifiableTextsCharacteristics");
    sqlQueries.add("DELETE FROM ClassifiableTexts");
    sqlQueries.add("DELETE FROM CharacteristicsValues");
    sqlQueries.add("DELETE FROM CharacteristicsNames");

    // reset all autoincrement keys
    //

    sqlQueries.add("ALTER TABLE CharacteristicsNames ALTER COLUMN Id RESTART WITH 1");
    sqlQueries.add("ALTER TABLE CharacteristicsValues ALTER COLUMN Id RESTART WITH 1");
    sqlQueries.add("ALTER TABLE ClassifiableTexts ALTER COLUMN Id RESTART WITH 1");
    sqlQueries.add("ALTER TABLE Vocabulary ALTER COLUMN Id RESTART WITH 1");

    // execute queries
    //

    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      for (String sqlQuery : sqlQueries) {
        manager.createNativeQuery(sqlQuery).executeUpdate();
      }

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }

      e.printStackTrace();
    } finally {
      manager.close();
    }
  }
}