package org.nickhill111.gui;

import org.imgscalr.Scalr;
import org.nickhill111.data.FrameComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.nickhill111.util.GuiUtils.PHOTO_SIZE;
import static org.nickhill111.util.GuiUtils.refreshComponent;

public class PreviewEvidenceToolBar extends JToolBar {
    private final static String MAX = "Max";
    private final PreviewEvidenceFrame previewEvidenceFrame;
    private final JLabel picLabel;
    private final JComboBox<String> zoomComboBox;
    private BufferedImage image;
    private final FrameComponents frameComponents = FrameComponents.getInstance();

    public PreviewEvidenceToolBar(PreviewEvidenceFrame previewEvidenceFrame, JLabel picLabel, BufferedImage image) {
        this.previewEvidenceFrame = previewEvidenceFrame;
        this.image = image;
        this.picLabel = picLabel;

        setFloatable(false);
        setPreferredSize(new Dimension(100, 60));

        add(createLeftButton());
        add(createRightButton());
        addSeparator();
        add(createZoomLabel());
        this.zoomComboBox = createZoomComboBox();
        add(zoomComboBox);
    }

    private JButton createLeftButton() {
        JButton moveButton = new JButton("<");
        moveButton.setFont(new Font("Arial", Font.BOLD, 60));
        moveButton.setPreferredSize(new Dimension(60, 60));
        addMoveButtonActionListener(moveButton, -1);

        return moveButton;
    }

    private JButton createRightButton() {
        JButton moveButton = new JButton(">");
        moveButton.setFont(new Font("Arial", Font.BOLD, 60));
        moveButton.setPreferredSize(new Dimension(60, 60));
        addMoveButtonActionListener(moveButton, 1);

        return moveButton;
    }

    private void addMoveButtonActionListener(JButton moveButton, int locationToMoveBy) {
        moveButton.addActionListener(e -> {
            ScenarioPanel selectedScenario = frameComponents.getUsers().getSelectedScenarios().getSelectedScenario();

            BufferedImage nextImage = selectedScenario.getPhotoNextTo(image, locationToMoveBy);

            if (nonNull(nextImage)) {
                updateImageSizeAndFrameDimensions(nextImage);
                image = nextImage;
            }
        });
    }

    private JLabel createZoomLabel() {
        return new JLabel("Zoom: ");
    }

    private JComboBox<String> createZoomComboBox() {
        JComboBox<String> zoomSizesComboBox = new JComboBox<>();
        zoomSizesComboBox.addItem("1");
        zoomSizesComboBox.addItem("2");
        zoomSizesComboBox.addItem("3 (Default)");
        zoomSizesComboBox.addItem("4");
        zoomSizesComboBox.addItem("5");
        zoomSizesComboBox.addItem(MAX);

        zoomSizesComboBox.setSelectedItem("3 (Default)");

        zoomSizesComboBox.addActionListener(e -> updateImageSizeAndFrameDimensions(image));

        return zoomSizesComboBox;
    }

    public void updateImageSizeAndFrameDimensions(BufferedImage imageToUpdate) {
        String selectedItem = (String) zoomComboBox.getSelectedItem();

        BufferedImage imageToSet = imageToUpdate;

        if (!MAX.equals(selectedItem)) {
            if (isNull(selectedItem)) {
                selectedItem = "3";
            }

            int selectedZoom = Integer.parseInt(selectedItem.replace(" (Default)", ""));
            imageToSet = Scalr.resize(imageToUpdate, PHOTO_SIZE * selectedZoom);
        }

        picLabel.setIcon(new ImageIcon(imageToSet));

        if (MAX.equals(selectedItem)) {
            previewEvidenceFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            previewEvidenceFrame.pack();
        }
        refreshComponent(previewEvidenceFrame);
    }
}
