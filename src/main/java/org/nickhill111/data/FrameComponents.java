package org.nickhill111.data;

import java.awt.*;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.gui.ToolBar;
import org.nickhill111.gui.Users;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
public class FrameComponents {
    private static FrameComponents INSTANCE;

    private JFrame frame;
    private MainPanel mainPanel;
    private ToolBar toolBar;
    private Users users;
    private TextArea generatedTextArea;

    private GlobalKeyboardHook keyboardHook;

    public static FrameComponents getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FrameComponents();
        }

        return INSTANCE;
    }
}
