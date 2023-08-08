package org.example.gui;

import static java.util.Objects.nonNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.example.model.RegressionTab;
import org.example.service.ScreenshotService;
import org.example.util.DialogUtils;
import org.example.util.GuiUtils;

public class PreviewPanel extends JPanel {
    private final int acValue;
    private final ScreenshotService screenshotService = new ScreenshotService();

    public PreviewPanel(int acValue) {
        this.acValue = acValue;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public PreviewPanel(int acValue, List<File> filesToAdd) {
        this.acValue = acValue;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        for (File file : filesToAdd) {
            try {
                addEvidence(ImageIO.read(file));
            } catch (IOException e) {
                DialogUtils.unableToOpenImageDialog(file);
            }
        }
    }

    public void addEvidence(BufferedImage screenshot) {
        add(new Photo(screenshot));
        add(Box.createRigidArea(new Dimension(5, 0)));
    }

    private String getTabValue() {
        if (acValue <= 0) {
            return getRegressionTabName();
        }

        return "AC_" + acValue;
    }

    public String saveAllEvidence(String userType, File folder) {
        String tabValue = getTabValue();

        StringBuilder generatedText = new StringBuilder(tabValue + ":\n\n ");

        Component[] components = getComponents();
        for (int i = 0; i < components.length; i = i + 2) {
            Component component = components[i];

            if (component instanceof Photo photo) {
                String fileName = userType + "_" + tabValue + "_" + ((i/2) + 1);
                fileName = verifyFileName(fileName, folder.listFiles());

                generatedText.append("!").append(fileName).append(".png").append("|thumbnail! ");

                if (!savePhoto(photo, new File(folder, fileName))) {
                    return null;
                }
            }
        }

        return generatedText.toString();
    }

    private boolean savePhoto(Photo photo, File file) {
        try {
            screenshotService.saveScreenshot(photo.getOriginalImage(), file);
        } catch (IOException e) {
            DialogUtils.cantSaveDialog(e);
            return false;
        }

        return true;
    }

    private String verifyFileName(String fileName, File[] files) {
        if (nonNull(files)) {
            String oldFileName = Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .filter(file -> file.startsWith(fileName)).findFirst().orElse(null);

            if (nonNull(oldFileName)) {
                if (oldFileName.contains("-")) {
                    String[] splitOldFileName = oldFileName.split("-");

                    String suffix = splitOldFileName[splitOldFileName.length - 1];

                    if (suffix.endsWith(".png")) {
                        suffix = suffix.replace(".png", "");
                        return fileName + "-" + (Integer.parseInt(suffix) + 1);
                    }
                }

                return fileName + "-1";
            }
        }

        return fileName;
    }

    private String getRegressionTabName() {
        return Arrays.stream(RegressionTab.values())
            .filter(regressionTab -> regressionTab.getId() == acValue)
            .map(RegressionTab::getValue)
            .findFirst().orElse(null);
    }

    public void switchEvidence(int indexToSwap, int index) {
        Component[] components = getComponents();

        Component componentToSwap = components[indexToSwap];
        Component component = components[index];

        remove(componentToSwap);
        remove(component);

        add(component, indexToSwap);
        add(componentToSwap, index);

        GuiUtils.refreshComponent(this);
    }
}
