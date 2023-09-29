package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PhotoButtons extends JPanel {
    private final BufferedImage image;

    public PhotoButtons(BufferedImage image) {
        this.image = image;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(createMoveLeftButton());
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(createRemoveButton());
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(createPreviewButton());
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(createMoveRightButton());
    }

    private JButton createPreviewButton() {
        JButton previewButton = new JButton("Preview");

        previewButton.addActionListener(e -> new PreviewEvidenceFrame(image));

        return previewButton;
    }

    private JButton createMoveLeftButton() {
        JButton moveButton = new JButton("<");
        addMoveButtonActionListener(moveButton, -1);

        return moveButton;
    }

    private JButton createMoveRightButton() {
        JButton moveButton = new JButton(">");
        addMoveButtonActionListener(moveButton, 1);

        return moveButton;
    }

    private void addMoveButtonActionListener(JButton moveButton, int movement) {
        moveButton.addActionListener(e -> {
            Container previewPanelComp = getParent().getParent();
            Container parentComp = getParent();

            GridBagLayout gridBagLayout = (GridBagLayout) previewPanelComp.getLayout();

            int index = gridBagLayout.getConstraints(parentComp).gridx;
            int indexToSwap = index + movement;

            if (indexToSwap < 0 || indexToSwap >= previewPanelComp.getComponentCount()) {
                return;
            }

            if (previewPanelComp instanceof ScenarioPanel scenarioPanel) {
                scenarioPanel.switchEvidence(indexToSwap, index);
            }
        });
    }

    private JButton createRemoveButton() {
        JButton removeButton = new JButton("Remove");

        removeButton.addActionListener(e -> {
            Container photoContainer = getParent();

            if (photoContainer instanceof Photo photo) {
                photo.removePhoto();
            }
        });

        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return removeButton;
    }
}
