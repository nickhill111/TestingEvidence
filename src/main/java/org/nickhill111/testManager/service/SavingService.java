package org.nickhill111.testManager.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.nickhill111.common.util.FileUtils.GENERATED_TEXT_FILE_NAME;
import static org.nickhill111.common.util.FileUtils.deleteOldFiles;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.nickhill111.common.data.TestManagerConfigDetails;
import org.nickhill111.testManager.TestingEvidenceApplication;
import org.nickhill111.common.data.Config;
import org.nickhill111.common.data.ConfigDetails;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.gui.ProgressBar;
import org.nickhill111.testManager.gui.ScenarioPanel;
import org.nickhill111.common.util.DialogUtils;
public class SavingService {
    private final Config config = Config.getInstance();
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
    private boolean isSaving = false;
    private XWPFDocument document;

    public void saveAllScreenshots() {
        if (!isSaving) {
            isSaving = true;

            new Thread(() -> {
                try {
                    if (config.isOpenedFolderPathActive()) {
                        saveScreenshots();
                    } else {
                        saveAllScreenshotsWithFileChooser();
                    }
                } catch (Exception e) {
                    DialogUtils.cantSaveDialog(e);
                }

                isSaving = false;
            }).start();

        } else {
            DialogUtils.currentlySavingDialog();
        }
    }

    public void saveAllScreenshotsWithFileChooser() {
        String previousTitle = testManagerComponents.getFrame().getTitle();

        if (!config.isOpenedFolderPathActive()) {
            String ticketNumber = DialogUtils.askForTicketNumber();
            if (isNull(ticketNumber)) {
                return;
            }
        }
        ConfigDetails configDetails = config.getConfigDetails();
        JFileChooser fileChooser = new JFileChooser(configDetails.getTestManagerConfigDetails().getFileChooserLocation());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Save Screenshots");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToSave = getFolderName(fileChooser.getSelectedFile());
            saveScreenshots(folderToSave);

            configDetails.getTestManagerConfigDetails().setFileChooserLocation(folderToSave.getAbsolutePath());
            config.saveConfig();
        } else {
            testManagerComponents.getFrame().setTitle(previousTitle);
        }
    }

    private void saveScreenshots() {
        saveScreenshots(new File(config.getConfigDetails().getTestManagerConfigDetails().getOpenedFolderPath()));
    }

    private void saveScreenshots(File folder) {
        File[] existingFiles = folder.listFiles();

        testManagerComponents.getFrame().setTitle(folder.getName());

        if (folder.exists() || folder.mkdirs()) {
            config.getConfigDetails().getTestManagerConfigDetails().setOpenedFolderPath(folder.getAbsolutePath());

            generateTextAndScreenshots(folder);

            DialogUtils.screenshotsSavedDialog(folder);
        } else {
            DialogUtils.cantSaveDialog(new Exception("Unable to create folder"));

            return;
        }

        if (nonNull(existingFiles)) {
            deleteOldFiles(existingFiles);
        }

        saveGeneratedTextToFile(folder);

        config.getConfigDetails().getTestManagerConfigDetails().setOpenedFolderPath(folder.getAbsolutePath());
    }

    private void generateTextAndScreenshots(File folder) {
        document = new XWPFDocument();
        StringBuilder generatedText = new StringBuilder();

        Map<String, List<ScenarioPanel>> allPreviewPanels = testManagerComponents.getUsers().getAllScenarioPanels();

        ProgressBar progressBar = new ProgressBar(allPreviewPanels.keySet().size(), testManagerComponents.getFrame().getGraphicsConfiguration());

        for (String userType : allPreviewPanels.keySet()) {
            String textTitle = userType + " testing:";

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun userRun = paragraph.createRun();
            userRun.setText(textTitle);
            userRun.addBreak();
            userRun.addBreak();

            generatedText.append(textTitle).append("\n\n");
            List<ScenarioPanel> scenarioPanels = allPreviewPanels.get(userType);

            for (ScenarioPanel scenarioPanel : scenarioPanels) {
                XWPFRun scenarioRun = paragraph.createRun();
                String generatedTextForScenario = scenarioPanel.saveAllEvidence(userType, folder, scenarioRun);

                if (nonNull(generatedTextForScenario)) {
                    generatedText.append(generatedTextForScenario).append("\n\n");
                } else {
                    testManagerComponents.getGeneratedTextArea().setText("Error when saving!");
                    return;
                }
            }

            progressBar.incrementValue();
        }

        progressBar.dispose();

        testManagerComponents.getGeneratedTextArea().setText(generatedText.toString());
    }

    private void saveGeneratedTextToFile(File folder) {
        String text = testManagerComponents.getGeneratedTextArea().getText();
        try {
            FileUtils.writeStringToFile(new File(folder, GENERATED_TEXT_FILE_NAME), text, StandardCharsets.UTF_8);
            FileOutputStream out = new FileOutputStream( new File(folder, testManagerComponents.getFrame().getTitle() + "-Evidence.docx"));
            document.write(out);
            out.close();
            document.close();
        } catch (IOException e) {
            DialogUtils.cantSaveGeneratedText(e);
        }
    }

    public File getFolderName(File folder) {
        folder = new File(folder, testManagerComponents.getFrame().getTitle());

        File temporaryFolder = new File(folder.getAbsolutePath());

        int number = 1;
        while (temporaryFolder.exists()) {
            temporaryFolder = new File(folder.getAbsolutePath() + "-" + number);
            number++;
        }

        return temporaryFolder;
    }

    public void openScreenshots() {
        TestManagerConfigDetails configDetails = config.getConfigDetails().getTestManagerConfigDetails();
        JFileChooser fileChooser = new JFileChooser(configDetails.getFileChooserLocation());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Open Screenshots");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToOpen = fileChooser.getSelectedFile();

            configDetails.setOpenedFolderPath(folderToOpen.getAbsolutePath());
            configDetails.setFileChooserLocation(folderToOpen.getParentFile().getAbsolutePath());
            config.saveConfig();

            testManagerComponents.getFrame().dispose();
            new TestingEvidenceApplication(folderToOpen);
        }
    }
}
