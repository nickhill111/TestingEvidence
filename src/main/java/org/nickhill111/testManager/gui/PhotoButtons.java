package org.nickhill111.testManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.common.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.nickhill111.common.data.Icons.PREVIEW_ICON;
import static org.nickhill111.common.data.Icons.REMOVE_ICON;
import static org.nickhill111.common.util.GuiUtils.MEDIUM_FONT;

public class PhotoButtons extends JPanel {
    private final BufferedImage image;
    private final Config config = Config.getInstance();

    public PhotoButtons(BufferedImage image) {
        this.image = image;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        addMoveLeftButton();
        addBreak();
        addRemoveButton();
        addBreak();
        addPreviewButton();
        addBreak();
        addMoveRightButton();
    }

    private void addBreak() {
        add(Box.createRigidArea(new Dimension(5, 0)));
    }

    private void addPreviewButton() {
        JButton previewButton = new JButton(PREVIEW_ICON);
        previewButton.setToolTipText("Preview Image");

        previewButton.addActionListener(e -> new PreviewEvidenceFrame(image,
            GuiUtils.getScreen(config.getConfigDetails().getTestManagerConfigDetails().getPreviewFrameConfigDetails().getWindowScreenId())));

        add(previewButton);
    }

    private void addMoveLeftButton() {
        JButton moveButton = new JButton("<");
        addMoveButtonActionListener(moveButton, -1);

        add(moveButton);
    }

    private void addMoveRightButton() {
        JButton moveButton = new JButton(">");
        addMoveButtonActionListener(moveButton, 1);

        add(moveButton);
    }

    private void addMoveButtonActionListener(JButton moveButton, int movement) {
        moveButton.setFont(MEDIUM_FONT);
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

    private void addRemoveButton() {
        JButton removeButton = new JButton(REMOVE_ICON);
        removeButton.setToolTipText("Delete");

        removeButton.addActionListener(e -> {
            Container photoContainer = getParent();

            if (photoContainer instanceof Photo photo) {
                photo.removePhoto();
            }
        });

        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(removeButton);
    }
}
