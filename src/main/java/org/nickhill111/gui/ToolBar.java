package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;

import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;

import static java.util.Objects.nonNull;
import static org.nickhill111.data.Icons.ADD_SCENARIO_ICON;
import static org.nickhill111.data.Icons.ADD_USER_ICON;
import static org.nickhill111.data.Icons.SAVE_ICON;
import static org.nickhill111.data.Icons.SCREENSHOT_ICON;
import static org.nickhill111.util.GuiUtils.MEDIUM_FONT;
import static org.nickhill111.util.GuiUtils.addNewScenarioTab;

public class ToolBar extends JToolBar {
    private final Config config = Config.getInstance();
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final SavingService savingService = new SavingService();
    private final ScreenshotService screenshotService = new ScreenshotService();
    private JComboBox<GraphicsDevice> screenSelection;

    public ToolBar() {
        addScreenshotButton();
        addUserButton();
        addAddScenarioButton();
        addSaveButton();
        addSeparator();
        addScreenLabel();
        addScreenSelection();

        frameComponents.setToolBar(this);
    }

    private void addScreenLabel() {
        JLabel screenLabel = new JLabel("Selected Screen:  ");
        screenLabel.setFont(MEDIUM_FONT);
        add(screenLabel);
    }

    private void addScreenshotButton() {
        JButton screenshotButton = new JButton(SCREENSHOT_ICON);
        screenshotButton.setToolTipText("Take Screenshot");
        screenshotButton.addActionListener(e -> screenshotService.takeAndAddScreenshot());

        add(screenshotButton);
    }

    private void addScreenSelection() {
        screenSelection = new JComboBox<>();
        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        String selectedScreenId = config.getConfigDetails().getSelectedScreenId();
        for (GraphicsDevice screen : screens) {
            screenSelection.addItem(screen);

            if (nonNull(selectedScreenId) && selectedScreenId.equals(screen.getIDstring())) {
                screenSelection.setSelectedItem(screen);
            }
        }

        screenSelection.addActionListener(e -> {
            GraphicsDevice graphicsDevice = (GraphicsDevice) screenSelection.getSelectedItem();
            if (nonNull(graphicsDevice)) {
                config.getConfigDetails().setSelectedScreenId(graphicsDevice.getIDstring());
                config.saveConfig();
            }
        });

        add(screenSelection);
    }

    private void addUserButton() {
        JButton addUserButton = new JButton(ADD_USER_ICON);
        addUserButton.setToolTipText("Add User");
        addUserButton.addActionListener(e -> frameComponents.getUsers().addEmptyTab());

        add(addUserButton);
    }

    private void addAddScenarioButton() {
        JButton addScenarioButton = new JButton(ADD_SCENARIO_ICON);
        addScenarioButton.setToolTipText("Add scenario");
        addScenarioButton.addActionListener(e -> addNewScenarioTab());

        add(addScenarioButton);
    }

    private void addSaveButton() {
        JButton saveButton = new JButton(SAVE_ICON);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(e -> savingService.saveAllScreenshots());

        add(saveButton);
    }

    public GraphicsDevice getSelectedScreen() {
        return (GraphicsDevice) screenSelection.getSelectedItem();
    }
}
