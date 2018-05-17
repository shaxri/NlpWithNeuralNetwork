package com.irvil.textclassifier.dao.jdbc;

import com.irvil.textclassifier.Config;
import com.irvil.textclassifier.dao.VocabularyWordDAOTest;
import com.irvil.textclassifier.dao.factories.DAOFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCH2VocabularyWordDAOTest extends VocabularyWordDAOTest {
  @Override
  public DAOFactory createDAOFactory() {
    Config cfg = mock(Config.class);

    when(cfg.getDaoType()).thenReturn("jdbc");
    when(cfg.getDbFileName()).thenReturn("h2_test_db");
    when(cfg.getDBMSType()).thenReturn("h2");
    when(cfg.getDbPath()).thenReturn("./db");

    return DAOFactory.getDaoFactory(cfg);
  }
}