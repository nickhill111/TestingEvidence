package org.nickhill111.testManager.gui;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.nickhill111.common.data.Icons.FAILED_ICON;
import static org.nickhill111.common.data.Icons.PASSED_ICON;
import static org.nickhill111.testManager.model.PassFailIcons.getValueFromGeneratedTextValue;
import static org.nickhill111.testManager.model.TabNames.REGRESSION;
import static org.nickhill111.testManager.model.TabNames.SCENARIO;
import static org.nickhill111.common.util.FileUtils.GENERATED_TEXT_FILE_NAME;
import static org.nickhill111.common.util.FileUtils.getScenariosFromFile;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.nickhill111.common.gui.GenericScrollPane;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.model.PassFailIcons;
import org.nickhill111.testManager.model.RegressionTab;
import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.common.util.GuiUtils;

public class Scenarios extends JTabbedPane implements MouseListener {
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();

    public Scenarios(List<File> files) {
        addScenarioTab(files);
        addMouseListener(this);
    }

    public Scenarios(List<File> files, boolean isRegression) {
        if (isRegression) {
            addRegressionTabs(files);
        } else {
            addScenarioTab(files);
        }
        addMouseListener(this);
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

        String generatedScenarioLines = getScenariosFromFile(new File(lastFile.getParentFile(), GENERATED_TEXT_FILE_NAME));

        for (int i = 1; i <= lastFileNumber; i++) {
            int finalI = i;
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().startsWith(userType + "_" + SCENARIO.getValue() + "_" + finalI + "_"))
                .toList();
            ScenarioPanel scenarioPanel = new ScenarioPanel(i, filesForTab);

            GenericScrollPane<ScenarioPanel> scrollPane = new GenericScrollPane<>(scenarioPanel);

            String title = SCENARIO.getValue() + "_" + i;

            addTab(title, scrollPane);

            selectNewComponent(scrollPane);

            if (nonNull(generatedScenarioLines)) {
                try {
                    String generatedTextValue = generatedScenarioLines.split(title + ":")[1].trim().split(System.lineSeparator())[0];

                    tryToSetIconAt(i - 1, generatedTextValue);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void tryToSetIconAt(int index, String generatedTextValue) {
        PassFailIcons passFailIcons = getValueFromGeneratedTextValue(generatedTextValue.trim());

        if (nonNull(passFailIcons)) {
            setIconAt(index, passFailIcons.getIcon());
        }
    }

    public void addEmptyTab() {
        Users users = testManagerComponents.getUsers();
        if (users.getTitleAt(users.getSelectedIndex()).equals(REGRESSION.getValue())) {
            DialogUtils.cantAddTabUnderRegression();
        } else {
            addNewEmptyTab();
        }
    }

    private void addNewEmptyTab() {
        int scenarioNumber = getTabCount() + 1;

        ScenarioPanel scenarioPanel = new ScenarioPanel(scenarioNumber);

        GenericScrollPane<ScenarioPanel> scrollPane = new GenericScrollPane<>(scenarioPanel);

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

            GenericScrollPane<ScenarioPanel> scrollPane = new GenericScrollPane<>(scenarioPanel);

            addTab(regressionTab.getValue(), scrollPane);
        }
    }

    private void addPopulatedRegressionTabs(List<File> files) {
        for (RegressionTab regressionTab : RegressionTab.values()) {
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().contains(regressionTab.getValue() + "_"))
                .toList();

            ScenarioPanel scenarioPanel = new ScenarioPanel(regressionTab.getId(), filesForTab);

            GenericScrollPane<ScenarioPanel> scrollPane = new GenericScrollPane<>(scenarioPanel);

            addTab(regressionTab.getValue(), scrollPane);

            selectNewComponent(scrollPane);
        }
    }

    public List<ScenarioPanel> getScenarioPanels() {
        List<ScenarioPanel> scenarioPanels = new LinkedList<>();

        for (int i = 0; i < getTabCount(); i++) {
            GenericScrollPane<?> scrollPane = ((GenericScrollPane<?>) getComponentAt(i));
            JPanel panel = scrollPane.getPanel();

            if (panel instanceof ScenarioPanel scenarioPanel) {
                scenarioPanels.add(scenarioPanel);
            }
        }

        return scenarioPanels;
    }

    private void selectNewComponent(GenericScrollPane<ScenarioPanel> scrollPane) {
        setSelectedComponent(scrollPane);
        GuiUtils.refreshComponent(this);
    }

    public ScenarioPanel getSelectedScenario() {
        if (getSelectedComponent() instanceof GenericScrollPane<?> scrollPane) {
            JPanel panel = scrollPane.getPanel();

            if (panel instanceof ScenarioPanel scenarioPanel) {
                return scenarioPanel;
            }
        }

        return null;
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
            int selectedIndex = getSelectedIndex();

            JPopupMenu menu = new JPopupMenu();
            if (getTitleAt(selectedIndex).equals(SCENARIO.getValue() + "_" + (getTabCount()))) {

                JMenuItem deleteMenuItem = new JMenuItem("Delete");
                deleteMenuItem.addActionListener(e1 -> {
                    removeTabAt(selectedIndex);
                    GuiUtils.refreshComponent(testManagerComponents.getFrame());
                });
                menu.add(deleteMenuItem);
            }

            JMenuItem passMenuItem = new JMenuItem("Pass");
            passMenuItem.addActionListener(e1 -> setIconAt(selectedIndex, PASSED_ICON));
            menu.add(passMenuItem);

            JMenuItem failMenuItem = new JMenuItem("Fail");
            failMenuItem.addActionListener(e1 -> setIconAt(selectedIndex, FAILED_ICON));
            menu.add(failMenuItem);


            JMenuItem removeAllEvidence = new JMenuItem("Remove all evidence");
            removeAllEvidence.addActionListener(e1 -> {
                if (getComponentAt(selectedIndex) instanceof GenericScrollPane<?> scrollPane) {
                    scrollPane.getPanel().removeAll();
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
}