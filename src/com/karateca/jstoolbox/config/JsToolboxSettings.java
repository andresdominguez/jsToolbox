package com.karateca.jstoolbox.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * @author Andres Dominguez.
 */
@State(
    name = "JsToolboxSettings",
    storages = {
        @Storage(
            id = "main",
            file = "$APP_CONFIG$/js_toolbox_settings.xml"
        )}
)
public class JsToolboxSettings implements PersistentStateComponent<JsToolboxSettings> {

  private String textSuffix;

  public String getTextSuffix() {
    return textSuffix != null ? textSuffix : "spec";
  }

  public static JsToolboxSettings getInstance() {
    return ServiceManager.getService(JsToolboxSettings.class);
  }


  @Nullable
  @Override
  public JsToolboxSettings getState() {
    return this;
  }

  @Override
  public void loadState(JsToolboxSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
