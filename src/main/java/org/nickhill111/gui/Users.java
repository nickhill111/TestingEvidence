package org.nickhill111.gui;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.poi.common.usermodel.PictureType.PNG;
import static org.nickhill111.model.TabNames.REGRESSION;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class Users extends JTabbedPane implements MouseListener {
    private final FrameComponents frameComponents = FrameComponents.getInstance();

    public Users() {
        frameComponents.setUsers(this);

        addMouseListener(this);

        Config config = Config.getInstance();
        if (config.isOpenedFolderPathActive()) {
            File activeFolder = new File(config.getConfigDetails().getOpenedFolderPath());
            File[] evidenceToOpen = activeFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(PNG.getExtension()));

            if (isNull(evidenceToOpen)) {
                DialogUtils.cantOpenFolder();
                return;
            }

            List<String> userTypes = Arrays.stream(evidenceToOpen)
                .map(File::getName)
                .map(name -> name.substring(0, name.indexOf("_")))
                .distinct().toList();

            addUserTypeTabs(userTypes, evidenceToOpen);
        }
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

            Scenarios scenarios = new Scenarios(files);

            addTab(userType, scenarios);
            setSelectedComponent(scenarios);

            GuiUtils.refreshComponent(this);
        }
    }

    public Scenarios getSelectedScenarios() {
        if (getSelectedComponent() instanceof Scenarios scenarios) {
            return scenarios;
        }

        return null;
    }

    public Map<String, List<ScenarioPanel>> getAllScenarioPanels() {
        Map<String, List<ScenarioPanel>> allScenarioPanels = new HashMap<>();

        for (int i = 0; i < getTabCount(); i++) {
            Scenarios scenarios = ((Scenarios) getComponentAt(i));

            allScenarioPanels.put(getTitleAt(i), scenarios.getScenarioPanels());
        }

        return allScenarioPanels;
    }

    public void addEmptyRegressionTab() {
        addRegressionTab(null);
    }

    public void addRegressionTab(List<File> files) {
        if (doesRegressionTestingTabExist()) {
            DialogUtils.regressionTestTabAlreadyExists();
        } else {
            Scenarios scenarios = new Scenarios(files, true);

            insertTab(REGRESSION.getValue(), null, scenarios, null, 0);
            setSelectedComponent(scenarios);

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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (getTabCount() > 1 && e.getModifiersEx() == MouseEvent.META_DOWN_MASK && e.getClickCount() == 1) {
            JPopupMenu menu = new JPopupMenu();

            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.addActionListener(e1 -> {
                int index = getSelectedIndex();
                removeTabAt(index);
                GuiUtils.refreshComponent(frameComponents.getUsers());
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
}
