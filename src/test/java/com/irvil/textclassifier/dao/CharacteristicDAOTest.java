package com.irvil.textclassifier.dao;

import com.irvil.textclassifier.dao.factories.DAOFactory;
import com.irvil.textclassifier.model.Characteristic;
import com.irvil.textclassifier.model.CharacteristicValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class CharacteristicDAOTest {
  private CharacteristicDAO characteristicDAO;

  @Before
  public void setUp() throws Exception {
    DAOFactory daoFactory = createDAOFactory();
    characteristicDAO = daoFactory.characteristicDAO();
    Helper.fillStorageWithTestData(daoFactory);
  }

  protected abstract DAOFactory createDAOFactory();

  @Test
  public void getAllCharacteristics() throws Exception {
    List<Characteristic> characteristics = characteristicDAO.getAllCharacteristics();

    // check characteristics
    //

    assertEquals(characteristics.size(), 2);

    assertEquals(characteristics.get(0).getId(), 1);
    assertEquals(characteristics.get(0).getName(), "Module");

    assertEquals(characteristics.get(1).getId(), 2);
    assertEquals(characteristics.get(1).getName(), "Handler");

    // check Module possible values
    //

    Set<CharacteristicValue> modulePossibleValues = characteristics.get(0).getPossibleValues();
    CharacteristicValue valPM = null;
    CharacteristicValue valMM = null;

    for (CharacteristicValue characteristicValue : modulePossibleValues) {
      if (characteristicValue.getOrderNumber() == 1) {
        valPM = characteristicValue;
      } else if (characteristicValue.getOrderNumber() == 2) {
        valMM = characteristicValue;
      }
    }

    assertEquals(modulePossibleValues.size(), 2);

    assertNotEquals(valPM, null);
    assertEquals(valPM.getOrderNumber(), 1);
    assertEquals(valPM.getValue(), "PM");

    assertNotEquals(valMM, null);
    assertEquals(valMM.getOrderNumber(), 2);
    assertEquals(valMM.getValue(), "MM");

    // check Handler possible values
    //

    Set<CharacteristicValue> handlerPossibleValues = characteristics.get(1).getPossibleValues();
    Iterator<CharacteristicValue> handlerIterator = handlerPossibleValues.iterator();
    CharacteristicValue valUser1 = handlerIterator.next();
    CharacteristicValue valUser2 = handlerIterator.next();
    CharacteristicValue valUser3 = handlerIterator.next();

    assertEquals(handlerPossibleValues.size(), 3);

    assertEquals(valUser1.getOrderNumber(), 1);
    assertEquals(valUser1.getValue(), "User 1");

    assertEquals(valUser2.getOrderNumber(), 2);
    assertEquals(valUser2.getValue(), "User 2");

    assertEquals(valUser3.getOrderNumber(), 3);
    assertEquals(valUser3.getValue(), "User 3");
  }

  @Test(expected = AlreadyExistsException.class)
  public void addCharacteristicExisted() throws Exception {
    Set<CharacteristicValue> possibleValues = new LinkedHashSet<>();
    possibleValues.add(new CharacteristicValue("BC"));
    characteristicDAO.addCharacteristic(new Characteristic("Module", possibleValues));
  }

  @Test
  public void addCharacteristic() throws Exception {
    Set<CharacteristicValue> possibleValues = new LinkedHashSet<>();
    possibleValues.add(new CharacteristicValue("Value 1"));
    possibleValues.add(new CharacteristicValue(""));
    possibleValues.add(null);
    possibleValues.add(new CharacteristicValue("Value 2"));
    possibleValues.add(new CharacteristicValue("Value 2"));
    Characteristic characteristic = new Characteristic("Test", possibleValues);

    characteristic = characteristicDAO.addCharacteristic(characteristic);

    // check returned object
    //

    Iterator<CharacteristicValue> iterator = characteristic.getPossibleValues().iterator();
    CharacteristicValue valValue1 = iterator.next();
    CharacteristicValue valValue2 = iterator.next();

    assertEquals(characteristic.getId(), 3);
    assertEquals(characteristic.getName(), "Test");

    assertEquals(characteristic.getPossibleValues().size(), 2);

    assertEquals(valValue1.getOrderNumber(), 1);
    assertEquals(valValue1.getValue(), "Value 1");
    assertEquals(valValue2.getOrderNumber(), 2);
    assertEquals(valValue2.getValue(), "Value 2");

    // check record from DB
    //

    List<Characteristic> characteristicsFromDb = characteristicDAO.getAllCharacteristics();

    assertEquals(characteristicsFromDb.size(), 3);

    assertEquals(characteristicsFromDb.get(2).getId(), 3);
    assertEquals(characteristicsFromDb.get(2).getName(), "Test");

    // check Test possible values
    //

    Set<CharacteristicValue> testPossibleValues = characteristicsFromDb.get(2).getPossibleValues();
    Iterator<CharacteristicValue> iteratorTest = testPossibleValues.iterator();
    CharacteristicValue valTestValue1 = iteratorTest.next();
    CharacteristicValue valTestValue2 = iteratorTest.next();

    assertEquals(testPossibleValues.size(), 2);

    assertEquals(valTestValue1.getOrderNumber(), 1);
    assertEquals(valTestValue1.getValue(), "Value 1");

    assertEquals(valTestValue2.getOrderNumber(), 2);
    assertEquals(valTestValue2.getValue(), "Value 2");
  }
}