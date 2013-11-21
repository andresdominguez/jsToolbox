package com.karateca.jstoolbox.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurationPanel {
  private JTextField unitTestSuffix;

  public JsToolboxConfigurationPanel() {
    useFilePath.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fromPath.setEnabled(useFilePath.isSelected());
      }
    });
  }

  public JPanel getMyPanel() {
    return myPanel;
  }

  private JPanel myPanel;
  private JTextField fileSuffix;
  private JTextField viewSuffix;
  private JButton resetButton;
  private JTextField searchUrl;
  private JCheckBox useFilePath;
  private JTextField fromPath;

  public void setTestSuffix(String testSuffix) {
    unitTestSuffix.setText(testSuffix);
  }

  public String getTestSuffix() {
    return unitTestSuffix.getText();
  }

  public String getFileSuffix() {
    return fileSuffix.getText();
  }

  public void setFileSuffix(String suffix) {
    fileSuffix.setText(suffix);
  }

  public String getViewSuffix() {
    return viewSuffix.getText();
  }

  public void setViewSuffix(String suffix) {
    viewSuffix.setText(suffix);
  }

  public String getSearchUrl() {
    return searchUrl.getText();
  }

  public void setSearchUrl(String url) {
    searchUrl.setText(url);
  }

  public String getFromPath() {
    return fromPath.getText();
  }

  public void setFromPath(String dir) {
    fromPath.setText(dir);
  }

  public boolean getUseFilePath() {
    return useFilePath.isSelected();
  }

  public void setUseFilePath(boolean selected) {
    useFilePath.setSelected(selected);
    fromPath.setEnabled(selected);
  }

  public JButton getResetButton() {
    return resetButton;
  }
}
