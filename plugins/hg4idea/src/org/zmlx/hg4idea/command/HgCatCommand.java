// Copyright 2008-2010 Victor Iacoban
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed under
// the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language governing permissions and
// limitations under the License.
package org.zmlx.hg4idea.command;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.zmlx.hg4idea.HgFile;
import org.zmlx.hg4idea.HgRevisionNumber;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HgCatCommand {

  private final Project myProject;

  public HgCatCommand(Project project) {
    myProject = project;
  }

  @Nullable
  public String execute(HgFile hgFile, HgRevisionNumber vcsRevisionNumber, Charset charset) {
    final List<String> arguments = createArguments(vcsRevisionNumber, hgFile.getRelativePath());
    final HgCommandService service = HgCommandService.getInstance(myProject);
    final HgCommandResult result = service.execute(hgFile.getRepo(), Collections.<String>emptyList(), "cat", arguments, charset);

    if (result == null) { // in case of error
      return null;
    }
    if (result.getExitValue() == 1) { // file not found in given revision
      return getContentFollowingRenames(hgFile, vcsRevisionNumber, charset, service);
    }
    return result.getRawOutput();
  }

  @Nullable
  private String getContentFollowingRenames(HgFile hgFile, HgRevisionNumber vcsRevisionNumber, Charset charset, HgCommandService service) {
    final String renamedHgFile = new HgTrackFileNamesAccrossRevisionsCommand(myProject)
      .execute(hgFile, getCurrentRevision(hgFile), vcsRevisionNumber.getRevision(), -1);
    if (renamedHgFile == null) {
      return null;
    }
    final HgCommandResult result = service.execute(hgFile.getRepo(), Collections.<String>emptyList(), "cat",
                                                   createArguments(vcsRevisionNumber, renamedHgFile), charset);
    return result != null ? result.getRawOutput() : null;
  }

  private String getCurrentRevision(HgFile hgFile) {
    HgParentsCommand parentsCommand = new HgParentsCommand(myProject);
    List<HgRevisionNumber> parents = parentsCommand.execute(hgFile.getRepo());

    String currentRevision = "0";

    if (parents.size() == 1) {
      currentRevision = parents.get(0).getRevision();
    } else if (parents.size() > 1) {
      long maxParentNumber = Long.MIN_VALUE;

      for (HgRevisionNumber revisionNumber : parents) {
        long revisionAsLong = revisionNumber.getRevisionAsLong();

        if (revisionAsLong > maxParentNumber) {
          maxParentNumber = revisionAsLong;
          currentRevision = revisionNumber.getRevision();
        }
      }
    }

    return currentRevision;
  }

  private static List<String> createArguments(HgRevisionNumber vcsRevisionNumber, String fileName) {
    final List<String> arguments = new LinkedList<String>();
    if (vcsRevisionNumber != null) {
      arguments.add("--rev");
      if (StringUtils.isNotBlank(vcsRevisionNumber.getChangeset())) {
        arguments.add(vcsRevisionNumber.getChangeset());
      } else {
        arguments.add(vcsRevisionNumber.getRevision());
      }
    }
    arguments.add(fileName);
    return arguments;
  }

}
