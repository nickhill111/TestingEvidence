package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.example.data.Settings;
import org.example.util.GuiUtils;
import org.imgscalr.Scalr;

@Getter
public class Photo extends JPanel {
    private final Settings settings = Settings.getInstance();
    private final BufferedImage originalImage;

    public Photo(BufferedImage image) {
        this.originalImage = image;

        add(createPicture(image));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new PhotoButtonPanel());
        addMouseListener();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private JLabel createPicture(BufferedImage image) {
        image = Scalr.resize(image, 300);
        JLabel picLabel = new JLabel(new ImageIcon(image));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return picLabel;
    }

    private void addMouseListener() {
        addMouseListener(new MouseListener() {
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

                    Map<String, List<PreviewPanel>> allPreviewPanels = settings.getUserTabbedPane().getAllPreviewPanels();

                    for (String user : allPreviewPanels.keySet()) {
                        JMenu userMenu = new JMenu(user);

                        for (PreviewPanel previewPanel : allPreviewPanels.get(user)) {
                            JMenuItem ac = new JMenuItem(previewPanel.getTabValue());
                            ac.addActionListener(e1 -> {
                                if (e.getSource() instanceof Photo photo) {
                                    photo.getParent().remove(photo);
                                    previewPanel.add(photo);

                                    GuiUtils.refreshComponent(settings.getFrame());
                                }
                            });
                            userMenu.add(ac);
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
        });
    }
}
