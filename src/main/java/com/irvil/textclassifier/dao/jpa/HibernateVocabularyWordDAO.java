package com.irvil.textclassifier.dao.jpa;

import com.irvil.textclassifier.dao.AlreadyExistsException;
import com.irvil.textclassifier.dao.VocabularyWordDAO;
import com.irvil.textclassifier.model.VocabularyWord;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HibernateVocabularyWordDAO implements VocabularyWordDAO {
  private EntityManagerFactory entityManagerFactory;

  public HibernateVocabularyWordDAO(EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory == null) {
      throw new IllegalArgumentException();
    }

    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public List<VocabularyWord> getAll() {
    List<VocabularyWord> vocabulary = new ArrayList<>();
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      vocabulary = manager.createQuery("SELECT v FROM VocabularyWord v", VocabularyWord.class).getResultList();

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }

      e.printStackTrace();
    } finally {
      manager.close();
    }

    return vocabulary;
  }

  @Override
  public void addAll(List<VocabularyWord> vocabulary) throws AlreadyExistsException {
    if (vocabulary == null ||
        vocabulary.size() == 0) {
      return;
    }

    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      for (VocabularyWord vocabularyWord : new LinkedHashSet<>(vocabulary)) {
        if (vocabularyWord != null &&
            !vocabularyWord.getValue().equals("") &&
            !isVocabularyWordExistsInDB(vocabularyWord)) {
          manager.persist(vocabularyWord);
        }
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

  private boolean isVocabularyWordExistsInDB(VocabularyWord vocabularyWord) {
    VocabularyWord foundVocabularyWord = null;

    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      TypedQuery<VocabularyWord> query = manager.createQuery("SELECT v FROM VocabularyWord v WHERE v.value=:value", VocabularyWord.class);
      query.setParameter("value", vocabularyWord.getValue());

      try {
        foundVocabularyWord = query.getSingleResult();
      } catch (NoResultException ignored) {

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

    return (foundVocabularyWord != null);
  }
}