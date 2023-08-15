package org.nickhill111.data;

import java.awt.*;
import java.io.File;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.gui.ToolBar;
import org.nickhill111.gui.UserTabbedPane;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
public class Settings {
    private static Settings INSTANCE;

    private Frame frame;
    private MainPanel mainPanel;
    private ToolBar toolBar;
    private UserTabbedPane userTabbedPane;
    private TextArea generatedTextArea;

    private File activeFolder;
    private GlobalKeyboardHook keyboardHook;

    public static Settings getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Settings();
        }

        return INSTANCE;
    }

    public boolean isActivefolder() {
        return nonNull(activeFolder) && activeFolder.exists() && activeFolder.isDirectory();
    }
}
