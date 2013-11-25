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

  private ConfigurationView view;
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
    if (view == null) {
      view = new ConfigurationView();
    }

    // Reset on click.
    view.getResetButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        view.setTestSuffix(
            Property.TestSuffix.getDefaultValue());
        view.setViewSuffix(
            Property.ViewSuffix.getDefaultValue());
        view.setFileSuffix(
            Property.FileSuffix.getDefaultValue());
        view.setSearchUrl(
            Property.SearchUrl.getDefaultValue());
        view.setUseFilePath(
            Boolean.parseBoolean(
                Property.UseFilePath.getDefaultValue()));
        view.setFromPath(
            Property.FromPath.getDefaultValue());
      }
    });

    reset();

    return view.getMyPanel();
  }

  @Override
  public boolean isModified() {
    return !StringUtils.equals(settings.getTestSuffix(), view.getTestSuffix()) ||
        !StringUtils.equals(settings.getViewSuffix(), view.getViewSuffix()) ||
        !StringUtils.equals(settings.getFileSuffix(), view.getFileSuffix()) ||
        !StringUtils.equals(settings.getSearchUrl(), view.getSearchUrl()) ||
        !StringUtils.equals(settings.getFromPath(), view.getFromPath()) ||
        settings.getUseFilePath() != view.getUseFilePath();
  }

  @Override
  public void apply() throws ConfigurationException {
    settings.setTestSuffix(view.getTestSuffix());
    settings.setFileSuffix(view.getFileSuffix());
    settings.setViewSuffix(view.getViewSuffix());
    settings.setSearchUrl(view.getSearchUrl());
    settings.setUseFilePath(view.getUseFilePath());
    settings.setFromPath(view.getFromPath());
    settings.save();
  }

  @Override
  public void reset() {
    settings.load();
    view.setTestSuffix(settings.getTestSuffix());
    view.setViewSuffix(settings.getViewSuffix());
    view.setFileSuffix(settings.getFileSuffix());
    view.setSearchUrl(settings.getSearchUrl());
    view.setUseFilePath(settings.getUseFilePath());
    view.setFromPath(settings.getFromPath());
  }

  @Override
  public void disposeUIResources() {
    view = null;
  }
}
