package com.irvil.textclassifier.dao.jpa;

import com.irvil.textclassifier.dao.ClassifiableTextDAOTest;
import com.irvil.textclassifier.dao.factories.DAOFactory;
import com.irvil.textclassifier.dao.factories.HibernateDAOFactory;
import org.junit.AfterClass;

import javax.persistence.EntityManagerFactory;

public class HibernateClassifiableTextDAOTest extends ClassifiableTextDAOTest {
  @Override
  public DAOFactory createDAOFactory() {
    EntityManagerFactory entityManagerFactory = EMFProvider.getInstance().getEntityManagerFactory("TextClassifier_test");
    return new HibernateDAOFactory(entityManagerFactory);
  }

  @AfterClass
  public static void closeEntityManagerFactory() {
    EMFProvider.getInstance().closeEntityManagerFactory();
  }
}