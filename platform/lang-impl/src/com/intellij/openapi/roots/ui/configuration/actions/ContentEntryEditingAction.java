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

package com.intellij.openapi.roots.ui.configuration.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import com.intellij.openapi.actionSystem.impl.ActionButtonWithText;
import com.intellij.openapi.fileChooser.ex.FileNodeDescriptor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Eugene Zhuravlev
 *         Date: Oct 14
 * @author 2003
 *         Time: 3:07:14 PM
 */
public abstract class ContentEntryEditingAction extends ToggleAction implements CustomComponentAction, DumbAware {
  protected final JTree myTree;

  protected ContentEntryEditingAction(JTree tree) {
    myTree = tree;
    getTemplatePresentation().setEnabled(true);
  }

  public void update(AnActionEvent e) {
    super.update(e);
    final Presentation presentation = e.getPresentation();
    presentation.setEnabled(true);
    final VirtualFile[] files = getSelectedFiles();
    if (files == null || files.length == 0) {
      presentation.setEnabled(false);
      return;
    }
    for (VirtualFile file : files) {
      if (file == null || !file.isDirectory()) {
        presentation.setEnabled(false);
        break;
      }
    }
  }

  @Nullable
  protected final VirtualFile[] getSelectedFiles() {
    final TreePath[] selectionPaths = myTree.getSelectionPaths();
    if (selectionPaths == null) {
      return null;
    }
    final VirtualFile[] selected = new VirtualFile[selectionPaths.length];
    for (int i = 0; i < selectionPaths.length; i++) {
      TreePath treePath = selectionPaths[i];
      final DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
      final Object nodeDescriptor = node.getUserObject();
      if (!(nodeDescriptor instanceof FileNodeDescriptor)) {
        return null;
      }
      selected[i] = ((FileNodeDescriptor)nodeDescriptor).getElement().getFile();
    }
    return selected;
  }

  public JComponent createCustomComponent(Presentation presentation) {
    return new ActionButtonWithText(this, presentation, ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE);
  }
}
