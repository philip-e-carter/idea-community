package com.intellij.openapi.fileEditor.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Author: msk
 */
public class EditorWithProviderComposite extends EditorComposite {
  private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite");
  private final FileEditorProvider[] myProviders;

  EditorWithProviderComposite (
    final VirtualFile file,
    final FileEditor[] editors,
    final FileEditorProvider[] providers,
    final FileEditorManagerEx fileEditorManager
    ) {
    super(file, editors, fileEditorManager);
    myProviders = providers;
  }

  public FileEditorProvider[] getProviders() {
    return myProviders;
  }

  public boolean isModified() {
    final FileEditor [] editors = getEditors ();
    for (FileEditor editor : editors) {
      if (editor.isModified()) {
        return true;
      }
    }
    return false;
  }

  public Pair<FileEditor, FileEditorProvider> getSelectedEditorWithProvider() {
    LOG.assertTrue(myEditors.length > 0, myEditors.length);
    if(myEditors.length==1){
      LOG.assertTrue(myTabbedPaneWrapper==null);
      return Pair.create (myEditors[0], myProviders [0]);
    }
    else{ // we have to get myEditor from tabbed pane
      LOG.assertTrue(myTabbedPaneWrapper!=null);
      final int index=myTabbedPaneWrapper.getSelectedIndex();
      if (index == -1) {
        LOG.error("No selected editors for " + getFile());
      }
      LOG.assertTrue(index>=0, index);
      LOG.assertTrue(index<myEditors.length, index);
      return Pair.create (myEditors[index], myProviders [index]);
    }
  }
}