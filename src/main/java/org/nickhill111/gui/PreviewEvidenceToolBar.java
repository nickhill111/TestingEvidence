package org.nickhill111.gui;

import lombok.Getter;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.util.Objects.nonNull;
import static org.nickhill111.util.GuiUtils.MEDIUM_FONT;
import static org.nickhill111.util.GuiUtils.SMALL_FONT;
import static org.nickhill111.util.GuiUtils.TITLE_FONT;
import static org.nickhill111.util.GuiUtils.convertZoomValueToScale;

@Getter
public class PreviewEvidenceToolBar extends JToolBar {
    private final PreviewEvidencePanel picture;
    private JSlider zoomSlider;
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final Config config = Config.getInstance();

    public PreviewEvidenceToolBar(PreviewEvidencePanel picture) {
        this.picture = picture;

        setFloatable(false);
        setPreferredSize(new Dimension(100, 60));

        addToStartButton();
        addSeparator();
        addLeftButton();
        addSeparator();
        addRightButton();
        addSeparator();
        addToEndButton();
        addSeparator();
        addZoomLabel();
        addZoomSlider();
    }

    private void addToStartButton() {
        JButton toStartButton = new JButton("<<");
        toStartButton.setFont(TITLE_FONT);
        toStartButton.setToolTipText("Skip to first image");
        toStartButton.setPreferredSize(new Dimension(100, 60));
        toStartButton.addActionListener(e -> {
            ScenarioPanel selectedScenario = frameComponents.getUsers().getSelectedScenarios().getSelectedScenario();

            BufferedImage nextImage = selectedScenario.getPhotoAt(0);

            if (nonNull(nextImage)) {
                picture.setImage(nextImage);
                picture.setScale(picture.getScale());
            }
        });

        add(toStartButton);
    }

    private void addLeftButton() {
        JButton moveButton = new JButton("<");
        moveButton.setFont(TITLE_FONT);
        moveButton.setToolTipText("Previous image");
        moveButton.setPreferredSize(new Dimension(60, 60));
        addMoveButtonActionListener(moveButton, -1);

        add(moveButton);
    }

    private void addRightButton() {
        JButton moveButton = new JButton(">");
        moveButton.setFont(TITLE_FONT);
        moveButton.setToolTipText("Next image");
        moveButton.setPreferredSize(new Dimension(60, 60));
        addMoveButtonActionListener(moveButton, 1);

        add(moveButton);
    }

    private void addMoveButtonActionListener(JButton moveButton, int locationToMoveBy) {
        moveButton.addActionListener(e -> {
            ScenarioPanel selectedScenario = frameComponents.getUsers().getSelectedScenarios().getSelectedScenario();

            BufferedImage nextImage = selectedScenario.getPhotoNextTo(picture.getImage(), locationToMoveBy);

            if (nonNull(nextImage)) {
                picture.setImage(nextImage);
                picture.setScale(picture.getScale());
            }
        });
    }

    private void addToEndButton() {
        JButton toEndButton = new JButton(">>");
        toEndButton.setFont(TITLE_FONT);
        toEndButton.setToolTipText("Skip to last image");
        toEndButton.setPreferredSize(new Dimension(100, 60));
        toEndButton.addActionListener(e -> {
            ScenarioPanel selectedScenario = frameComponents.getUsers().getSelectedScenarios().getSelectedScenario();

            BufferedImage nextImage = selectedScenario.getLastPhoto();

            if (nonNull(nextImage)) {
                picture.setImage(nextImage);
                picture.setScale(picture.getScale());
            }
        });

        add(toEndButton);
    }

    private void addZoomLabel() {
        JLabel zoomLabel = new JLabel("Zoom: ");
        zoomLabel.setFont(MEDIUM_FONT);
        add(zoomLabel);
    }

    private void addZoomSlider() {
        JSlider zoomSlider = new JSlider(30, 150, config.getConfigDetails().getPreviewZoomValue());

        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setFont(SMALL_FONT);

        zoomSlider.addChangeListener(e -> {
            if (!zoomSlider.getValueIsAdjusting()) {
                zoomSlider.setValue((int) (Math.round(zoomSlider.getValue()/10.0) * 10));

                picture.setScale(convertZoomValueToScale(zoomSlider.getValue()));

                config.getConfigDetails().setPreviewZoomValue(zoomSlider.getValue());
            }
        });

        add(zoomSlider);
        this.zoomSlider = zoomSlider;
    }
}
