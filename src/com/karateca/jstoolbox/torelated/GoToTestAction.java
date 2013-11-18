package com.karateca.jstoolbox.torelated;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GoToTestAction extends GoToRelatedAction {

  @Override
  String getDestinationSuffix() {
    return testSuffix;
  }
}
