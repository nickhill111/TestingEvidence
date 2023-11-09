package org.nickhill111.common.util;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.common.data.Personalisation;
import org.nickhill111.testManager.gui.PreviewEvidenceFrame;
import org.nickhill111.testManager.gui.Scenarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isEmpty;

public class GuiUtils {
    private static final Dimension DEFAULT_GUI_SIZE = new Dimension(700, 500);
    public static final int PHOTO_SIZE = 400;
    public static final Font TITLE_FONT = getFont(60);
    public static final Font MEDIUM_FONT = getFont(24);
    public static final Font SMALL_FONT = getFont(14);

    private static final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
    private static final Config config = Config.getInstance();

    public static void setLookAndFeel() {
        FlatLightLaf.installLafInfo();
        FlatDarkLaf.installLafInfo();
        FlatIntelliJLaf.installLafInfo();
        FlatDarculaLaf.installLafInfo();
        FlatMacLightLaf.installLafInfo();
        FlatMacDarkLaf.installLafInfo();

        try {
            Personalisation personalisation = config.getConfigDetails().getGlobalConfigDetails().getPersonalisation();
            if (isEmpty(personalisation.getLookAndFeel())) {
                personalisation.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

            UIManager.setLookAndFeel(personalisation.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        Scenarios selectedScenarios = testManagerComponents.getUsers().getSelectedScenarios();
        if (nonNull(selectedScenarios)) {
            selectedScenarios.addEmptyTab();
        } else {
            DialogUtils.cantTakeScreenshotDialog(new Exception("No users created!"));
        }
    }

    public static void previewSelectedScenario() {
        new PreviewEvidenceFrame(testManagerComponents.getUsers().getSelectedScenarios().getSelectedScenario().getPhotoAt(0),
            GuiUtils.getScreen(config.getConfigDetails().getTestManagerConfigDetails().getPreviewFrameConfigDetails().getWindowScreenId()));
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
