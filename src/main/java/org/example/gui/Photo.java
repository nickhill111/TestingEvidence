package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import lombok.Getter;
import org.imgscalr.Scalr;

@Getter
public class Photo extends JPanel {
    private final BufferedImage originalImage;

    public Photo(BufferedImage image) {
        this.originalImage = image;

        add(createPicture(image));
        add(Box.createRigidArea(new Dimension(0,5)));
        add(new PhotoButtonPanel());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private JLabel createPicture(BufferedImage image) {
        image = Scalr.resize(image, 300);
        JLabel picLabel = new JLabel(new ImageIcon(image));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return picLabel;
    }
}
