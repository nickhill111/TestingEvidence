package org.nickhill111.testManager.gui;

import org.nickhill111.common.util.GuiUtils;

import javax.swing.*;
import java.awt.*;

import static org.nickhill111.common.util.GuiUtils.MEDIUM_FONT;

public class ProgressBar extends JFrame {
    private final JProgressBar progressBar;
    public ProgressBar(int size, GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(20,10)));

        JLabel savingLabel = new JLabel("Saving Evidence");
        savingLabel.setFont(MEDIUM_FONT);
        savingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(savingLabel);
        add(Box.createRigidArea(new Dimension(20,10)));

        progressBar = new JProgressBar(0, size);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        Dimension progressBarSize = new Dimension(250, 35);
        progressBar.setMinimumSize(progressBarSize);
        progressBar.setMaximumSize(progressBarSize);
        progressBar.setPreferredSize(progressBarSize);
        progressBar.setSize(progressBarSize);
        add(progressBar, Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,10)));

        setTitle("Saving");
        setAlwaysOnTop(true);
        pack();
        GuiUtils.setupGui(this);
        setResizable(false);
    }

    public void incrementValue() {
        progressBar.setValue(progressBar.getValue() + 1);
    }
}
