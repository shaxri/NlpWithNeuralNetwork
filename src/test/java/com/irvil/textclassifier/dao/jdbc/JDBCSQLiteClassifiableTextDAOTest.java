package com.irvil.textclassifier.dao.jdbc;

import com.irvil.textclassifier.Config;
import com.irvil.textclassifier.dao.ClassifiableTextDAOTest;
import com.irvil.textclassifier.dao.factories.DAOFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCSQLiteClassifiableTextDAOTest extends ClassifiableTextDAOTest {
  @Override
  public DAOFactory createDAOFactory() {
    Config cfg = mock(Config.class);

    when(cfg.getDaoType()).thenReturn("jdbc");
    when(cfg.getDbFileName()).thenReturn("sqlite_test_db");
    when(cfg.getDBMSType()).thenReturn("sqlite");
    when(cfg.getDbPath()).thenReturn("./db");

    return DAOFactory.getDaoFactory(cfg);
  }
}