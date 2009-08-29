package com.intellij.openapi.components.impl.stores;

import com.intellij.openapi.application.ex.DecodeDefaultsUtil;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.components.StateStorage;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.util.xmlb.JDOMXIncluder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Set;


class DefaultsStateStorage implements StateStorage {
  private final PathMacroManager myPathMacroManager;


  public DefaultsStateStorage(@Nullable final PathMacroManager pathMacroManager) {
    myPathMacroManager = pathMacroManager;
  }

  @Nullable
  public Element getState(final Object component, final String componentName) throws StateStorageException {
    final URL url = DecodeDefaultsUtil.getDefaults(component, componentName);
    if (url == null) return null;

    try {
      Document document = JDOMUtil.loadDocument(url);
      document = JDOMXIncluder.resolve(document, url.toExternalForm());
      final Element documentElement = document.getRootElement();

      if (myPathMacroManager != null) {
        myPathMacroManager.expandPaths(documentElement);
      }

      return documentElement;
    }
    catch (IOException e) {
      throw new StateStorageException("Error loading state from " + url, e);
    }
    catch (JDOMException e) {
      throw new StateStorageException("Error loading state from " + url, e);
    }
  }

  @Nullable
  public <T> T getState(final Object component, final String componentName, final Class<T> stateClass, @Nullable final T mergeInto) throws
                                                                                                                                    StateStorageException {
    return DefaultStateSerializer.deserializeState(getState(component, componentName), stateClass, mergeInto);
  }

  public boolean hasState(final Object component, final String componentName, final Class<?> aClass) throws StateStorageException {
    final URL url = DecodeDefaultsUtil.getDefaults(component, componentName);
    return url != null;
  }

  @NotNull
  public ExternalizationSession startExternalization() {
    throw new UnsupportedOperationException("Method startExternalization not implemented in " + getClass());
  }

  @NotNull
  public SaveSession startSave(ExternalizationSession externalizationSession) {
    throw new UnsupportedOperationException("Method startSave not implemented in " + getClass());
  }

  public void finishSave(SaveSession saveSession) {
    throw new UnsupportedOperationException("Method finishSave not implemented in " + getClass());
  }

  public void reload(final Set<String> changedComponents) throws StateStorageException {
    throw new UnsupportedOperationException("Method reload not implemented in " + getClass());
  }

}