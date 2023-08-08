package org.example.service;

import static java.util.Objects.nonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.example.data.Settings;
import org.example.gui.MainPanel;
import org.example.gui.ScrollPane;
import org.example.util.DialogUtils;
import org.example.util.GuiUtils;

public class ScreenshotService {
    Settings settings = Settings.getInstance();

    public void takeAndAddScreenshot() {
        BufferedImage screenshot = takeScreenshot();

        if (nonNull(screenshot)) {
            addScreenshot(screenshot);
        }
    }


    private BufferedImage takeScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle rectangle = settings.getToolBar().getSelectedScreen().getDefaultConfiguration().getBounds();

            return robot.createScreenCapture(rectangle);
        } catch (Exception ex) {
            DialogUtils.cantTakeScreenshotDialog(ex);
        }

        return null;
    }

    private void addScreenshot(BufferedImage screenshot) {
        ScrollPane scrollPane = (ScrollPane) settings.getUserTabbedPane().getSelectedAcTabbedPane().getSelectedComponent();
        scrollPane.getPreviewPanel().addEvidence(screenshot);

        MainPanel mainPanel = settings.getMainPanel();
        GuiUtils.refreshComponent(mainPanel);
    }

    public void saveScreenshot(BufferedImage image, File filePath) throws IOException {
        ImageIO.write(image, "png", new File(filePath + ".png"));
    }
}
