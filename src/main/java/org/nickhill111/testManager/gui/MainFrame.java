package org.nickhill111.testManager.gui;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.common.data.FrameConfigDetails;
import org.nickhill111.testManager.service.ScreenshotService;
import org.nickhill111.common.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import static java.util.Objects.nonNull;

public class MainFrame extends JFrame implements ComponentListener {
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
    private final Config config = Config.getInstance();
    private final ScreenshotService screenshotService = new ScreenshotService();
    public MainFrame(GraphicsConfiguration screen, File folderToOpenFrom) {
        super(screen);
        if (nonNull(screen)) {
            setLocation(screen.getBounds().getLocation());
        }

        addComponentListener(this);

        testManagerComponents.setFrame(this);
        if (nonNull(folderToOpenFrom)) {
            config.getConfigDetails().getTestManagerConfigDetails().setOpenedFolderPath(folderToOpenFrom.getAbsolutePath());
        }

        setJMenuBar(new MenuBar());

        MainPanel mainPanel = new MainPanel();

        add(mainPanel);

        setupKeyboardHook();

        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getTestManagerConfigDetails().getFrameConfigDetails();
        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            setSize(windowSize);
            setExtendedState(frameConfigDetails.getWindowState());
        }

        GuiUtils.setupGui(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        config.saveConfigDetails();

        if (nonNull(folderToOpenFrom) && folderToOpenFrom.exists()) {
            setTitle(folderToOpenFrom.getName());
        } else {
            setTitle("Testing Evidence");
        }
    }

    private void setupKeyboardHook() {
        GlobalKeyboardHook keyboardHook = testManagerComponents.getKeyboardHook();
        if (nonNull(keyboardHook)) {
            testManagerComponents.getKeyboardHook().shutdownHook();
        }

        keyboardHook = new GlobalKeyboardHook(false);

        keyboardHook.addKeyListener(createKeyListener());

        testManagerComponents.setKeyboardHook(keyboardHook);
    }

    private GlobalKeyAdapter createKeyListener() {
        return new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                Frame gui = TestManagerComponents.getInstance().getFrame();

                if (!gui.isFocused() && event.getVirtualKeyCode() == GlobalKeyEvent.VK_F8) {
                    screenshotService.takeAndAddScreenshot();
                }
            }
        };
    }

    @Override
    public void componentResized(ComponentEvent e) {
        config.saveTestManagerFrameConfigDetails();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        config.saveTestManagerFrameConfigDetails();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
