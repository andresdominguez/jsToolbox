package com.karateca.jstoolbox.sort;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.IdeFocusManager;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SortSelection extends AnAction {

  public void actionPerformed(AnActionEvent actionEvent) {

    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }

    EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();

    Font font = new Font(scheme.getEditorFontName(), Font.BOLD, scheme.getConsoleFontSize());

    SearchBox searchBox = new SearchBox();
    int width = searchBox.getFontMetrics(font).stringWidth("w");
    Dimension dimension = new Dimension(width * 2, editor.getLineHeight());
    if (SystemInfo.isMac) {
      dimension.setSize(dimension.width * 2, dimension.height * 2);
    }

    searchBox.setSize(dimension);
    searchBox.setFocusable(true);
    searchBox.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent focusEvent) {
        System.out.println("gained");
      }

      @Override
      public void focusLost(FocusEvent focusEvent) {
        System.out.println("lost");
      }
    });
  }

  void focus() {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override
      public void run() {
        IdeFocusManager.getInstance(project).requestFocus(searchBox, true);
      }
    });
  }
}
