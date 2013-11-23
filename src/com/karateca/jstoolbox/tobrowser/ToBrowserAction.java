package com.karateca.jstoolbox.tobrowser;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author Andres Dominguez
 */
public class ToBrowserAction extends MyAction {
  @Override
  public void actionPerformed(AnActionEvent actionEvent) {
    if (!canEnableAction(actionEvent)) {
      return;
    }

    PsiFile file = actionEvent.getData(LangDataKeys.PSI_FILE);
    if (file == null) {
      return;
    }

    JsToolboxSettings settings = new JsToolboxSettings();

    String fileName = getCurrentFileName(actionEvent);

    if (settings.getUseFilePath()) {
      // Get the directory.
      PsiDirectory containingDirectory = file.getContainingDirectory();
      if (containingDirectory == null) {
        return;
      }

      String fromPath = settings.getFromPath();
      fileName = containingDirectory.toString().replaceFirst("^.*" + fromPath, fromPath) +
          System.getProperty("file.separator") + fileName;
    }

    String searchUrl = settings.getSearchUrl();

    String url = searchUrl.replace(JsToolboxSettings.FILE_NAME_TOKEN, fileName);
    openBrowser(url);
  }

  private void openBrowser(String url) {
    try {
      BrowserUtil.browse(url);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
