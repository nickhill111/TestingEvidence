package org.example.gui;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.example.model.TabNames.REGRESSION;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.data.Settings;
import org.example.util.DialogUtils;
import org.example.util.GuiUtils;

public class UserTabbedPane extends JTabbedPane {
    private final Settings settings = Settings.getInstance();

    public UserTabbedPane() {
        settings.setUserTabbedPane(this);

        if (settings.isActivefolder()) {
            File activeFolder = settings.getActiveFolder();
            File[] evidenceToOpen = activeFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (isNull(evidenceToOpen)) {
                DialogUtils.cantOpenFolder();
                return;
            }

            List<String> userTypes = Arrays.stream(evidenceToOpen)
                .map(File::getName)
                .map(name -> name.substring(0, name.indexOf("_")))
                .distinct().toList();

            addUserTypeTabs(userTypes, evidenceToOpen);
        } else {
            addEmptyTab();
        }

        addMouseListener();
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

                    JMenuItem deleteMenuItem = new JMenuItem("Delete");
                    deleteMenuItem.addActionListener(e1 -> {
                        int index = getSelectedIndex();
                        removeTabAt(index);
                        GuiUtils.refreshComponent(settings.getUserTabbedPane());
                    });
                    menu.add(deleteMenuItem);

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

    private void addUserTypeTabs(List<String> userTypes, File[] evidenceToOpen) {
        for (String userType : userTypes) {
            List<File> userTypeFiles = Arrays.stream(evidenceToOpen)
                .filter(file -> file.getName().startsWith(userType)).toList();

            addTab(userType, userTypeFiles);
        }
    }

    public void addEmptyTab() {
        addTab(DialogUtils.askForUserType(), List.of());
    }

    private void addTab(String userType, List<File> files) {
        if (nonNull(userType)) {

            if (REGRESSION.getValue().equals(userType)) {
                addRegressionTab(files);
                return;
            }

            AcTabbedPane acTabbedPane = new AcTabbedPane(files);

            addTab(userType, acTabbedPane);
            setSelectedComponent(acTabbedPane);

            GuiUtils.refreshComponent(this);
        }
    }

    public AcTabbedPane getSelectedAcTabbedPane() {
        if (getSelectedComponent() instanceof AcTabbedPane acTabbedPane) {
            return acTabbedPane;
        }

        return null;
    }

    public Map<String, List<PreviewPanel>> getAllPreviewPanels() {
        Map<String, List<PreviewPanel>> allPreviewPanels = new HashMap<>();

        for (int i = 0; i < getTabCount(); i++) {
            AcTabbedPane acTabbedPane = ((AcTabbedPane) getComponentAt(i));

            allPreviewPanels.put(getTitleAt(i), acTabbedPane.getPreviewPanels());
        }

        return allPreviewPanels;
    }

    public void addEmptyRegressionTab() {
        addRegressionTab(null);
    }

    public void addRegressionTab(List<File> files) {
        if (doesRegressionTestingTabExist()) {
            DialogUtils.regressionTestTabAlreadyExists();
        } else {
            AcTabbedPane acTabbedPane = new AcTabbedPane(files, true);

            insertTab(REGRESSION.getValue(), null, acTabbedPane, null, 0);
            setSelectedComponent(acTabbedPane);

            GuiUtils.refreshComponent(this);
        }
    }

    private boolean doesRegressionTestingTabExist() {
        return doesUserExist(REGRESSION.getValue());
    }

    public boolean doesUserExist(String userType) {
        for (int i = 0; i < getTabCount(); i++) {
            if (userType.equals(getTitleAt(i))) {
                return true;
            }
        }

        return false;
    }
}
