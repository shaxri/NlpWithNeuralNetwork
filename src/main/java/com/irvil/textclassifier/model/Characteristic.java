package com.irvil.textclassifier.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "CharacteristicsNames")
@NamedQuery(name = "Characteristic.findByName", query = "SELECT c FROM Characteristic c WHERE c.name=:characteristicName")
public class Characteristic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Name")
  private String name;

  @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<CharacteristicValue> possibleValues;

  public Characteristic() {
  }

  private Characteristic(int id, String name, Set<CharacteristicValue> possibleValues) {
    this.id = id;
    this.name = name;
    this.possibleValues = possibleValues;
  }

  public Characteristic(int id, String name) {
    this(id, name, new LinkedHashSet<>());
  }

  public Characteristic(String name, Set<CharacteristicValue> possibleValues) {
    this(0, name, possibleValues);
  }

  public Characteristic(String name) {
    this(0, name, new LinkedHashSet<>());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Set<CharacteristicValue> getPossibleValues() {
    return possibleValues;
  }

  public void setPossibleValues(Set<CharacteristicValue> possibleValues) {
    this.possibleValues = possibleValues;
  }

  public void addPossibleValue(CharacteristicValue value) {
    possibleValues.add(value);
  }

  @Override
  public boolean equals(Object o) {
    return ((o instanceof Characteristic) && (this.name.equals(((Characteristic) o).getName())));
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}