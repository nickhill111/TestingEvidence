package org.nickhill111.gui;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.data.FrameConfigDetails;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import static java.util.Objects.nonNull;

public class MainFrame extends JFrame implements ComponentListener {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final Config config = Config.getInstance();
    private final ScreenshotService screenshotService = new ScreenshotService();
    public MainFrame(GraphicsConfiguration screen, File folderToOpenFrom) {
        super(screen);
        if (nonNull(screen)) {
            setLocation(screen.getBounds().getLocation());
        }

        addComponentListener(this);

        frameComponents.setFrame(this);
        if (nonNull(folderToOpenFrom)) {
            config.getConfigDetails().setOpenedFolderPath(folderToOpenFrom.getAbsolutePath());
        }

        setJMenuBar(new MenuBar());

        MainPanel mainPanel = new MainPanel();

        add(mainPanel);

        setupKeyboardHook();

        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getFrameConfigDetails();
        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            setSize(windowSize);
            setExtendedState(frameConfigDetails.getWindowState());
        }

        GuiUtils.setupGui(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        config.saveConfig();

        if (nonNull(folderToOpenFrom) && folderToOpenFrom.exists()) {
            setTitle(folderToOpenFrom.getName());
        } else {
            setTitle("Testing Evidence");
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
}
