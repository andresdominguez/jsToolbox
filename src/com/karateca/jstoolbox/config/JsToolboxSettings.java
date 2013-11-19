package com.karateca.jstoolbox.config;

import com.intellij.ide.util.PropertiesComponent;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxSettings {

  public enum Property {
    TestSuffix("com.karateca.jstoolbox.testSuffix", "-spec.js"),
    FileSuffix("com.karateca.jstoolbox.fileSuffix", ".js"),
    ViewSuffix("com.karateca.jstoolbox.viewSuffix", ".html"),
    SearchUrl("com.karateca.jstoolbox.searchUrl", "https://github.com/search?q=FILE_NAME"),
    UseFilePath("com.karateca.jstoolbox.useFilePath", "false"),
    FromPath("com.karateca.jstoolbox.fromPath", "");

    private final String property;
    private final String defaultValue;

    private Property(String property, String defaultValue) {
      this.property = property;
      this.defaultValue = defaultValue;
    }

    public String getProperty() {
      return property;
    }

    public String getDefaultValue() {
      return defaultValue;
    }
  }

  public static final String FILE_NAME_TOKEN = "FILE_NAME";

  private final PropertiesComponent properties;

  private String fileSuffix;
  private String testSuffix;
  private String viewSuffix;
  private String searchUrl;
  private boolean useFilePath;
  private String fromPath;

  public JsToolboxSettings() {
    properties = PropertiesComponent.getInstance();
    load();
  }

  public void load() {
    this.testSuffix = getValue(Property.TestSuffix);
    this.fileSuffix = getValue(Property.FileSuffix);
    this.viewSuffix = getValue(Property.ViewSuffix);
    this.searchUrl = getValue(Property.SearchUrl);
    this.useFilePath = Boolean.valueOf(getValue(Property.UseFilePath));
    this.fromPath = getValue(Property.FromPath);
  }

  private String getValue(Property property) {
    return properties.getValue(
        property.getProperty(), Property.TestSuffix.getDefaultValue());
  }

  public void save() {
    setValue(Property.TestSuffix, testSuffix);
    setValue(Property.FileSuffix, fileSuffix);
    setValue(Property.ViewSuffix, viewSuffix);
    setValue(Property.SearchUrl, searchUrl);
    setValue(Property.UseFilePath, String.valueOf(this.useFilePath));
    setValue(Property.FromPath, fromPath);
  }

  private void setValue(Property property, String value) {
    properties.setValue(property.getProperty(), value);
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

  public boolean getUseFilePath() {
    return useFilePath;
  }

  public void setUseFilePath(boolean useFilePath) {
    this.useFilePath = useFilePath;
  }

  public String getFromPath() {
    return fromPath;
  }

  public void setFromPath(String fromPath) {
    this.fromPath = fromPath;
  }
}
