package com.karateca.jstoolbox.sort;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SortDialog extends DialogWrapper {

  private JTextField sortToken;
  static boolean ignoreCase;

  protected SortDialog(@Nullable Project project) {
    super(project);
    init();
    setTitle("Enter a string to split on");
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return sortToken;
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    String text = sortToken.getText();
    if (text.length() == 0) {
      return new ValidationInfo("Enter a value to sort on", sortToken);
    }
    return null;
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    sortToken = new JTextField();

    final JCheckBox checkBox = new JCheckBox("Ignore case when sorting");
    checkBox.setSelected(ignoreCase);
    checkBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ignoreCase = checkBox.isSelected();
      }
    });

    panel.add(sortToken, BorderLayout.CENTER);
    panel.add(checkBox, BorderLayout.PAGE_END);

    return panel;
  }

  public String getSortToken() {
    return sortToken.getText();
  }
}
