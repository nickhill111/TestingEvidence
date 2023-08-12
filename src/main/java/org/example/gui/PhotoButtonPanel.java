package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

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

            if (previewPanelComp instanceof PreviewPanel previewPanel) {
                previewPanel.switchEvidence(indexToSwap, index);
            }
        });
    }

    private JButton createRemoveButton() {
        JButton removeButton = new JButton("Remove");

        removeButton.addActionListener(e -> {
            Container previewPanelComp = getParent().getParent();
            Container parentComp = getParent();

            GridBagLayout gridBagLayout = (GridBagLayout) previewPanelComp.getLayout();

            int indexOfRemovedComp = gridBagLayout.getConstraints(parentComp).gridx;

            previewPanelComp.remove(parentComp);

            Arrays.stream(previewPanelComp.getComponents())
                .filter(component -> gridBagLayout.getConstraints(component).gridx > indexOfRemovedComp)
                .forEach(component -> {
                    GridBagConstraints constraints = gridBagLayout.getConstraints(component);
                    constraints.gridx--;

                    previewPanelComp.remove(component);
                    previewPanelComp.add(component, constraints);
                });

            GuiUtils.refreshComponent(previewPanelComp);
        });

        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return removeButton;
    }
}
