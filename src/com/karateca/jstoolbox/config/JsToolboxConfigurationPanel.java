package com.karateca.jstoolbox.config;

import javax.swing.*;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurationPanel {
  private JTextField unitTestSuffix;

  public JPanel getMyPanel() {
    return myPanel;
  }

  private JPanel myPanel;

  public void setTestSuffix(String testSuffix) {
    unitTestSuffix.setText(testSuffix);
  }

  public String getTestSuffix() {
    return unitTestSuffix.getText();
  }
}
