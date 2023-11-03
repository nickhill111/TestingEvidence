package org.nickhill111.testManager.gui;

import javax.swing.*;
import java.awt.*;

import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.service.SavingService;
import org.nickhill111.testManager.service.ScreenshotService;

import static java.util.Objects.nonNull;
import static org.nickhill111.common.data.Icons.ADD_SCENARIO_ICON;
import static org.nickhill111.common.data.Icons.ADD_USER_ICON;
import static org.nickhill111.common.data.Icons.SAVE_ICON;
import static org.nickhill111.common.data.Icons.SCREENSHOT_ICON;
import static org.nickhill111.common.util.GuiUtils.MEDIUM_FONT;
import static org.nickhill111.common.util.GuiUtils.addNewScenarioTab;

public class TestingToolBar extends JToolBar {
    private final Config config = Config.getInstance();
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
    private final SavingService savingService = new SavingService();
    private final ScreenshotService screenshotService = new ScreenshotService();
    private JComboBox<GraphicsDevice> screenSelection;

    public TestingToolBar() {
        addScreenshotButton();
        addUserButton();
        addAddScenarioButton();
        addSaveButton();
        addSeparator();
        addScreenLabel();
        addScreenSelection();

        testManagerComponents.setTestingToolBar(this);
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
        screenSelection.setFont(MEDIUM_FONT);

        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        String selectedScreenId = config.getConfigDetails().getTestManagerConfigDetails().getSelectedScreenId();
        for (GraphicsDevice screen : screens) {
            screenSelection.addItem(screen);

            if (nonNull(selectedScreenId) && selectedScreenId.equals(screen.getIDstring())) {
                screenSelection.setSelectedItem(screen);
            }
        }

        screenSelection.addActionListener(e -> {
            GraphicsDevice graphicsDevice = (GraphicsDevice) screenSelection.getSelectedItem();
            if (nonNull(graphicsDevice)) {
                config.getConfigDetails().getTestManagerConfigDetails().setSelectedScreenId(graphicsDevice.getIDstring());
                config.saveConfigDetails();
            }
        });

        add(screenSelection);
    }

    private void addUserButton() {
        JButton addUserButton = new JButton(ADD_USER_ICON);
        addUserButton.setToolTipText("Add User");
        addUserButton.addActionListener(e -> testManagerComponents.getUsers().addEmptyTab());

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
