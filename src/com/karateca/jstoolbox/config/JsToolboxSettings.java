package com.karateca.jstoolbox.config;

import com.intellij.ide.util.PropertiesComponent;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxSettings {

  private static final String TEST_SUFFIX_PROP = "com.karateca.jstoolbox.testSuffix";
  private static final String FILE_SUFFIX_PROP = "com.karateca.jstoolbox.fileSuffix";
  private static final String VIEW_SUFFIX_PROP = "com.karateca.jstoolbox.viewSuffix";
  private static final String SEARCH_URL_PROP = "com.karateca.jstoolbox.searchUrl";

  public static final String DEFAULT_TEST_SUFFIX = "-spec.js";
  public static final String DEFAULT_FILE_SUFFIX = ".js";
  public static final String DEFAULT_VIEW_SUFFIX = ".html";
  public static final String DEFAULT_SEARCH_URL = "https://github.com/search?q=%3CFILE_NAME%3E";

  private final PropertiesComponent properties;

  private String fileSuffix;
  private String testSuffix;
  private String viewSuffix;
  private String searchUrl;

  public JsToolboxSettings() {
    properties = PropertiesComponent.getInstance();
    load();
  }

  public void load() {
    this.testSuffix = properties.getValue(TEST_SUFFIX_PROP, DEFAULT_TEST_SUFFIX);
    this.fileSuffix = properties.getValue(FILE_SUFFIX_PROP, DEFAULT_FILE_SUFFIX);
    this.viewSuffix = properties.getValue(VIEW_SUFFIX_PROP, DEFAULT_VIEW_SUFFIX);
    this.searchUrl = properties.getValue(SEARCH_URL_PROP, DEFAULT_SEARCH_URL);
  }

  public void save() {
    properties.setValue(TEST_SUFFIX_PROP, testSuffix);
    properties.setValue(FILE_SUFFIX_PROP, fileSuffix);
    properties.setValue(VIEW_SUFFIX_PROP, viewSuffix);
    properties.setValue(SEARCH_URL_PROP, searchUrl);
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

  public String getSearchUrl() {
    return searchUrl;
  }

  public void setSearchUrl(String searchUrl) {
    this.searchUrl = searchUrl;
  }
}
