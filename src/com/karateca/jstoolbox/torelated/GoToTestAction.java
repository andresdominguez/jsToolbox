package com.karateca.jstoolbox.torelated;

import java.util.List;

/**
 * @author Andres Dominguez
 */
public class GoToTestAction extends GoToRelatedAction {

  @Override
  List<String> getDestinationSuffixList() {
    return testSuffixList;
  }
}
