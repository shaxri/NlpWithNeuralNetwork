package com.irvil.textclassifier.dao.jdbc;

import com.irvil.textclassifier.dao.AlreadyExistsException;
import com.irvil.textclassifier.dao.VocabularyWordDAO;
import com.irvil.textclassifier.dao.jdbc.connectors.JDBCConnector;
import com.irvil.textclassifier.model.VocabularyWord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCVocabularyWordDAO implements VocabularyWordDAO {
  private JDBCConnector connector;

  public JDBCVocabularyWordDAO(JDBCConnector connector) {
    if (connector == null) {
      throw new IllegalArgumentException();
    }

    this.connector = connector;
  }

  @Override
  public List<VocabularyWord> getAll() {
    List<VocabularyWord> vocabularyWords = new ArrayList<>();

    try (Connection con = connector.getConnection()) {
      String sql = "SELECT Id, Value FROM Vocabulary";
      ResultSet rs = con.createStatement().executeQuery(sql);

      while (rs.next()) {
        vocabularyWords.add(new VocabularyWord(rs.getInt("Id"), rs.getString("Value")));
      }
    } catch (SQLException ignored) {
    }

    return vocabularyWords;
  }

  @Override
  public void addAll(List<VocabularyWord> vocabulary) throws AlreadyExistsException {
    if (vocabulary == null ||
        vocabulary.size() == 0) {
      return;
    }

    try (Connection con = connector.getConnection()) {
      con.setAutoCommit(false);

      // prepare sql query
      //

      String sqlInsert = "INSERT INTO Vocabulary (Value) VALUES (?)";
      PreparedStatement statement = con.prepareStatement(sqlInsert);

      //

      for (VocabularyWord vocabularyWord : vocabulary) {
        if (vocabularyWord != null &&
            !vocabularyWord.getValue().equals("") &&
            !isVocabularyWordExistsInDB(con, vocabularyWord)) {

          statement.setString(1, vocabularyWord.getValue());
          statement.executeUpdate();
        }
      }

      con.commit();
      con.setAutoCommit(true);
    } catch (SQLException ignored) {
    }
  }

  private boolean isVocabularyWordExistsInDB(Connection con, VocabularyWord vocabularyWord) throws SQLException {
    String sqlSelect = "SELECT Id FROM Vocabulary WHERE Value = ?";
    PreparedStatement statement = con.prepareStatement(sqlSelect);
    statement.setString(1, vocabularyWord.getValue());
    ResultSet rs = statement.executeQuery();

    return rs.next();
  }
}