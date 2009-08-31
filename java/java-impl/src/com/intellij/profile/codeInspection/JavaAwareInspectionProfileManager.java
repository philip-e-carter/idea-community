/*
 * User: anna
 * Date: 21-Feb-2008
 */
package com.intellij.profile.codeInspection;

import com.intellij.codeInsight.daemon.InspectionProfileConvertor;
import com.intellij.codeInsight.daemon.JavaAwareInspectionProfileCoverter;
import com.intellij.codeInspection.ex.InspectionToolRegistrar;
import com.intellij.openapi.options.SchemesManagerFactory;

public class JavaAwareInspectionProfileManager extends InspectionProfileManager{
  public JavaAwareInspectionProfileManager(InspectionToolRegistrar registrar, SchemesManagerFactory schemesManagerFactory) {
    super(registrar, schemesManagerFactory);
  }

  @Override
  public InspectionProfileConvertor getConverter() {
    return new JavaAwareInspectionProfileCoverter(this);
  }
}