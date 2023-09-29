package org.nickhill111.gui;

import org.nickhill111.util.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JFrame {
    JProgressBar progressBar;
    public ProgressBar(int size) {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(20,10)));

        JLabel savingLabel = new JLabel("Saving Evidence");
        savingLabel.setFont(new Font("Arial", Font.BOLD, 24));
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
