package com.intellij.codeInsight.template.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;

/**
 *
 */
public class EmptyNode extends Expression {
  public EmptyNode() {
  }

  public Result calculateResult(ExpressionContext context) {
    return null;
  }

  public Result calculateQuickResult(ExpressionContext context) {
    return null;
  }

  public LookupElement[] calculateLookupItems(ExpressionContext context) {
    return null;
  }

}