package com.karateca.jstoolbox.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        configurationPanel.setTestSuffix(JsToolboxSettings.DEFAULT_TEST_SUFFIX);
        configurationPanel.setViewSuffix(JsToolboxSettings.DEFAULT_VIEW_SUFFIX);
        configurationPanel.setFileSuffix(JsToolboxSettings.DEFAULT_FILE_SUFFIX);
        configurationPanel.setSearchUrl(JsToolboxSettings.DEFAULT_SEARCH_URL);
        configurationPanel.setUseFilePath(
            JsToolboxSettings.DEFAULT_USE_FILE_PATH);
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
        settings.getUseFilePath() != configurationPanel.getUseFilePath();
  }

  @Override
  public void apply() throws ConfigurationException {
    settings.setTestSuffix(configurationPanel.getTestSuffix());
    settings.setFileSuffix(configurationPanel.getFileSuffix());
    settings.setViewSuffix(configurationPanel.getViewSuffix());
    settings.setSearchUrl(configurationPanel.getSearchUrl());
    settings.setUseFilePath(configurationPanel.getUseFilePath());
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
  }

  @Override
  public void disposeUIResources() {
    configurationPanel = null;
  }
}
