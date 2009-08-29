package com.intellij.application.options.colors;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.options.newEditor.OptionsEditor;
import com.intellij.ide.DataManager;
import com.intellij.ide.util.scopeChooser.ScopeChooserConfigurable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ScopeColorsPageFactory implements ColorAndFontPanelFactory {
  public NewColorAndFontPanel createPanel(ColorAndFontOptions options) {
    final JPanel scopePanel = createChooseScopePanel();
    return NewColorAndFontPanel.create(new PreviewPanel.Empty(){
      public Component getPanel() {
        return scopePanel;
      }

    }, ColorAndFontOptions.SCOPES_GROUP, options, null, null);
  }

  public String getPanelDisplayName() {
    return ColorAndFontOptions.SCOPES_GROUP;
  }

  private static JPanel createChooseScopePanel() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    JPanel panel = new JPanel(new GridBagLayout());
    //panel.setBorder(new LineBorder(Color.red));
    if (projects.length == 0) return panel;
    GridBagConstraints gc = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                                                   new Insets(0, 0, 0, 0), 0, 0);
    final Project contextProject = PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    final Project project = contextProject != null ? contextProject : projects[0];

    JButton button = new JButton(ApplicationBundle.message("button.edit.scopes"));
    button.setPreferredSize(new Dimension(230, button.getPreferredSize().height));
    panel.add(button, gc);
    gc.gridx = GridBagConstraints.REMAINDER;
    gc.weightx = 1;
    panel.add(new JPanel(), gc);

    gc.gridy++;
    gc.gridx=0;
    gc.weighty = 1;
    panel.add(new JPanel(), gc);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final OptionsEditor optionsEditor = OptionsEditor.KEY.getData(DataManager.getInstance().getDataContext());
        if (optionsEditor != null) {
          optionsEditor.select(ScopeChooserConfigurable.getInstance(project));
        }
      }
    });
    return panel;
  }
}