package com.irvil.textclassifier.dao;

import com.irvil.textclassifier.dao.factories.DAOFactory;
import com.irvil.textclassifier.model.Characteristic;
import com.irvil.textclassifier.model.CharacteristicValue;
import com.irvil.textclassifier.model.ClassifiableText;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public abstract class ClassifiableTextDAOTest {
  private ClassifiableTextDAO classifiableTextDAO;

  @Before
  public void setUp() throws Exception {
    DAOFactory daoFactory = createDAOFactory();
    classifiableTextDAO = daoFactory.classifiableTextDAO();
    Helper.fillStorageWithTestData(daoFactory);
  }

  protected abstract DAOFactory createDAOFactory();

  @Test
  public void getAll() throws Exception {
    List<ClassifiableText> classifiableTexts = classifiableTextDAO.getAll();

    // check size
    assertEquals(classifiableTexts.size(), 3);

    // check text
    //

    assertEquals(classifiableTexts.get(0).getText(), "text text");
    assertEquals(classifiableTexts.get(1).getText(), "text1 text1");
    assertEquals(classifiableTexts.get(2).getText(), "text1 text1");

    // check characteristics
    //

    assertEquals(classifiableTexts.get(0).getCharacteristics().size(), 2);
    assertEquals(classifiableTexts.get(0).getCharacteristicValue("Module").getOrderNumber(), 1);
    assertEquals(classifiableTexts.get(0).getCharacteristicValue("Module").getValue(), "PM");
    assertEquals(classifiableTexts.get(0).getCharacteristicValue("Handler").getOrderNumber(), 1);
    assertEquals(classifiableTexts.get(0).getCharacteristicValue("Handler").getValue(), "User 1");

    assertEquals(classifiableTexts.get(1).getCharacteristics().size(), 2);
    assertEquals(classifiableTexts.get(1).getCharacteristicValue("Module").getOrderNumber(), 2);
    assertEquals(classifiableTexts.get(1).getCharacteristicValue("Module").getValue(), "MM");
    assertEquals(classifiableTexts.get(1).getCharacteristicValue("Handler").getOrderNumber(), 2);
    assertEquals(classifiableTexts.get(1).getCharacteristicValue("Handler").getValue(), "User 2");

    assertEquals(classifiableTexts.get(2).getCharacteristics().size(), 2);
    assertEquals(classifiableTexts.get(2).getCharacteristicValue("Module").getOrderNumber(), 2);
    assertEquals(classifiableTexts.get(2).getCharacteristicValue("Module").getValue(), "MM");
    assertEquals(classifiableTexts.get(2).getCharacteristicValue("Handler").getOrderNumber(), 2);
    assertEquals(classifiableTexts.get(2).getCharacteristicValue("Handler").getValue(), "User 2");
  }

  @Test(expected = NotExistsException.class)
  public void addCharacteristicNotExists() throws Exception {
    Map<Characteristic, CharacteristicValue> characteristics = new HashMap<>();
    characteristics.put(new Characteristic("Module"), new CharacteristicValue("PM"));
    characteristics.put(new Characteristic("Test"), new CharacteristicValue("User 1"));

    List<ClassifiableText> classifiableTexts = new ArrayList<>();
    classifiableTexts.add(new ClassifiableText("text text", characteristics));
    classifiableTextDAO.addAll(classifiableTexts);
  }

  @Test(expected = NotExistsException.class)
  public void addCharacteristicValueNotExists() throws Exception {
    Map<Characteristic, CharacteristicValue> characteristics = new HashMap<>();
    characteristics.put(new Characteristic("Module"), new CharacteristicValue("PM"));
    characteristics.put(new Characteristic("Handler"), new CharacteristicValue("User 4"));

    List<ClassifiableText> classifiableTexts = new ArrayList<>();
    classifiableTexts.add(new ClassifiableText("text text", characteristics));
    classifiableTextDAO.addAll(classifiableTexts);
  }

  @Test
  public void add() throws Exception {
    List<ClassifiableText> classifiableTexts = new ArrayList<>();

    // normal text
    //

    Map<Characteristic, CharacteristicValue> characteristics = new HashMap<>();
    characteristics.put(new Characteristic("Module"), new CharacteristicValue("MM"));
    characteristics.put(new Characteristic("Handler"), new CharacteristicValue("User 1"));
    classifiableTexts.add(new ClassifiableText("text2 text2", characteristics));

    // empty text
    //

    Map<Characteristic, CharacteristicValue> characteristicsEmptyText = new HashMap<>();
    characteristicsEmptyText.put(new Characteristic("Handler"), new CharacteristicValue("User 1"));
    classifiableTexts.add(new ClassifiableText("", characteristicsEmptyText));

    // null Characteristic
    //

    classifiableTexts.add(new ClassifiableText("text text", null));

    // empty Characteristic
    //

    classifiableTexts.add(new ClassifiableText("text text", new HashMap<>()));

    // null
    //

    classifiableTexts.add(null);

    //

    classifiableTextDAO.addAll(classifiableTexts);

    // check record from DB
    //

    List<ClassifiableText> classifiableTextsFromDB = classifiableTextDAO.getAll();

    // check size
    assertEquals(classifiableTextsFromDB.size(), 4);

    // check text
    //

    assertEquals(classifiableTextsFromDB.get(3).getText(), "text2 text2");

    // check characteristics
    //

    assertEquals(classifiableTextsFromDB.get(3).getCharacteristics().size(), 2);
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Module").getId(), 2);
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Module").getOrderNumber(), 2);
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Module").getValue(), "MM");
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Handler").getId(), 3);
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Handler").getOrderNumber(), 1);
    assertEquals(classifiableTextsFromDB.get(3).getCharacteristicValue("Handler").getValue(), "User 1");
  }
}