package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.util.GuiUtils;
import org.imgscalr.Scalr;

import static org.nickhill111.util.GuiUtils.PHOTO_SIZE;

@Getter
public class Photo extends JPanel implements MouseListener {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final BufferedImage originalImage;

    public Photo(BufferedImage image) {
        this.originalImage = image;
        addMouseListener(this);

        addPicture(image);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new PhotoButtons(image));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void addPicture(BufferedImage image) {
        image = Scalr.resize(image, PHOTO_SIZE);
        JLabel picLabel = new JLabel(new ImageIcon(image));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(picLabel);
    }

    public void removePhoto() {
        Container previewPanelComp = getParent();

        GridBagLayout gridBagLayout = (GridBagLayout) previewPanelComp.getLayout();

        int indexOfRemovedComp = gridBagLayout.getConstraints(this).gridx;

        previewPanelComp.remove(this);

        Arrays.stream(previewPanelComp.getComponents())
            .filter(component -> gridBagLayout.getConstraints(component).gridx > indexOfRemovedComp)
            .forEach(component -> {
                GridBagConstraints constraints = gridBagLayout.getConstraints(component);
                constraints.gridx--;

                previewPanelComp.remove(component);
                previewPanelComp.add(component, constraints);
            });

        GuiUtils.refreshComponent(previewPanelComp);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getModifiersEx() == MouseEvent.META_DOWN_MASK && e.getClickCount() == 1) {
            JPopupMenu menu = new JPopupMenu();

            JMenu moveToMenu = new JMenu("Move to");
            menu.add(moveToMenu);

            Map<String, List<ScenarioPanel>> allPreviewPanels = frameComponents.getUsers().getAllScenarioPanels();

            for (String user : allPreviewPanels.keySet()) {
                JMenu userMenu = new JMenu(user);

                for (ScenarioPanel scenarioPanel : allPreviewPanels.get(user)) {
                    JMenuItem scenario = new JMenuItem(scenarioPanel.getTabValue());
                    scenario.addActionListener(e1 -> {
                        if (e.getSource() instanceof Photo photo) {
                            removePhoto();
                            scenarioPanel.addEvidence(photo.getOriginalImage());

                            GuiUtils.refreshComponent(frameComponents.getFrame());
                        }
                    });
                    userMenu.add(scenario);
                }
                moveToMenu.add(userMenu);
            }

            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
