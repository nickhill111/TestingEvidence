package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;

import static org.nickhill111.util.GuiUtils.addNewScenarioTab;

public class ToolBar extends JToolBar {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final SavingService savingService = new SavingService();
    private JComboBox<GraphicsDevice> screenSelection;

    ScreenshotService screenshotService = new ScreenshotService();

    public ToolBar() {
        add(createScreenSelection());
        add(createScreenshotButton());
        add(createUserButton());
        add(createAddScenarioButton());
        add(createSaveButton());

        frameComponents.setToolBar(this);
    }

    private JButton createScreenshotButton() {
        JButton screenshotButton = new JButton("Take Screenshot");
        screenshotButton.addActionListener(e -> screenshotService.takeAndAddScreenshot());

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
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> frameComponents.getUsers().addEmptyTab());

        return addUserButton;
    }

    private JButton createAddScenarioButton() {
        JButton addScenarioButton = new JButton("Add Scenario");
        addScenarioButton.addActionListener(e -> addNewScenarioTab());

        return addScenarioButton;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> savingService.saveAllScreenshots());

        return saveButton;
    }

    public GraphicsDevice getSelectedScreen() {
        return (GraphicsDevice) screenSelection.getSelectedItem();
    }
}
