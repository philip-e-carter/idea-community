/*
 * Copyright (c) 2004 JetBrains s.r.o. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 *
 * Neither the name of JetBrains or IntelliJ IDEA
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. JETBRAINS AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL JETBRAINS OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF JETBRAINS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.intellij.openapi.module.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ModuleTypeManagerImpl extends ModuleTypeManager {
  private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.module.impl.ModuleTypeManagerImpl");

  private final LinkedHashMap<ModuleType, Boolean> myModuleTypes = new LinkedHashMap<ModuleType, Boolean>();


  public ModuleTypeManagerImpl() {
    registerModuleType(getDefaultModuleType(), true);
  }

  public void registerModuleType(ModuleType type) {
    registerModuleType(type, false);
  }

  public void registerModuleType(ModuleType type, boolean classpathProvider) {
    for (ModuleType oldType : myModuleTypes.keySet()) {
      if (oldType.getId().equals(type.getId())) {
        LOG.error("Trying to register a module type that claunches with existing one. Old=" + oldType + ", new = " + type);
        return;
      }
    }
    myModuleTypes.put(type, classpathProvider);
  }

  public ModuleType[] getRegisteredTypes() {
    List<ModuleType> result = new ArrayList<ModuleType>();
    result.addAll(myModuleTypes.keySet());
    for(ModuleTypeEP moduleTypeEP: Extensions.getExtensions(ModuleTypeEP.EP_NAME)) {
      result.add(moduleTypeEP.getModuleType());
    }

    return result.toArray(new ModuleType[result.size()]);
  }

  public ModuleType findByID(String moduleTypeID) {
    if (moduleTypeID == null) return getDefaultModuleType();
    for (ModuleType type : myModuleTypes.keySet()) {
      if (type.getId().equals(moduleTypeID)) {
        return type;
      }
    }
    for(ModuleTypeEP ep: Extensions.getExtensions(ModuleTypeEP.EP_NAME)) {
      if (ep.id.equals(moduleTypeID)) {
        return ep.getModuleType();
      }
    }


    return new UnknownModuleType(moduleTypeID, getDefaultModuleType());
  }

  public boolean isClasspathProvider(final ModuleType moduleType) {
    for(ModuleTypeEP ep: Extensions.getExtensions(ModuleTypeEP.EP_NAME)) {
      if (ep.id.equals(moduleType.getId())) {
        return ep.classpathProvider;
      }
    }

    final Boolean provider = myModuleTypes.get(moduleType);
    return provider != null && provider.booleanValue();
  }

  public ModuleType getDefaultModuleType() {
    return EmptyModuleType.getInstance();
  }
}