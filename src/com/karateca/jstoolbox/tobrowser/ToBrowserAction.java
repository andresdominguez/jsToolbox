package com.karateca.jstoolbox.tobrowser;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class ToBrowserAction extends MyAction {
  @Override
  public void actionPerformed(AnActionEvent actionEvent) {
    if (!canEnableAction(actionEvent)) {
      return;
    }

    JsToolboxSettings settings = new JsToolboxSettings();

    String fileName = getCurrentFileName(actionEvent);

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
