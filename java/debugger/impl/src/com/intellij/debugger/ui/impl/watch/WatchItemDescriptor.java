/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Class WatchItemDescriptor
 * @author Jeka
 */
package com.intellij.debugger.ui.impl.watch;

import com.intellij.debugger.engine.StackFrameContext;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.EvaluationContextImpl;
import com.intellij.debugger.engine.evaluation.TextWithImports;
import com.intellij.debugger.impl.PositionUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.sun.jdi.Value;

/**
 * update(Value, boolean) method must be called whenever the state of the target VM changes
 */
public class WatchItemDescriptor extends EvaluationDescriptor {

  public WatchItemDescriptor(Project project, TextWithImports text) {
    super(text, project);
    setValueLabel("");
  }

  public WatchItemDescriptor(Project project, TextWithImports text, Value value) {
    super(text, project, value);
    setValueLabel("");
  }

  public String getName() {
    return getEvaluationText().getText();
  }

  public void setNew() {
    myIsNew = true;
  }

  // call update() after setting a new expression
  public void setEvaluationText(TextWithImports evaluationText) {
    if (!Comparing.equal(getEvaluationText(), evaluationText)) {
      setLvalue(false);
    }
    myText = evaluationText;
    myIsNew = true;
    setValueLabel("");
  }

  protected EvaluationContextImpl getEvaluationContext(EvaluationContextImpl evaluationContext) {
    return evaluationContext;
  }

  protected PsiCodeFragment getEvaluationCode(StackFrameContext context) throws EvaluateException {
    final PsiElement psiContext = PositionUtil.getContextElement(context);
    final PsiCodeFragment fragment = getEffectiveCodeFragmentFactory(psiContext).createCodeFragment(getEvaluationText(), psiContext, myProject);
    fragment.forceResolveScope(GlobalSearchScope.allScope(myProject));
    return fragment;
  }
}