package org.example.data;

import java.awt.*;
import java.io.File;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lombok.Getter;
import lombok.Setter;
import org.example.gui.MainPanel;
import org.example.gui.ToolBar;
import org.example.gui.UserTabbedPane;

import static java.util.Objects.nonNull;

@Getter
@Setter
public class Settings {
    private static Settings INSTANCE;

    private Frame frame;
    private MainPanel mainPanel;
    private ToolBar toolBar;
    private UserTabbedPane userTabbedPane;
    private TextArea generatedTextArea;

    private File activeFolder;
    private GlobalKeyboardHook keyboardHook;

    private Settings() {
    }

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
