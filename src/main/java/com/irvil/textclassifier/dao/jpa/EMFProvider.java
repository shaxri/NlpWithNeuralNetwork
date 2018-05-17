package com.irvil.textclassifier.dao.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMFProvider {
  private static final EMFProvider singleton = new EMFProvider();
  private EntityManagerFactory emf;

  private EMFProvider() {
  }

  public static EMFProvider getInstance() {
    return singleton;
  }

  public EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    return emf;
  }

  public void closeEntityManagerFactory() {
    if (emf != null) {
      emf.close();
    }

    emf = null;
  }
}