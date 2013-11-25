package com.karateca.jstoolbox.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.karateca.jstoolbox.config.JsToolboxSettings.Property;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurable implements Configurable {

  private JsToolboxConfigurationPanel configurationPanel;
  private final JsToolboxSettings settings;

  public JsToolboxConfigurable() {
    settings = new JsToolboxSettings();
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

    // Reset on click.
    configurationPanel.getResetButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        configurationPanel.setTestSuffix(
            Property.TestSuffix.getDefaultValue());
        configurationPanel.setViewSuffix(
            Property.ViewSuffix.getDefaultValue());
        configurationPanel.setFileSuffix(
            Property.FileSuffix.getDefaultValue());
        configurationPanel.setSearchUrl(
            Property.SearchUrl.getDefaultValue());
        configurationPanel.setUseFilePath(
            Boolean.parseBoolean(
                Property.UseFilePath.getDefaultValue()));
        configurationPanel.setFromPath(
            Property.FromPath.getDefaultValue());
      }
    });

    reset();

    return configurationPanel.getMyPanel();
  }

  @Override
  public boolean isModified() {
    return !StringUtils.equals(settings.getTestSuffix(), configurationPanel.getTestSuffix()) ||
        !StringUtils.equals(settings.getViewSuffix(), configurationPanel.getViewSuffix()) ||
        !StringUtils.equals(settings.getFileSuffix(), configurationPanel.getFileSuffix()) ||
        !StringUtils.equals(settings.getSearchUrl(), configurationPanel.getSearchUrl()) ||
        !StringUtils.equals(settings.getFromPath(), configurationPanel.getFromPath()) ||
        settings.getUseFilePath() != configurationPanel.getUseFilePath();
  }

  @Override
  public void apply() throws ConfigurationException {
    settings.setTestSuffix(configurationPanel.getTestSuffix());
    settings.setFileSuffix(configurationPanel.getFileSuffix());
    settings.setViewSuffix(configurationPanel.getViewSuffix());
    settings.setSearchUrl(configurationPanel.getSearchUrl());
    settings.setUseFilePath(configurationPanel.getUseFilePath());
    settings.setFromPath(configurationPanel.getFromPath());
    settings.save();
  }

  @Override
  public void reset() {
    settings.load();
    configurationPanel.setTestSuffix(settings.getTestSuffix());
    configurationPanel.setViewSuffix(settings.getViewSuffix());
    configurationPanel.setFileSuffix(settings.getFileSuffix());
    configurationPanel.setSearchUrl(settings.getSearchUrl());
    configurationPanel.setUseFilePath(settings.getUseFilePath());
    configurationPanel.setFromPath(settings.getFromPath());
  }

  @Override
  public void disposeUIResources() {
    configurationPanel = null;
  }
}
