package de.mprengemann.intellij.plugin.androidicons.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * User: marcprengemann
 * Date: 04.04.14
 * Time: 10:32
 */
public class AndroidIconsSettings implements Configurable {
  private JComponent                                        mComponent;
  private JPanel                                            mPanel;
  private com.intellij.openapi.ui.TextFieldWithBrowseButton textFieldHome;

  private VirtualFile selectedFile;
  private String      persistedFile;
  private boolean selectionPerformed = false;

  @Nullable
  @Override
  public JComponent createComponent() {
    persistedFile = SettingsHelper.getAssetPathString();
    VirtualFile loadedFile = VirtualFileManager.getInstance().findFileByUrl(persistedFile);
    if (loadedFile != null) {
      textFieldHome.setText(loadedFile.getCanonicalPath());
      selectedFile = loadedFile;
    }

    FileChooserDescriptor workingDirectoryChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
    String title = "Select res directory";
    workingDirectoryChooserDescriptor.setTitle(title);
    textFieldHome.addBrowseFolderListener(title, null, null, workingDirectoryChooserDescriptor);
    textFieldHome.addBrowseFolderListener(new TextBrowseFolderListener(workingDirectoryChooserDescriptor) {
      @Override
      protected void onFileChoosen(@NotNull VirtualFile chosenFile) {
        super.onFileChoosen(chosenFile);
        selectionPerformed = true;
        selectedFile = chosenFile;
      }
    });

    mComponent = mPanel;
    return mComponent;
  }

  @Override
  public boolean isModified() {
    boolean isModified = false;

    if (selectionPerformed) {
      if (!persistedFile.equalsIgnoreCase(selectedFile.getUrl())) {
        isModified = true;
      }
    } else if (!TextUtils.isEmpty(persistedFile) && selectedFile == null) {
      isModified = true;
    }

    return isModified;
  }

  @Override
  public void apply() throws ConfigurationException {
    SettingsHelper.saveAssetPath(selectedFile);
    if (selectedFile != null) {
      persistedFile = selectedFile.getUrl();
      selectionPerformed = false;
    }
  }

  @Override
  public void reset() {
  }

  @Override
  public void disposeUIResources() {
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Android Icons";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }
}