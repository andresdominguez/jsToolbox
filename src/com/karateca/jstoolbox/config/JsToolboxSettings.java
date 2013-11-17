package com.karateca.jstoolbox.config;

import com.intellij.ide.util.PropertiesComponent;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxSettings {

  public static final String TEST_SUFFIX_PROP = "com.karateca.jstoolbox.testSuffix";
  public static final String FILE_SUFFIX_PROP = "com.karateca.jstoolbox.fileSuffix";
  public static final String VIEW_SUFFIX_PROP = "com.karateca.jstoolbox.viewSuffix";

  public static final String DEFAULT_TEST_SUFFIX = "-spec.js";
  public static final String DEFAULT_FILE_SUFFIX = ".js";
  public static final String DEFAULT_VIEW_SUFFIX = ".html";

  private final PropertiesComponent properties;

  private String fileSuffix;
  private String testSuffix;
  private String viewSuffix;

  public JsToolboxSettings() {
    properties = PropertiesComponent.getInstance();
    load();
  }

  public void load() {
    this.testSuffix = properties.getValue(TEST_SUFFIX_PROP, DEFAULT_TEST_SUFFIX);
    this.fileSuffix = properties.getValue(FILE_SUFFIX_PROP, DEFAULT_FILE_SUFFIX);
    this.viewSuffix = properties.getValue(VIEW_SUFFIX_PROP, DEFAULT_VIEW_SUFFIX);
  }

  public void save() {
    properties.setValue(TEST_SUFFIX_PROP, testSuffix);
    properties.setValue(FILE_SUFFIX_PROP, fileSuffix);
    properties.setValue(VIEW_SUFFIX_PROP, viewSuffix);
  }

  public String getFileSuffix() {
    return fileSuffix;
  }

  public void setFileSuffix(String fileSuffix) {
    this.fileSuffix = fileSuffix;
  }

  public String getTestSuffix() {
    return testSuffix;
  }

  public void setTestSuffix(String testSuffix) {
    this.testSuffix = testSuffix;
  }

  public String getViewSuffix() {
    return viewSuffix;
  }

  public void setViewSuffix(String viewSuffix) {
    this.viewSuffix = viewSuffix;
  }

  public void resetDefaultValues() {
    this.fileSuffix = DEFAULT_FILE_SUFFIX;
    this.viewSuffix = DEFAULT_VIEW_SUFFIX;
    this.testSuffix = DEFAULT_TEST_SUFFIX;
  }
}
