package org.nickhill111;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.nickhill111.util.DialogUtils.checkValidFileName;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.data.Personalisation;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.gui.MenuBar;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class TestingEvidenceApplication {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final Config config = Config.getInstance();

    public TestingEvidenceApplication() {
        new TestingEvidenceApplication(null);
    }
    public TestingEvidenceApplication(File folderToOpenFrom) {
        setLookAndFeel();

        JFrame gui = new JFrame();
        frameComponents.setFrame(gui);
        frameComponents.setActiveFolder(folderToOpenFrom);

        if (!frameComponents.isActiveFolder()) {
            String ticketNumber = checkValidFileName(DialogUtils.askForTicketNumber());
            if (isNull(ticketNumber)) {
                System.exit(0);
            }
        } else {
            gui.setTitle(folderToOpenFrom.getName());
        }

        gui.setJMenuBar(new MenuBar());

        MainPanel mainPanel = new MainPanel();

        gui.add(mainPanel);

        setupKeyboardHook();
        GuiUtils.setupGui(gui);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);

        config.saveConfig();
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
                    new ScreenshotService().takeAndAddScreenshot();
                }
            }
        };
    }

    public static void main(String[] args) {
        new TestingEvidenceApplication();
    }
}