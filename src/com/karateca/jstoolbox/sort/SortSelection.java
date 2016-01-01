package com.karateca.jstoolbox.sort;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;
import org.apache.commons.lang.StringUtils;

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
    SortDialog dialog = new SortDialog(project);
    dialog.show();
    if (dialog.isOK()) {
      doSort(dialog.getSortToken(), SortDialog.ignoreCase);
    }
  }

  private void doSort(String separator, final boolean ignoreCase) {
    final SelectionModel selectionModel = editor.getSelectionModel();
    String selectedText = selectionModel.getSelectedText();
    if (selectedText == null) {
      return;
    }

    List<String> strings = Arrays.asList(selectedText.split(separator));
    Collections.sort(strings, new Comparator<String>() {
      @Override
      public int compare(String s, String s2) {
        if (ignoreCase) {
          s = s.toLowerCase();
          s2 = s2.toLowerCase();
        }
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
