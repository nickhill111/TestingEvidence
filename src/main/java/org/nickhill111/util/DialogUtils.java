package org.nickhill111.util;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.nickhill111.data.Settings;
import org.nickhill111.gui.UserTabbedPane;

public class DialogUtils {
    static Settings settings = Settings.getInstance();

    public static void cantSaveDialog(Exception e) {
        JOptionPane.showMessageDialog(null, "Cannot save screenshots!\n\n" + e.getMessage(),
            "Cannot save screenshots", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantTakeScreenshotDialog(Exception e) {
        JOptionPane.showMessageDialog(null, "Cannot take screenshot!\n\n" + e.getMessage(),
            "Cannot take screenshot", JOptionPane.ERROR_MESSAGE);
    }

    public static void screenshotsSavedDialog(File folder) {
        String folderPath = folder.getPath();
        int dialogButton = JOptionPane.showConfirmDialog(null,
            "Screenshots saved to: " + folderPath + "\n\n Would you like to open the folder?",
            "Screenshots Saved", JOptionPane.YES_NO_OPTION);

        if (dialogButton == JOptionPane.YES_OPTION) {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Cannot open " + folderPath + ":\n\n" + e.getMessage(),
                    "Cannot open folder", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static String askForTicketNumber() {
        if (settings.isActivefolder()) {
            JOptionPane.showMessageDialog(null, "Cannot change ticket number after saving!",
                "Unable to change ticket number", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String ticketNumber = JOptionPane.showInputDialog("Enter ticket number please");

        ticketNumber = checkValidFileName(ticketNumber);

        if (nonNull(ticketNumber)) {
            settings.getFrame().setTitle(ticketNumber);
        }

        return ticketNumber;
    }

    public static String askForUserType() {
        String userType = JOptionPane.showInputDialog("Enter user type please");

        if (isNull(checkValidFileName(userType))) {
            return null;
        }
        
        return validateUserType(userType);
    }

    private static String validateUserType(String userType) {
        UserTabbedPane userTabbedPane = settings.getUserTabbedPane();

        if (userTabbedPane.doesUserExist(userType)) {
            JOptionPane.showMessageDialog(null, "User type " + userType + " already exists!",
                "User type already exists", JOptionPane.ERROR_MESSAGE);
            return askForUserType();
        }

        return userType;
    }

    private static String checkValidFileName(String name) {
        if (nonNull(name) && !name.isEmpty()) {
            File file = new File(name);
            boolean created;

            try {
                created = file.createNewFile();
                if (created) {
                    file.delete();
                    return name.replace("_", " ");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, name + " is not a valid name!",
                    "Invalid name", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null;
    }

    public static void regressionTestTabAlreadyExists() {
        JOptionPane.showMessageDialog(null, "Regression test tab already exists!",
            "Regression test tab exists", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantDeleteActiveFolder() {
        JOptionPane.showMessageDialog(null,
            "Cannot delete active folder: "
                + settings.getActiveFolder()
                + "\n\nPlease use \"Save As\" in the file menu or manually delete the folder.",
            "Cannot delete active folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantOpenFolder() {
        JOptionPane.showMessageDialog(null, "Can't open evidence from folder! Evidence should be .png files",
            "Can't open folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void unableToOpenImageDialog(File file) {
        JOptionPane.showMessageDialog(null, "Unable to open image: " + file.getAbsolutePath(),
            "Can't open image", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantAddTabUnderRegression() {
        JOptionPane.showMessageDialog(null, "Cant create an AC in the regression tab",
            "Can't add AC", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantSaveGeneratedText(IOException e) {
        JOptionPane.showMessageDialog(null, "Cant save generated text to file: \n\n" + e.getMessage(),
            "Can't save generated text", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantReadGeneratedTextFile(File generatedTextFile) {
        JOptionPane.showMessageDialog(null, "Cant read from generated text file: " + generatedTextFile.getAbsolutePath(),
            "Can't read file", JOptionPane.ERROR_MESSAGE);
    }
}
