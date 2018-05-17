package com.irvil.textclassifier.model;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "ClassifiableTexts")
public class ClassifiableText {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Text", length = 10000)
  private String text;

  @ManyToMany
  @JoinTable(name = "ClassifiableTextsCharacteristics",
      joinColumns = @JoinColumn(name = "ClassifiableTextId"),
      inverseJoinColumns = @JoinColumn(name = "CharacteristicsValueId"))
  @MapKeyJoinColumn(name = "CharacteristicsNameId")
  private Map<Characteristic, CharacteristicValue> characteristics;

  public ClassifiableText() {
  }

  public ClassifiableText(String text, Map<Characteristic, CharacteristicValue> characteristics) {
    this.text = text;
    this.characteristics = characteristics;
  }

  public ClassifiableText(String text) {
    this(text, null);
  }

  public String getText() {
    return text;
  }

  public Map<Characteristic, CharacteristicValue> getCharacteristics() {
    return characteristics;
  }

  public CharacteristicValue getCharacteristicValue(String characteristicName) {
    return characteristics.get(new Characteristic(characteristicName));
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof ClassifiableText) && this.text.equals(((ClassifiableText) o).getText()) && this.characteristics.equals(((ClassifiableText) o).getCharacteristics());
  }
}