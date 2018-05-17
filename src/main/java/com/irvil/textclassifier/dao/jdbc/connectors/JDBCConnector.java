package com.irvil.textclassifier.dao.jdbc.connectors;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCConnector {
  Connection getConnection() throws SQLException;

  void createStorage();
}