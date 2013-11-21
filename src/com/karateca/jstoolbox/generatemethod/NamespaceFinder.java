package com.karateca.jstoolbox.generatemethod;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.find.FindResult;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EventDispatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
class NamespaceFinder {
  private final Project project;
   private final DocumentImpl document;
   private final VirtualFile virtualFile;
   private FindManager findManager;
   private FindModel findModel;
   private String namespaceFound;

   private final EventDispatcher<ChangeListener> myEventDispatcher = EventDispatcher.create(ChangeListener.class);
   private final Pattern REQUIRE_PATTERN = Pattern.compile("(goog.provide\\(['\"]([\\w.]+))(['\"])");

   public NamespaceFinder(Project project, DocumentImpl document, VirtualFile virtualFile) {
     this.project = project;
     this.document = document;
     this.virtualFile = virtualFile;
   }

   FindModel createFindModel(FindManager findManager) {
     FindModel clone = (FindModel) findManager.getFindInFileModel().clone();
     clone.setFindAll(true);
     clone.setFromCursor(true);
     clone.setForward(true);
     clone.setWholeWordsOnly(false);
     clone.setCaseSensitive(true);
     clone.setSearchHighlighters(true);
     clone.setPreserveCase(false);

     return clone;
   }

   public void findText(final String text, boolean isRegEx) {
     findManager = FindManager.getInstance(project);
     findModel = createFindModel(findManager);

     findModel.setStringToFind(text);
     findModel.setRegularExpressions(isRegEx);

     ApplicationManager.getApplication().runReadAction(new Runnable() {
       @Override
       public void run() {
         findNamespace();
         myEventDispatcher.getMulticaster().stateChanged(new ChangeEvent("NamespaceFound"));
       }
     });
   }

   private void findNamespace() {
     namespaceFound = null;
     CharSequence text = document.getCharsSequence();

     FindResult result = findManager.findString(text, 0, findModel, virtualFile);

     if (!result.isStringFound()) {
       return;
     }

     String lineText = getLineOfCode(result);

     Matcher matcher = REQUIRE_PATTERN.matcher(lineText);
     if (matcher.find()) {
       namespaceFound = matcher.group(2);
     }
   }

   private String getLineOfCode(FindResult result) {
     int lineNumber = document.getLineNumber(result.getEndOffset());
     int startOfLine = document.getLineStartOffset(lineNumber);
     int endOfLine = document.getLineEndOffset(lineNumber);

     return document.getText(new TextRange(startOfLine, endOfLine));
   }

   /**
    * Register for change events.
    *
    * @param changeListener The listener to be added.
    */
   public void addResultsReadyListener(ChangeListener changeListener) {
     myEventDispatcher.addListener(changeListener);
   }

   public String getNamespaceFound() {
     return namespaceFound;
   }
}
