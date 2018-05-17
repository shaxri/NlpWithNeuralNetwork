package com.irvil.textclassifier.dao.jpa;

import com.irvil.textclassifier.dao.ClassifiableTextDAO;
import com.irvil.textclassifier.dao.NotExistsException;
import com.irvil.textclassifier.model.Characteristic;
import com.irvil.textclassifier.model.CharacteristicValue;
import com.irvil.textclassifier.model.ClassifiableText;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.*;

public class HibernateClassifiableTextDAO implements ClassifiableTextDAO {
  private EntityManagerFactory entityManagerFactory;

  public HibernateClassifiableTextDAO(EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory == null) {
      throw new IllegalArgumentException();
    }

    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public List<ClassifiableText> getAll() {
    Set<ClassifiableText> classifiableTextsWithoutDuplicates = new LinkedHashSet<>();
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      classifiableTextsWithoutDuplicates.addAll(manager.createQuery("SELECT c FROM ClassifiableText c JOIN c.characteristics v", ClassifiableText.class).getResultList());

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }

      e.printStackTrace();
    } finally {
      manager.close();
    }

    List<ClassifiableText> classifiableTexts = new ArrayList<>();
    classifiableTexts.addAll(classifiableTextsWithoutDuplicates);
    return classifiableTexts;
  }

  @Override
  public void addAll(List<ClassifiableText> classifiableTexts) throws NotExistsException {
    if (classifiableTexts == null ||
        classifiableTexts.size() == 0) {
      return;
    }

    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;

    try {
      transaction = manager.getTransaction();
      transaction.begin();

      for (ClassifiableText classifiableText : classifiableTexts) {
        if (classifiableText != null &&
            !classifiableText.getText().equals("") &&
            classifiableText.getCharacteristics() != null &&
            classifiableText.getCharacteristics().size() != 0) {

          if (!fillCharacteristicNamesAndValuesIDs(classifiableText)) {
            throw new NotExistsException("Characteristic value not exists");
          }

          manager.persist(classifiableText);
        }
      }

      transaction.commit();
    } catch (NotExistsException e) {
      throw e;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }

      e.printStackTrace();
    } finally {
      manager.close();
    }
  }

  private boolean fillCharacteristicNamesAndValuesIDs(ClassifiableText classifiableText) {
    for (Map.Entry<Characteristic, CharacteristicValue> entry : classifiableText.getCharacteristics().entrySet()) {
      Characteristic characteristic = new HibernateCharacteristicDAO(entityManagerFactory).findCharacteristicByName(entry.getKey().getName());

      if (characteristic == null) {
        return false;
      }

      // fill characteristic id from DB
      entry.getKey().setId(characteristic.getId());

      // fill characteristic value id and order number from DB
      //

      boolean characteristicValueIsFound = false;

      for (CharacteristicValue characteristicValue : characteristic.getPossibleValues()) {
        if (characteristicValue.getValue().equals(entry.getValue().getValue())) {
          entry.getValue().setId(characteristicValue.getId());
          entry.getValue().setOrderNumber(characteristicValue.getOrderNumber());
          characteristicValueIsFound = true;
        }
      }

      if (!characteristicValueIsFound) {
        return false;
      }
    }

    return true;
  }
}