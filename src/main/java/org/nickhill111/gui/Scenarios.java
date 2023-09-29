package org.nickhill111.gui;

import static java.util.Objects.isNull;
import static org.nickhill111.model.TabNames.REGRESSION;
import static org.nickhill111.model.TabNames.SCENARIO;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.model.RegressionTab;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class Scenarios extends JTabbedPane {
    private final FrameComponents frameComponents = FrameComponents.getInstance();

    public Scenarios(List<File> files) {
        addScenarioTab(files);
        addMouseListener();
    }

    public Scenarios(List<File> files, boolean isRegression) {
        if (isRegression) {
            addRegressionTabs(files);
        } else {
            addScenarioTab(files);
        }
        addMouseListener();
    }

    private void addScenarioTab(List<File> files) {
        if (isNull(files) || files.isEmpty()) {
            addNewEmptyTab();
        } else {
            addPopulatedTab(files);
        }
    }

    private void addPopulatedTab(List<File> files) {
        File lastFile = files.get(files.size() -1);
        String[] lastFileNameSplit = lastFile.getName().split("_");

        String userType = lastFileNameSplit[0];
        int lastFileNumber = Integer.parseInt(lastFileNameSplit[2]);

        for (int i = 1; i <= lastFileNumber; i++) {
            int finalI = i;
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().startsWith(userType + "_" + SCENARIO.getValue() + "_" + finalI + "_"))
                .toList();
            ScenarioPanel scenarioPanel = new ScenarioPanel(i, filesForTab);

            ScrollPane scrollPane = new ScrollPane(scenarioPanel);

            addTab(SCENARIO.getValue() + "_" + i, scrollPane);

            selectNewComponent(scrollPane);
        }
    }

    public void addEmptyTab() {
        Users users = frameComponents.getUsers();
        if (users.getTitleAt(users.getSelectedIndex()).equals(REGRESSION.getValue())) {
            DialogUtils.cantAddTabUnderRegression();
        } else {
            addNewEmptyTab();
        }
    }

    private void addNewEmptyTab() {
        int scenarioNumber = getTabCount() + 1;

        ScenarioPanel scenarioPanel = new ScenarioPanel(scenarioNumber);

        ScrollPane scrollPane = new ScrollPane(scenarioPanel);

        addTab(SCENARIO.getValue() + "_" + scenarioNumber, scrollPane);

        selectNewComponent(scrollPane);
    }

    private void addRegressionTabs(List<File> files) {
        if (isNull(files) || files.isEmpty()) {
            addEmptyRegressionTabs();
        } else {
            addPopulatedRegressionTabs(files);
        }
    }

    private void addEmptyRegressionTabs() {
        for (RegressionTab regressionTab : RegressionTab.values()) {
            ScenarioPanel scenarioPanel = new ScenarioPanel(regressionTab.getId());

            ScrollPane scrollPane = new ScrollPane(scenarioPanel);

            addTab(regressionTab.getValue(), scrollPane);
        }
    }

    private void addPopulatedRegressionTabs(List<File> files) {
        for (RegressionTab regressionTab : RegressionTab.values()) {
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().contains(regressionTab.getValue() + "_"))
                .toList();

            ScenarioPanel scenarioPanel = new ScenarioPanel(regressionTab.getId(), filesForTab);

            ScrollPane scrollPane = new ScrollPane(scenarioPanel);

            addTab(regressionTab.getValue(), scrollPane);

            selectNewComponent(scrollPane);
        }
    }

    public List<ScenarioPanel> getScenarioPanels() {
        List<ScenarioPanel> scenarioPanels = new LinkedList<>();

        for (int i = 0; i < getTabCount(); i++) {
            ScenarioPanel scenarioPanel = ((ScrollPane) getComponentAt(i)).getScenarioPanel();
            scenarioPanels.add(scenarioPanel);
        }

        return scenarioPanels;
    }

    private void selectNewComponent(ScrollPane scrollPane) {
        setSelectedComponent(scrollPane);
        GuiUtils.refreshComponent(this);
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
                    int selectedIndex = getSelectedIndex();

                    JPopupMenu menu = new JPopupMenu();
                    if (getTabCount() > 1 && selectedIndex == getTabCount() - 1 && getTitleAt(selectedIndex).startsWith(SCENARIO.getValue() + " ")) {

                        JMenuItem deleteMenuItem = new JMenuItem("Delete");
                        deleteMenuItem.addActionListener(e1 -> {
                            removeTabAt(selectedIndex);
                            GuiUtils.refreshComponent(frameComponents.getFrame());
                        });
                        menu.add(deleteMenuItem);
                    }

                    JMenuItem removeAllEvidence = new JMenuItem("Remove all evidence");
                    removeAllEvidence.addActionListener(e1 -> {
                        if (getComponentAt(selectedIndex) instanceof ScrollPane scrollPane) {
                            scrollPane.getScenarioPanel().removeAll();
                            GuiUtils.refreshComponent(scrollPane);
                        }
                    });
                    menu.add(removeAllEvidence);

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

    public ScenarioPanel getSelectedScenario() {
        if (getSelectedComponent() instanceof ScrollPane scrollPane) {
            return scrollPane.getScenarioPanel();
        }

        return null;
    }
}
