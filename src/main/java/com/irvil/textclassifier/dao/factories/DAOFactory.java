package com.irvil.textclassifier.dao.factories;

import com.irvil.textclassifier.Config;
import com.irvil.textclassifier.dao.CharacteristicDAO;
import com.irvil.textclassifier.dao.ClassifiableTextDAO;
import com.irvil.textclassifier.dao.StorageCreator;
import com.irvil.textclassifier.dao.VocabularyWordDAO;
import com.irvil.textclassifier.dao.jdbc.connectors.JDBCConnector;
import com.irvil.textclassifier.dao.jdbc.connectors.JDBCH2Connector;
import com.irvil.textclassifier.dao.jdbc.connectors.JDBCSQLiteConnector;
import com.irvil.textclassifier.dao.jpa.EMFProvider;

import javax.persistence.EntityManagerFactory;

public interface DAOFactory {
  static DAOFactory getDaoFactory(Config config) {
    DAOFactory daoFactory = null;

    // create DAO factory depends on config values
    //

    try {
      if (config.getDaoType().equals("jdbc")) {
        // create connector depends on config value
        //

        JDBCConnector jdbcConnector = null;

        if (config.getDBMSType().equals("sqlite")) {
          jdbcConnector = new JDBCSQLiteConnector(config.getDbPath(), config.getDbFileName());
        }

        if (config.getDBMSType().equals("h2")) {
          jdbcConnector = new JDBCH2Connector(config.getDbPath(), config.getDbFileName());
        }

        // create factory
        daoFactory = new JDBCDAOFactory(jdbcConnector);
      } else if (config.getDaoType().equals("hibernate")) {
        EntityManagerFactory entityManagerFactory = EMFProvider.getInstance().getEntityManagerFactory("TextClassifier");
        daoFactory = new HibernateDAOFactory(entityManagerFactory);
      }
    } catch (IllegalArgumentException e) {
      return null;
    }

    return daoFactory;
  }

  ClassifiableTextDAO classifiableTextDAO();

  CharacteristicDAO characteristicDAO();

  VocabularyWordDAO vocabularyWordDAO();

  StorageCreator storageCreator();
}