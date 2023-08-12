package org.nickhill111.service;

import static java.util.Objects.nonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.nickhill111.data.Settings;
import org.nickhill111.gui.MainPanel;
import org.nickhill111.gui.ScrollPane;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

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
