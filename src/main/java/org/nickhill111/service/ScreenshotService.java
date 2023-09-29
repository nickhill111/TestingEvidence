package org.nickhill111.service;

import static java.util.Objects.nonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.ScenarioPanel;
import org.nickhill111.gui.Scenarios;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class ScreenshotService {
    FrameComponents frameComponents = FrameComponents.getInstance();

    public void takeAndAddScreenshot() {
        BufferedImage screenshot = takeScreenshot();

        if (nonNull(screenshot)) {
            Scenarios scenarios = frameComponents.getUsers().getSelectedScenarios();

            if (nonNull(scenarios)) {
                addScreenshot(screenshot, scenarios.getSelectedScenario());
            } else {
                DialogUtils.cantTakeScreenshotDialog(new Exception("No tab selected to put the screenshot in!"));
            }
        }
    }


    private BufferedImage takeScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle rectangle = frameComponents.getToolBar().getSelectedScreen().getDefaultConfiguration().getBounds();

            return robot.createScreenCapture(rectangle);
        } catch (Exception ex) {
            DialogUtils.cantTakeScreenshotDialog(ex);
        }

        return null;
    }

    private void addScreenshot(BufferedImage screenshot, ScenarioPanel scenarioPanel) {
        scenarioPanel.addEvidence(screenshot);

        MainPanel mainPanel = frameComponents.getMainPanel();
        GuiUtils.refreshComponent(mainPanel);
    }

    public void saveScreenshot(BufferedImage image, File filePath) throws IOException {
        ImageIO.write(image, "png", filePath);
    }
}
