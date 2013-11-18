package com.karateca.jstoolbox.torelated;

import java.util.Arrays;
import java.util.List;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GoToViewAction extends GoToRelatedAction {

  @Override
  List<String> getDestinationSuffixList() {
    return Arrays.asList(viewSuffix);
  }
}
