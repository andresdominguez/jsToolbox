package com.karateca.jstoolbox.sort;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.ui.awt.RelativePoint;
import com.karateca.jstoolbox.MyAction;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortSelection extends MyAction {

  private Project project;
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
    String sortToken = showDialog();
    if (sortToken != null) {
      doSort(sortToken);
    }
  }

  private String showDialog() {
    SortDialog dialog = new SortDialog();
    dialog.pack();
    RelativePoint relativePoint = guessBestLocation(editor);
    dialog.setLocation(relativePoint.getOriginalPoint());
    dialog.setVisible(true);
    return dialog.clickedOk ? dialog.getSortToken() : null;
  }

  private RelativePoint guessBestLocation(Editor editor) {
    VisualPosition logicalPosition = editor.getCaretModel().getVisualPosition();
    return getPointFromVisualPosition(editor, logicalPosition);
  }

  private RelativePoint getPointFromVisualPosition(Editor editor, VisualPosition logicalPosition) {
    Point p = editor.visualPositionToXY(new VisualPosition(logicalPosition.line, logicalPosition.column));
    return new RelativePoint(editor.getContentComponent(), p);
  }

  private void doSort(String separator) {
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
  }
}
