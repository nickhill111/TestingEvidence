package org.nickhill111.testManager.service;

import static java.util.Objects.nonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.gui.ScenarioPanel;
import org.nickhill111.testManager.gui.Scenarios;
import org.nickhill111.testManager.gui.MainPanel;
import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.common.util.GuiUtils;

public class ScreenshotService {
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();

    public void takeAndAddScreenshot() {
        BufferedImage screenshot = takeScreenshot();

        if (nonNull(screenshot)) {
            Scenarios scenarios = testManagerComponents.getUsers().getSelectedScenarios();

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
            Rectangle rectangle = testManagerComponents.getTestingToolBar().getSelectedScreen().getDefaultConfiguration().getBounds();

            return robot.createScreenCapture(rectangle);
        } catch (Exception ex) {
            DialogUtils.cantTakeScreenshotDialog(ex);
        }

        return null;
    }

    private void addScreenshot(BufferedImage screenshot, ScenarioPanel scenarioPanel) {
        scenarioPanel.addEvidence(screenshot);

        MainPanel mainPanel = testManagerComponents.getMainPanel();
        GuiUtils.refreshComponent(mainPanel);
    }

    public void saveScreenshot(BufferedImage image, File filePath) throws IOException {
        ImageIO.write(image, "png", filePath);
    }
}
