package org.nickhill111.util;

import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.Scenarios;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.nonNull;

public class GuiUtils {
    private static final Dimension GUI_SIZE = new Dimension(700, 500);
    public static final int PHOTO_SIZE = 300;

    public static void refreshComponent(Component component) {
        component.revalidate();
        component.repaint();
    }

    public static void setupGui(JFrame gui) {
        if (new Dimension(0, 0).equals(gui.getSize())) {
            gui.setSize(GUI_SIZE);
        }

        gui.setLocationRelativeTo(null);
        gui.setResizable(true);
        gui.setVisible(true);
    }

    public static void addNewScenarioTab() {
        Scenarios selectedScenarios = FrameComponents.getInstance().getUsers().getSelectedScenarios();
        if (nonNull(selectedScenarios)) {
            selectedScenarios.addEmptyTab();
        } else {
            DialogUtils.cantTakeScreenshotDialog(new Exception("No users created!"));
        }
    }
}
