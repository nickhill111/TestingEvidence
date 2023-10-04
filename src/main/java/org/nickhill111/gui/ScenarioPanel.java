package org.nickhill111.gui;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.apache.poi.common.usermodel.PictureType.PNG;
import static org.nickhill111.data.Icons.PASSED_ICON;
import static org.nickhill111.model.TabNames.SCENARIO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.nickhill111.model.RegressionTab;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class ScenarioPanel extends JPanel {
    private final int scenarioNumber;
    private final ScreenshotService screenshotService = new ScreenshotService();

    private GridBagConstraints constraints;
    private GridBagLayout layout;

    public ScenarioPanel(int scenarioNumber) {
        this.scenarioNumber = scenarioNumber;

        setLayout();
    }

    public ScenarioPanel(int scenarioNumber, List<File> filesToAdd) {
        this.scenarioNumber = scenarioNumber;
        setLayout();

        for (File file : filesToAdd) {
            try {
                addEvidence(ImageIO.read(file));
            } catch (IOException e) {
                DialogUtils.unableToOpenImageDialog(file);
            }
        }
    }

    private void setLayout() {
        layout = new GridBagLayout();
        setLayout(layout);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 10, 0, 10);
    }

    public void addEvidence(BufferedImage screenshot) {
        constraints.gridx = getComponentCount();
        add(new Photo(screenshot), constraints);
    }

    public String getTabValue() {
        if (scenarioNumber <= 0) {
            return getRegressionTabName();
        }

        return SCENARIO.getValue() + "_" + scenarioNumber;
    }

    private Component[] getComponentsInGbcOrder() {
        Component[] components = new Component[getComponentCount()];
        for (Component component : getComponents()) {
            components[layout.getConstraints(component).gridx] = component;
        }

        return components;
    }

    @SneakyThrows
    public String saveAllEvidence(String userType, File folder, XWPFRun run) {
        String tabValue = getTabValue();

        StringBuilder generatedText = new StringBuilder();

        Component[] components = getComponentsInGbcOrder();

        if (components.length != 0) {
            String iconValue = getIconValue();
            run.setText(tabValue + ": " + getIconValueForWord(iconValue));
            run.addBreak();
            run.addBreak();
            generatedText.append(tabValue).append(": ").append(iconValue).append("\n\n ");

            for (Component component : components) {
                try {
                    int gridx = layout.getConstraints(component).gridx;

                    if (component instanceof Photo photo) {
                        String fileName = userType + "_" + tabValue + "_" + (gridx + 1);
                        fileName = verifyFileName(fileName, folder.listFiles());
                        fileName += PNG.getExtension();
                        generatedText.append("!").append(fileName).append("|thumbnail! ");

                        File savedPhoto = new File(folder, fileName);

                        BufferedImage image = photo.getOriginalImage();
                        if (!savePhoto(image, savedPhoto)) {
                            return null;
                        }

                        double width = (double) image.getWidth()/3;
                        double height = (double) image.getHeight()/3;

                        if (width > 500) {
                            height = (double) image.getHeight()/((double) image.getWidth()/500);
                            width = 500;
                        }

                        FileInputStream fileInputStream = new FileInputStream(savedPhoto);
                        run.addPicture(fileInputStream, PNG, savedPhoto.getName(), Units.toEMU(width), Units.toEMU((height)));
                        fileInputStream.close();
                        run.addTab();
                    }
                } catch (Exception e) {
                    throw new Exception(e.getMessage() +"\n\n Please delete and re-add all evidence under " + tabValue);
                }
            }
        }

        return generatedText.toString();
    }

    private String getIconValueForWord(String iconValue) {
        if (nonNull(iconValue) && isNotEmpty(iconValue)) {
            if (iconValue.equals("(/)")) {
                return "PASSED";
            }

            return "FAILED";
        }

        return "";
    }

    private String getIconValue() {
        if (getParent().getParent().getParent() instanceof Scenarios scenarios) {
            Icon icon = scenarios.getIconAt(scenarioNumber - 1);

            if (nonNull(icon)) {
                if (icon.equals(PASSED_ICON)) {
                    return "(/)";
                }

                return "(x)";
            }
        }

        return "";
    }

    private boolean savePhoto(BufferedImage image, File file) {
        try {
            screenshotService.saveScreenshot(image, file);
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
            .filter(regressionTab -> regressionTab.getId() == scenarioNumber)
            .map(RegressionTab::getValue)
            .findFirst().orElse(null);
    }

    public void switchEvidence(int indexToSwap, int index) {
        Component componentToSwap = getComponentWithGridxPosition(indexToSwap);
        GridBagConstraints constraintsToSwap = layout.getConstraints(componentToSwap);

        Component component = getComponentWithGridxPosition(index);
        GridBagConstraints constraints = layout.getConstraints(component);

        remove(component);
        remove(componentToSwap);

        add(component, constraintsToSwap);
        add(componentToSwap, constraints);

        GuiUtils.refreshComponent(this);
    }

    private Component getComponentWithGridxPosition(int gridx) {
        return Arrays.stream(getComponents())
            .filter(component -> layout.getConstraints(component).gridx == gridx)
            .findFirst()
            .orElse(null);
    }

    private Photo getPhotoWithGridxPosition(int gridx) {
        Component nextPhoto = getComponentWithGridxPosition(gridx);

        if (nextPhoto instanceof Photo photo) {
            return photo;
        }

        return null;
    }

    public BufferedImage getPhotoNextTo(BufferedImage image, int locationToMoveBy) {
        Component[] components = getComponentsInGbcOrder();

        for (int i = 0; i < components.length; i++) {
            int locationToMoveTo = i + locationToMoveBy;

            if (locationToMoveTo >= 0 && locationToMoveTo < components.length && components[i] instanceof Photo photo) {
                if (photo.getOriginalImage().equals(image)) {
                    return getPhotoWithGridxPosition(locationToMoveTo).getOriginalImage();
                }
            }
        }

        return null;
    }
}
