package org.nickhill111;

import static java.util.Objects.nonNull;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.apache.logging.log4j.util.Strings.isEmpty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.data.FrameConfigDetails;
import org.nickhill111.data.Personalisation;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.gui.MenuBar;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.GuiUtils;

public class TestingEvidenceApplication {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final Config config = Config.getInstance();
    private final ScreenshotService screenshotService = new ScreenshotService();

    public TestingEvidenceApplication() {
        String openedFolderPath = config.getConfigDetails().getOpenedFolderPath();

        if (nonNull(openedFolderPath)) {
            new TestingEvidenceApplication(new File(openedFolderPath));
            return;
        }

        new TestingEvidenceApplication(null);
    }
    public TestingEvidenceApplication(File folderToOpenFrom) {
        setLookAndFeel();

        JFrame gui = new JFrame();

        GraphicsConfiguration screen = GuiUtils.getScreen(config.getConfigDetails().getFrameConfigDetails().getWindowScreenId());
        if (nonNull(screen)) {
            gui = new JFrame(screen);
            gui.setLocation(screen.getBounds().getLocation());
        }

        frameComponents.setFrame(gui);
        frameComponents.setActiveFolder(folderToOpenFrom);

        gui.setJMenuBar(new MenuBar());

        MainPanel mainPanel = new MainPanel();

        gui.add(mainPanel);

        setupKeyboardHook();

        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getFrameConfigDetails();
        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            gui.setSize(windowSize);
            gui.setExtendedState(frameConfigDetails.getWindowState());
        }

        GuiUtils.setupGui(gui);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);

        gui.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                config.saveFrameConfigDetails();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                config.saveFrameConfigDetails();
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        config.saveConfig();

        if (!frameComponents.isActiveFolder()) {
            gui.setTitle("Testing Evidence");
        } else {
            gui.setTitle(folderToOpenFrom.getName());
        }
    }

    private void setLookAndFeel() {
        try {
            Personalisation personalisation = config.getConfigDetails().getPersonalisation();
            if (isEmpty(personalisation.getLookAndFeel())) {
                personalisation.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

            UIManager.setLookAndFeel(personalisation.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyboardHook() {
        GlobalKeyboardHook keyboardHook = frameComponents.getKeyboardHook();
        if (nonNull(keyboardHook)) {
            frameComponents.getKeyboardHook().shutdownHook();
        }

        keyboardHook = new GlobalKeyboardHook(false);

        keyboardHook.addKeyListener(createKeyListener());

        frameComponents.setKeyboardHook(keyboardHook);
    }

    private GlobalKeyAdapter createKeyListener() {
        return new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                Frame gui = FrameComponents.getInstance().getFrame();

                if (!gui.isFocused() && event.getVirtualKeyCode() == GlobalKeyEvent.VK_F8) {
                    screenshotService.takeAndAddScreenshot();
                }
            }
        };
    }

    public static void main(String[] args) {
        new TestingEvidenceApplication();
    }
}