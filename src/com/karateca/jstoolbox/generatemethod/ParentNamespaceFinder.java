package com.karateca.jstoolbox.generatemethod;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EventDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
public class ParentNamespaceFinder extends ClassFinder {

  private final Project project;
  private final Document document;
  private final VirtualFile virtualFile;
  private final Editor editor;
  private final EventDispatcher<ChangeListener> myEventDispatcher =
      EventDispatcher.create(ChangeListener.class);

  private FindManager findManager;
  private String currentNamespace;
  private String parentNamespace;
  private List<Function> functionNames;

  public ParentNamespaceFinder(Project project, Document document,
      VirtualFile virtualFile, Editor editor) {
    super(document);
    this.project = project;
    this.document = document;
    this.virtualFile = virtualFile;
    this.editor = editor;
  }

  public String getCurrentNamespace() {
    return currentNamespace;
  }

  public String getParentNamespace() {
    return parentNamespace;
  }

  public List<Function> getFunctionNames() {
    return functionNames;
  }

  public void findParentClass() {
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      @Override
      public void run() {
        // First find the namespace for the current file.
        currentNamespace = findNamespaceForCurrentFile();
        if (currentNamespace == null) {
          return;
        }

        if (!findParentNamespace()) {
          return;
        }

        VirtualFile parentFile = findParentFile(parentNamespace);
        if (parentFile == null) {
          return;
        }

        try {
          functionNames = getMethods(parentFile);
          broadcastEvent("ParentNamespaceFound");
        } catch (Exception e) {
          System.err.println("Error reading file " + virtualFile.getName());
          e.printStackTrace(System.err);
        }
      }
    });
  }

  private VirtualFile findParentFile(final String namespace) {
    final VirtualFile[] parentFile = {null};

    ProjectRootManager.getInstance(editor.getProject()).getFileIndex()
        .iterateContent(new ContentIterator() {
          @Override
          public boolean processFile(VirtualFile virtualFile) {
            boolean parentFileFound = false;
            try {
              parentFileFound = fileProvidesNamespace(virtualFile, namespace);
            } catch (Exception e) {
              System.err.println("Error reading file " + virtualFile.getName());
              e.printStackTrace(System.err);
            }

            if (parentFileFound) {
              parentFile[0] = virtualFile;
            }

            // Stop the search.
            return !parentFileFound;
          }
        });

    return parentFile[0];
  }

  /**
   * Search for the file providing the namespace.
   */
  private boolean fileProvidesNamespace(
      VirtualFile virtualFile, String namespace) throws Exception {
    // Ignore non-js files.
    if (!virtualFile.getName().endsWith(".js")) {
      return false;
    }

    String provideSearchLine = "goog.provide('" + namespace;
    String fileContents = getFileContents(virtualFile);
    return fileContents.contains(provideSearchLine);
  }

  private String getFileContents(VirtualFile virtualFile) {
    Document doc = FileDocumentManager.getInstance().getDocument(virtualFile);
    return doc != null ? doc.getText() : "";
  }

  private List<Function> getMethods(VirtualFile virtualFile)
      throws Exception {
    List<Function> result = new ArrayList<Function>();

    String fileContents = getFileContents(virtualFile);
    String regexp = "(%s.prototype.)([\\w]+)(\\s*=\\s*function\\s*\\()" +
        "([\\w\\s\\n,]*)(\\))";
    String methodPattern = String.format(regexp, parentNamespace);

    Pattern pattern = Pattern.compile(methodPattern, Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(fileContents);
    while (matcher.find()) {
      String name = matcher.group(2);
      String arguments = matcher.group(4);
      result.add(new Function(name, arguments));
    }

    return result;
  }

  private TextRange getTextRange(int lineNumber) {
    int lineStart = document.getLineStartOffset(lineNumber);
    int lineEnd = document.getLineEndOffset(lineNumber);

    return new TextRange(lineStart, lineEnd);
  }

  private boolean findParentNamespace() {
    // TODO: return the name, if found.
    String documentText = document.getText();

    // Find @extends.
    int extendsOffset = documentText.indexOf("@extends");
    if (extendsOffset < 0) {
      return false;
    }

    // Find the next "*".
    int lineNumber = document.getLineNumber(extendsOffset);
    String line = document.getText(getTextRange(lineNumber));

    // Remove the beginning of the line.
    line = line.replaceAll("[\\s\\*]*@extends\\s+", "");

    // Remove the end of the line.
    line = line.replaceAll("\\s*$", "");

    // TODO: Temporary fix
    line = line.replace("{", "");
    line = line.replace("}", "");

    parentNamespace = line;

    return true;
  }

  private FindModel createFindModel() {
    findManager = FindManager.getInstance(project);
    FindModel clone = (FindModel) findManager.getFindInFileModel().clone();
    clone.setFindAll(true);
    clone.setFromCursor(true);
    clone.setForward(true);
    clone.setMultiline(true);
    clone.setRegularExpressions(true);
    clone.setWholeWordsOnly(false);
    clone.setCaseSensitive(true);
    clone.setSearchHighlighters(true);
    clone.setPreserveCase(false);

    return clone;
  }

  private void broadcastEvent(String eventName) {
    myEventDispatcher.getMulticaster().stateChanged(new ChangeEvent(eventName));
  }

  /**
   * Register for change events.
   *
   * @param changeListener The listener to be added.
   */
  public void addResultsReadyListener(ChangeListener changeListener) {
    myEventDispatcher.addListener(changeListener);
  }

}
