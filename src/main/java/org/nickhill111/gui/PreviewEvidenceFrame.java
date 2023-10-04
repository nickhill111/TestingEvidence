package org.nickhill111.gui;

import org.nickhill111.data.Config;
import org.nickhill111.data.FrameConfigDetails;
import org.nickhill111.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static java.util.Objects.nonNull;
import static org.nickhill111.util.GuiUtils.convertZoomValueToScale;

public class PreviewEvidenceFrame extends JFrame {
    private final Config config = Config.getInstance();

    public PreviewEvidenceFrame(BufferedImage originalImage, GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);

        PreviewEvidencePanel previewEvidencePanel = new PreviewEvidencePanel(originalImage,
            convertZoomValueToScale(config.getConfigDetails().getPreviewZoomValue()));

        add(new JScrollPane(previewEvidencePanel));

        PreviewEvidenceToolBar toolBar = new PreviewEvidenceToolBar(previewEvidencePanel);
        add(toolBar, BorderLayout.PAGE_END);

        setTitle("Preview Evidence");
        setAlwaysOnTop(true);
        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getPreviewFrameConfigDetails();

        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            setSize(windowSize);
            setExtendedState(frameConfigDetails.getWindowState());
        }

        GuiUtils.setupGui(this);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                FrameConfigDetails frameConfigDetails = config.getConfigDetails().getPreviewFrameConfigDetails();
                frameConfigDetails.setWindowSize(getSize());
                frameConfigDetails.setWindowState(getExtendedState());
                frameConfigDetails.setWindowScreenId(getGraphicsConfiguration().getDevice().getIDstring());
                config.saveConfig();
            }
        });
    }
}
