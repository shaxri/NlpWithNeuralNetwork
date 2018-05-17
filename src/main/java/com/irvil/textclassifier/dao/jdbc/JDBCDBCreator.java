package com.irvil.textclassifier.dao.jdbc;

import com.irvil.textclassifier.dao.StorageCreator;
import com.irvil.textclassifier.dao.jdbc.connectors.JDBCConnector;

public class JDBCDBCreator implements StorageCreator {
  private JDBCConnector connector;

  public JDBCDBCreator(JDBCConnector connector) {
    if (connector == null) {
      throw new IllegalArgumentException();
    }

    this.connector = connector;
  }

  @Override
  public void createStorage() {
    connector.createStorage();
  }
}