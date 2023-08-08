package org.example.gui;

import javax.swing.*;
import java.awt.*;
import org.example.util.GuiUtils;

public class PhotoButtonPanel extends JPanel {

    PhotoButtonPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(createMoveLeftButton());
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(createRemoveButton());
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(createMoveRightButton());
    }

    private JButton createMoveLeftButton() {
        JButton moveButton = new JButton("<");

        moveButton.addActionListener(e -> {
            Container parent = getParent().getParent();
            int index = parent.getComponentZOrder(this.getParent());

            if (index == 0) {
                return;
            }

            int indexToSwap = index - 2;

            if (parent instanceof PreviewPanel previewPanel) {
                previewPanel.switchEvidence(indexToSwap, index);
            }
        });

        return moveButton;
    }

    private JButton createMoveRightButton() {
        JButton moveButton = new JButton(">");

        moveButton.requestFocusInWindow();

        moveButton.addActionListener(e -> {
            Container parent = getParent().getParent();
            int index = parent.getComponentZOrder(this.getParent());
            int indexToSwap = index + 2;

            if (indexToSwap >= parent.getComponentCount()) {
                return;
            }

            if (parent instanceof PreviewPanel previewPanel) {
                previewPanel.switchEvidence(index, indexToSwap);
            }
        });

        return moveButton;
    }

    private JButton createRemoveButton() {
        JButton removeButton = new JButton("Remove");

        removeButton.addActionListener(e -> {
            Container parent = getParent().getParent();

            int position = parent.getComponentZOrder(this.getParent());
            parent.remove(position + 1);
            parent.remove(position);

            GuiUtils.refreshComponent(parent);
        });

        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return removeButton;
    }
}
