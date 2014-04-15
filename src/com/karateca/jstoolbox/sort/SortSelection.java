package com.karateca.jstoolbox.sort;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.awt.RelativePoint;
import com.karateca.jstoolbox.MyAction;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class SortSelection extends MyAction {

  private Project project;
  private JTextField searchBox;
  private JBPopup popup;
  private Editor editor;

  @Override
  protected boolean canEnableAction(AnActionEvent e) {
    // Must have a selection to do the sort.
    editor = getEditor(e);
    if (editor == null) {
      return false;
    }

    return editor.getSelectionModel().hasSelection();
  }

  public void actionPerformed(AnActionEvent actionEvent) {
    getEditor(actionEvent);
    if (editor == null) {
      return;
    }

    project = actionEvent.getProject();
    searchBox = new JTextField();

    Dimension dimension = getDimension(editor);
    showSearchBox(dimension);
  }

  private Dimension getDimension(Editor editor) {
    EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();

    Font font = new Font(scheme.getEditorFontName(), Font.BOLD, scheme.getConsoleFontSize());

    int width = searchBox.getFontMetrics(font).stringWidth("w");
    Dimension dimension = new Dimension(width * 2, editor.getLineHeight());
    if (SystemInfo.isMac) {
      dimension.setSize(dimension.width * 2, dimension.height * 2);
    }
    return dimension;
  }

  private void showSearchBox(Dimension dimension) {
    searchBox.setSize(dimension);
    searchBox.setFocusable(true);
    searchBox.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
          doSort();
        }
        super.keyPressed(keyEvent);
      }
    });

    searchBox.setVisible(true);

    popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(searchBox, searchBox)
            .setCancelKeyEnabled(true)
            .createPopup();

    popup.show(guessBestLocation(editor));
    popup.setSize(dimension);

    // Get focus.
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override
      public void run() {
        IdeFocusManager.getInstance(project).requestFocus(searchBox, true);
      }
    });
  }

  private void doSort() {
    String separator = searchBox.getText();
    final SelectionModel selectionModel = editor.getSelectionModel();
    String selectedText = selectionModel.getSelectedText();
    if (selectedText == null) {
      return;
    }

    List<String> strings = Arrays.asList(selectedText.split(separator));
    Collections.sort(strings, new Comparator<String>() {
      @Override
      public int compare(String s, String s2) {
        return s.trim().compareTo(s2.trim());
      }
    });

    final String join = StringUtils.join(strings, separator);
    final Document document = editor.getDocument();
    runWriteActionInsideCommand(project, "Joining", new Runnable() {
      @Override
      public void run() {
        document.replaceString(
                selectionModel.getSelectionStart(),
                selectionModel.getSelectionEnd(),
                join
        );
      }
    });
    popup.closeOk(null);
  }

  private RelativePoint guessBestLocation(Editor editor) {
    VisualPosition logicalPosition = editor.getCaretModel().getVisualPosition();
    return getPointFromVisualPosition(editor, logicalPosition);
  }

  private RelativePoint getPointFromVisualPosition(Editor editor, VisualPosition logicalPosition) {
    Point p = editor.visualPositionToXY(new VisualPosition(logicalPosition.line, logicalPosition.column));
    return new RelativePoint(editor.getContentComponent(), p);
  }
}
