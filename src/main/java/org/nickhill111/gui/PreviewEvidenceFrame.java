package org.nickhill111.gui;

import org.imgscalr.Scalr;
import org.nickhill111.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.nickhill111.util.GuiUtils.PHOTO_SIZE;

public class PreviewEvidenceFrame extends JFrame {

    public PreviewEvidenceFrame(BufferedImage image) {
        JPanel jPanel = new JPanel();
        BufferedImage scaledImage = Scalr.resize(image, PHOTO_SIZE * 3);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(picLabel);

        add(jPanel);
        PreviewEvidenceToolBar toolBar = new PreviewEvidenceToolBar(this, picLabel, image);
        add(toolBar, BorderLayout.PAGE_END);

        setTitle("Preview Evidence");
        setAlwaysOnTop(true);
        pack();
        GuiUtils.setupGui(this);
        setResizable(false);
    }
}
