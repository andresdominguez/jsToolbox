package com.karateca.jstoolbox.config;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurable implements Configurable {

  public static final String TEST_SUFFIX = "com.karateca.jstoolbox.unitTestSuffix";
  private final PropertiesComponent propertiesComponent;
  private JsToolboxConfigurationPanel configurationPanel;

  public JsToolboxConfigurable() {
    propertiesComponent = PropertiesComponent.getInstance();
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "JS Toolbox";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return "Configure the default settings for the JS Toolbox";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    if (configurationPanel == null) {
      configurationPanel = new JsToolboxConfigurationPanel();
    }

    reset();

    return configurationPanel.getMyPanel();
  }

  @Override
  public boolean isModified() {
    return !getTestSuffix().equals(configurationPanel.getTestSuffix());
  }

  @Override
  public void apply() throws ConfigurationException {
    propertiesComponent.setValue(TEST_SUFFIX, configurationPanel.getTestSuffix());
  }

  @Override
  public void reset() {

    getTestSuffix();
    configurationPanel.setTestSuffix(getTestSuffix());
  }

  private String getTestSuffix() {
    return propertiesComponent.getValue(TEST_SUFFIX, "spec");
  }

  @Override
  public void disposeUIResources() {
    configurationPanel = null;
  }
}
