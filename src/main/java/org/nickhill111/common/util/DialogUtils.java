package org.nickhill111.common.util;

import static java.util.Objects.nonNull;
import static org.apache.poi.common.usermodel.PictureType.PNG;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.gui.Users;

public class DialogUtils {
    private final static TestManagerComponents FRAME_COMPONENTS = TestManagerComponents.getInstance();

    public static void cantSaveDialog(Exception e) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Cannot save screenshots!\n\n" + e.getMessage(),
            "Cannot save screenshots", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantTakeScreenshotDialog(Exception e) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Cannot take screenshot!\n\n" + e.getMessage(),
            "Cannot take screenshot", JOptionPane.ERROR_MESSAGE);
    }

    public static void screenshotsSavedDialog(File folder) {
        String folderPath = folder.getPath();
        int dialogButton = JOptionPane.showConfirmDialog(FRAME_COMPONENTS.getFrame(),
            "Screenshots saved to: " + folderPath + "\n\n Would you like to open the folder?",
            "Screenshots Saved", JOptionPane.YES_NO_OPTION);

        if (dialogButton == JOptionPane.YES_OPTION) {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Cannot open " + folderPath + ":\n\n" + e.getMessage(),
                    "Cannot open folder", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static String askForTicketNumber() {
        String ticketNumber = JOptionPane.showInputDialog("Enter ticket number please");

        ticketNumber = checkValidFileName(ticketNumber);

        if (nonNull(ticketNumber)) {
            FRAME_COMPONENTS.getFrame().setTitle(ticketNumber);
        }

        return ticketNumber;
    }

    public static String askForUserType() {
        String userType = JOptionPane.showInputDialog("Enter user type please");

        userType = checkValidFileName(userType);

        return validateUserType(userType);
    }

    private static String validateUserType(String userType) {
        if (nonNull(userType)) {
            Users users = FRAME_COMPONENTS.getUsers();

            if (users.doesUserExist(userType)) {
                JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "User type " + userType + " already exists!",
                    "User type already exists", JOptionPane.ERROR_MESSAGE);
                return askForUserType();
            }
        }

        return userType;
    }

    public static String checkValidFileName(String name) {
        if (nonNull(name) && !name.isEmpty()) {
            name = name.trim().replace("_", " ");

            if (name.matches("^[A-Za-z0-9- ]*$")) {
                return name;
            }

            JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), name + " is not a valid name!\n" +
                    "Please only use letters, numbers, spaces or -",
                "Invalid name", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public static void regressionTestTabAlreadyExists() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Regression test tab already exists!",
            "Regression test tab exists", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantDeleteFile(String filePath) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(),
            "Cannot delete file: "
                + filePath
                + "\n\nPlease use \"Save As\" in the file menu or manually delete the folder.",
            "Cannot delete active folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantOpenFolder() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't open evidence from folder! Evidence should be " + PNG.getExtension() + " files",
            "Can't open folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void unableToOpenImageDialog(File file) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Unable to open image: " + file.getAbsolutePath(),
            "Can't open image", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantAddTabUnderRegression() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't create a Scenario in the regression tab",
            "Can't add Scenario", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantSaveGeneratedText(IOException e) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't save generated text to file: \n\n" + e.getMessage(),
            "Can't save generated text", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantReadGeneratedTextFile(File generatedTextFile) {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't read from generated text file: " + generatedTextFile.getAbsolutePath(),
            "Can't read file", JOptionPane.ERROR_MESSAGE);
    }

    public static void currentlySavingDialog() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Currently saving, please wait till it is saved to save again",
            "Currently saving", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantCreateConfig() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Unable to create the config!",
            "Can't create config", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantSaveConfig() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Unable to save the config!\n\nPlease go to preferences -> reset config to amend",
            "Can't save config", JOptionPane.ERROR_MESSAGE);
    }

    public static void lookAndFeelChangedSuccessfully() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), """
                Look and feel changed successfully!

                Please reopen the application to see changes""",
            "Look and Feel change", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void configHasBeenReset() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), """
                Config has been reset!

                Please reopen the application to see all changes""",
            "Config reset", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void cantDeleteConfig() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't delete the config! please go and delete it manually at:\n" +
                Config.CONFIG_FOLDER.getAbsolutePath(),
            "Can't delete config", JOptionPane.ERROR_MESSAGE);
    }

    public static String askForTaskName() {
        String ticketNumber = JOptionPane.showInputDialog("Enter new task name please");

        if (nonNull(ticketNumber)) {
            ticketNumber = ticketNumber.trim();
        }

        return ticketNumber;
    }

    public static void cantMoveTasks() {
        JOptionPane.showMessageDialog(FRAME_COMPONENTS.getFrame(), "Can't move the tasks up or down as you have selected multiple.\n" +
                "Please just select one task to move at a time",
            "Can't move tasks", JOptionPane.ERROR_MESSAGE);
    }
}
