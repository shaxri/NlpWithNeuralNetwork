package com.irvil.textclassifier.dao.jdbc.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCSQLiteConnector implements JDBCConnector {
  private final String dbName;

  public JDBCSQLiteConnector(String dbPath, String dbFileName) {
    if (dbFileName == null || dbFileName.equals("")) {
      throw new IllegalArgumentException();
    }

    this.dbName = dbPath + "/" + dbFileName;
  }

  @Override
  public Connection getConnection() throws SQLException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException ignored) {
    }

    return DriverManager.getConnection("jdbc:sqlite:" + dbName);
  }

  @Override
  public void createStorage() {
    List<String> sqlQueries = new ArrayList<>();

    // create database structure
    //

    sqlQueries.add("CREATE TABLE IF NOT EXISTS CharacteristicsNames " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE )");
    sqlQueries.add("CREATE TABLE IF NOT EXISTS CharacteristicsValues " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, OrderNumber INTEGER, CharacteristicsNameId INTEGER, Value TEXT)");
    sqlQueries.add("CREATE TABLE IF NOT EXISTS ClassifiableTexts " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Text TEXT )");
    sqlQueries.add("CREATE TABLE IF NOT EXISTS ClassifiableTextsCharacteristics " +
        "( ClassifiableTextId INTEGER, CharacteristicsNameId INTEGER, CharacteristicsValueId INTEGER, PRIMARY KEY(ClassifiableTextId,CharacteristicsNameId,CharacteristicsValueId) )");
    sqlQueries.add("CREATE TABLE IF NOT EXISTS Vocabulary " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Value TEXT UNIQUE )");

    // clear all tables
    //

    sqlQueries.add("DELETE FROM Vocabulary");
    sqlQueries.add("DELETE FROM ClassifiableTextsCharacteristics");
    sqlQueries.add("DELETE FROM ClassifiableTexts");
    sqlQueries.add("DELETE FROM CharacteristicsValues");
    sqlQueries.add("DELETE FROM CharacteristicsNames");

    // reset all autoincrement keys
    //

    sqlQueries.add("DELETE FROM sqlite_sequence WHERE name IN ('CharacteristicsNames' , " +
        "'CharacteristicsValues', 'ClassifiableTexts', 'ClassifiableTextsCharacteristics', 'Vocabulary')");

    // execute queries
    //

    try (Connection con = getConnection()) {
      Statement statement = con.createStatement();

      for (String sqlQuery : sqlQueries) {
        statement.execute(sqlQuery);
      }
    } catch (SQLException ignored) {
    }
  }
}