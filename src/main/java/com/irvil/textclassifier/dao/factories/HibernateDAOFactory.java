package com.irvil.textclassifier.dao.factories;

import com.irvil.textclassifier.dao.CharacteristicDAO;
import com.irvil.textclassifier.dao.ClassifiableTextDAO;
import com.irvil.textclassifier.dao.StorageCreator;
import com.irvil.textclassifier.dao.VocabularyWordDAO;
import com.irvil.textclassifier.dao.jpa.HibernateCharacteristicDAO;
import com.irvil.textclassifier.dao.jpa.HibernateClassifiableTextDAO;
import com.irvil.textclassifier.dao.jpa.HibernateDBCreator;
import com.irvil.textclassifier.dao.jpa.HibernateVocabularyWordDAO;

import javax.persistence.EntityManagerFactory;

public class HibernateDAOFactory implements DAOFactory {
  private EntityManagerFactory entityManagerFactory;

  public HibernateDAOFactory(EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory == null) {
      throw new IllegalArgumentException();
    }

    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public ClassifiableTextDAO classifiableTextDAO() {
    return new HibernateClassifiableTextDAO(entityManagerFactory);
  }

  @Override
  public CharacteristicDAO characteristicDAO() {
    return new HibernateCharacteristicDAO(entityManagerFactory);
  }

  @Override
  public VocabularyWordDAO vocabularyWordDAO() {
    return new HibernateVocabularyWordDAO(entityManagerFactory);
  }

  @Override
  public StorageCreator storageCreator() {
    return new HibernateDBCreator(entityManagerFactory);
  }
}