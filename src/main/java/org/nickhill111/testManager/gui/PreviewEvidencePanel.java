package org.nickhill111.testManager.gui;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static org.nickhill111.common.util.GuiUtils.refreshComponent;

@Getter
@Setter
class PreviewEvidencePanel extends JPanel {
    private BufferedImage image;
    private double scale;

    public PreviewEvidencePanel(BufferedImage image, double scale) {
        this.image = image;
        this.scale = scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (getWidth() - scale * imageWidth) / 2;
        double y = (getHeight() - scale * imageHeight) / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }

    @Override
    public Dimension getPreferredSize() {
        int width = (int) (scale * image.getWidth());
        int height = (int) (scale * image.getHeight());
        return new Dimension(width, height);
    }

    public void setScale(double newScale) {
        scale = newScale;
        refreshComponent(this);
    }
}
