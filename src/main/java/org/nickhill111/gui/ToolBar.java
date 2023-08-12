package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import org.nickhill111.data.Settings;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;

public class ToolBar extends JToolBar {
    private final Settings settings = Settings.getInstance();
    private final SavingService savingService = new SavingService();
    private JComboBox<GraphicsDevice> screenSelection;

    ScreenshotService screenshotService = new ScreenshotService();

    public ToolBar() {
        add(createScreenSelection());
        add(createScreenshotButton());
        add(createUserButton());
        add(createAddAcButton());
        add(createSaveButton());

        settings.setToolBar(this);
    }

    private JButton createScreenshotButton() {
        JButton screenshotButton = new JButton("Take Screenshot");
        screenshotButton.addActionListener(e -> {
            screenshotService.takeAndAddScreenshot();
        });

        return screenshotButton;
    }

    private JComboBox<GraphicsDevice> createScreenSelection() {
        screenSelection = new JComboBox<>();
        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice screen : screens) {
            screenSelection.addItem(screen);
        }
        screenSelection.setSelectedIndex(0);

        return screenSelection;
    }

    private JButton createUserButton() {
        JButton addAcButton = new JButton("Add User");
        addAcButton.addActionListener(e -> {
            settings.getUserTabbedPane().addEmptyTab();
        });

        return addAcButton;
    }

    private JButton createAddAcButton() {
        JButton addAcButton = new JButton("Add AC");
        addAcButton.addActionListener(e -> {
            AcTabbedPane acTabbedPane = settings.getUserTabbedPane().getSelectedAcTabbedPane();
            acTabbedPane.addEmptyTab();
        });

        return addAcButton;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            savingService.saveAllScreenshots();
        });

        return saveButton;
    }

    public GraphicsDevice getSelectedScreen() {
        return (GraphicsDevice) screenSelection.getSelectedItem();
    }
}
