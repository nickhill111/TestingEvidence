package org.example;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.example.data.Settings;
import org.example.gui.MainPanel;
import org.example.gui.MenuBar;
import org.example.service.ScreenshotService;
import org.example.util.DialogUtils;

public class TestingEvidenceApplication {
    private final Settings settings = Settings.getInstance();
    private final Dimension GUI_SIZE = new Dimension(700, 500);

    public TestingEvidenceApplication() {
        new TestingEvidenceApplication(null);
    }
    public TestingEvidenceApplication(File folderToOpenFrom) {
        setLookAndFeel();

        JFrame gui = new JFrame();
        settings.setFrame(gui);
        settings.setActiveFolder(folderToOpenFrom);

        if (!settings.isActivefolder()) {
            String ticketNumber = DialogUtils.askForTicketNumber();
            if (isNull(ticketNumber) || ticketNumber.isEmpty()) {
                System.exit(0);
            }
        } else {
            gui.setTitle(folderToOpenFrom.getName());
        }

        gui.setJMenuBar(new MenuBar());

        MainPanel mainPanel = new MainPanel();

        gui.add(mainPanel);

        setupKeyboardHook();
        setupGui(gui);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyboardHook() {
        GlobalKeyboardHook keyboardHook = settings.getKeyboardHook();
        if (nonNull(keyboardHook)) {
            settings.getKeyboardHook().shutdownHook();
        }

        keyboardHook = new GlobalKeyboardHook(false);

        keyboardHook.addKeyListener(createKeyListener());

        settings.setKeyboardHook(keyboardHook);
    }

    private GlobalKeyAdapter createKeyListener() {
        return new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                Frame gui = Settings.getInstance().getFrame();

                if (!gui.isFocused() && event.getVirtualKeyCode() == GlobalKeyEvent.VK_F8) {
                    new ScreenshotService().takeAndAddScreenshot();
                }
            }
        };
    }

    private void setupGui(JFrame gui) {
        gui.setSize(GUI_SIZE);
        gui.setLocationRelativeTo(null);
        gui.setResizable(true);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new TestingEvidenceApplication();
    }
}