package ro.redeul.google.go.ide;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import ro.redeul.google.go.options.GoSettings;
import ro.redeul.google.go.sdk.GoSdkUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Author: Florin Patan <florinpatan@gmail.com>
 */
public class GoGlobalConfigurableForm {
   public JPanel componentPanel;
    private TextFieldWithBrowseButton goPath;
    private JButton importSysGoPath;
    private JCheckBox enableOnTheFlyImportOptimization;
    private final GoGlobalSettings goGlobalSettings = GoGlobalSettings.getInstance();
    private final GoSettings goSettings = GoSettings.getInstance();

    GoGlobalConfigurableForm() {
        goPath.addBrowseFolderListener("GOPATH directory", "Select the GOPATH directory of your GO setup",
                null, new FileChooserDescriptor(false, true, false, false, false, false));

        importSysGoPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goPath.setText(GoSdkUtil.getSysGoPathPath().split(File.pathSeparator)[0]);
            }
        });
    }

    public void reset() {
        goPath.setText(goGlobalSettings.getGoPath());
        enableOnTheFlyImportOptimization.setSelected(goSettings.getState().OPTIMIZE_IMPORTS_ON_THE_FLY);
    }

    public boolean isModified() {
        if (enableOnTheFlyImportOptimization.isSelected() != goSettings.getState().OPTIMIZE_IMPORTS_ON_THE_FLY) {
            return true;
        }
        return !Comparing.equal(goPath.getText(), goGlobalSettings.getGoPath());
    }

    public void apply() {
        String goPathStr = goPath.getText();

        if (goPathStr.endsWith(File.separator)) {
            goPathStr = goPathStr.substring(0, goPathStr.length() - 1);
            goPath.setText(goPathStr);
        }

        if (!(new File(goPathStr).exists())) {
            Messages.showErrorDialog("Error while saving your settings. \nGOPATH doesn't exists.", "Error on Google Go Plugin");
            return;
        }

        /* The error check of a bin folder is not necessary because bin folder is created automatically and the
         * error seems to confuse more then it helps because GOROOT also has a bin folder (i.e. https://github.com/go-lang-plugin-org/go-lang-idea-plugin/issues/752)
        if (!(new File(goPathStr.concat("/bin")).exists())) {
            Messages.showErrorDialog("Error while saving your settings. \nGOPATH/bin doesn't exists.", "Error on Google Go Plugin");
            return;
        }
        */

        if (!(new File(goPathStr.concat("/src")).exists())) {
            Messages.showErrorDialog("Error while saving your settings. \nGOPATH/src doesn't exists.", "Error on Google Go Plugin");
            return;
        }

        goGlobalSettings.setGoPath(goPathStr);
        goSettings.OPTIMIZE_IMPORTS_ON_THE_FLY = enableOnTheFlyImportOptimization.isSelected();
    }
}
