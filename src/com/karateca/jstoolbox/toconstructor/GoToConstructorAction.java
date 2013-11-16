package com.karateca.jstoolbox.toconstructor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.karateca.jstoolbox.MyAction;

/**
 * @author Andres Dominguez.
 */
public class GoToConstructorAction extends MyAction {

  public void actionPerformed(AnActionEvent actionEvent) {
    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }

    Document document = editor.getDocument();

    String text = document.getText();

    // Find the @constructor and then go to the first function.
    int offset = text.indexOf("@constructor");
    offset = text.indexOf("function", offset);

    editor.getCaretModel().moveToOffset(offset);
  }
}
