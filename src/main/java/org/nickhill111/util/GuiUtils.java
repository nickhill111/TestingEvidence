package org.nickhill111.util;

import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.Scenarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;

import static java.util.Objects.nonNull;

public class GuiUtils {
    private static final Dimension DEFAULT_GUI_SIZE = new Dimension(700, 500);
    public static final int PHOTO_SIZE = 400;
    public static final Font TITLE_FONT = getFont(60);
    public static final Font MEDIUM_FONT = getFont(24);
    public static final Font SMALL_FONT = getFont(14);

    public static void refreshComponent(Component component) {
        component.revalidate();
        component.repaint();
    }

    public static void setupGui(JFrame gui) {
        if (new Dimension(0, 0).equals(gui.getSize())) {
            gui.setSize(DEFAULT_GUI_SIZE);
        }

        gui.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                refreshComponent(gui);
            }
        });

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

    public static GraphicsConfiguration getScreen(String windowScreenId) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        if (nonNull(windowScreenId)) {
            for (GraphicsDevice screen : gs) {
                if (windowScreenId.equals(screen.getIDstring())) {
                    return screen.getDefaultConfiguration();
                }
            }
        }

        return null;
    }

    public static Font getFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    public static double convertZoomValueToScale(int zoomValue) {
        return (double) zoomValue/100;
    }
}
