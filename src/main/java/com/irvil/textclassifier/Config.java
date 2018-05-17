package com.irvil.textclassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// todo: add default configuration and create config file automatically
public class Config {
  private final Properties properties = new Properties();

  public Config(String fileName) {
    // read config file
    try (InputStream inputStream = new FileInputStream(new File(fileName))) {
      properties.load(inputStream);
    } catch (IOException ignored) {

    }
  }

  public boolean isLoaded() {
    return properties.size() > 0;
  }

  public String getDbPath() {
    return getProperty("db_path");
  }

  public String getDaoType() {
    return getProperty("dao_type");
  }

  public String getDBMSType() {
    return getProperty("dbms_type");
  }

  public String getDbFileName() {
    return getProperty("db_filename");
  }

  public String getNGramStrategy() {
    return getProperty("ngram_strategy");
  }

  private String getProperty(String property) {
    return properties.getProperty(property) != null ? properties.getProperty(property) : "";
  }
}