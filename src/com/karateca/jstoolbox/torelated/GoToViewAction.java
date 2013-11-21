package com.karateca.jstoolbox.torelated;

import java.util.List;

/**
 * @author Andres Dominguez
 */
public class GoToViewAction extends GoToRelatedAction {

  @Override
  List<String> getDestinationSuffixList() {
    return viewSuffixList;
  }
}
