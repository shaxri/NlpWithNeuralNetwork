package com.irvil.textclassifier.dao;

import com.irvil.textclassifier.dao.factories.DAOFactory;
import com.irvil.textclassifier.model.VocabularyWord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class VocabularyWordDAOTest {
  private VocabularyWordDAO vocabularyWordDAO;

  @Before
  public void setUp() throws Exception {
    DAOFactory daoFactory = createDAOFactory();
    vocabularyWordDAO = daoFactory.vocabularyWordDAO();
    Helper.fillStorageWithTestData(daoFactory);
  }

  protected abstract DAOFactory createDAOFactory();

  @Test
  public void getAll() throws Exception {
    List<VocabularyWord> vocabularyWords = vocabularyWordDAO.getAll();

    assertEquals(vocabularyWords.size(), 2);

    assertEquals(vocabularyWords.get(0).getId(), 1);
    assertEquals(vocabularyWords.get(0).getValue(), "Test 1");

    assertEquals(vocabularyWords.get(1).getId(), 2);
    assertEquals(vocabularyWords.get(1).getValue(), "Test 2");
  }

  @Test
  public void add() throws Exception {
    List<VocabularyWord> vocabulary = new ArrayList<>();
    vocabulary.add(new VocabularyWord("Test 1"));
    vocabulary.add(new VocabularyWord("Test 3"));
    vocabulary.add(new VocabularyWord("Test 3"));
    vocabulary.add(new VocabularyWord(""));
    vocabulary.add(null);
    vocabularyWordDAO.addAll(vocabulary);

    // check record from DB
    //

    List<VocabularyWord> vocabularyWords = vocabularyWordDAO.getAll();

    assertEquals(vocabularyWords.size(), 3);

    assertEquals(vocabularyWords.get(2).getId(), 3);
    assertEquals(vocabularyWords.get(2).getValue(), "Test 3");
  }
}