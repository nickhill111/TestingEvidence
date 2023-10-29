package org.nickhill111.testManager.data;

import java.awt.*;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nickhill111.testManager.gui.MainPanel;
import org.nickhill111.testManager.gui.TestingToolBar;
import org.nickhill111.testManager.gui.Users;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
public class TestManagerComponents {
    private static TestManagerComponents INSTANCE;

    private JFrame frame;
    private MainPanel mainPanel;
    private TestingToolBar testingToolBar;
    private Users users;
    private TextArea generatedTextArea;

    private GlobalKeyboardHook keyboardHook;

    public static TestManagerComponents getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestManagerComponents();
        }

        return INSTANCE;
    }
}
