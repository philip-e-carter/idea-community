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
package com.intellij.psi.formatter.java;

import com.intellij.lang.ASTNode;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Block;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import java.util.List;

public class MethodCallExpressionBlock extends AbstractJavaBlock{
  public MethodCallExpressionBlock(final ASTNode node, final Wrap wrap, final Alignment alignment, final Indent indent, final CodeStyleSettings settings) {
    super(node, wrap, alignment, indent, settings);
  }

  protected List<Block> buildChildren() {
    return null;
  }
}
