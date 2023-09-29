package org.nickhill111.util;

import static java.util.Objects.nonNull;
import static org.apache.poi.common.usermodel.PictureType.PNG;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.Users;

public class DialogUtils {
    static FrameComponents frameComponents = FrameComponents.getInstance();

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
        if (frameComponents.isActiveFolder()) {
            JOptionPane.showMessageDialog(null, "Cannot change ticket number after saving!",
                "Unable to change ticket number", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String ticketNumber = JOptionPane.showInputDialog("Enter ticket number please");

        ticketNumber = checkValidFileName(ticketNumber);

        if (nonNull(ticketNumber)) {
            frameComponents.getFrame().setTitle(ticketNumber);
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
            Users users = frameComponents.getUsers();

            if (users.doesUserExist(userType)) {
                JOptionPane.showMessageDialog(null, "User type " + userType + " already exists!",
                    "User type already exists", JOptionPane.ERROR_MESSAGE);
                return askForUserType();
            }
        }

        return userType;
    }

    public static String checkValidFileName(String name) {
        if (nonNull(name) && !name.isEmpty()) {
            name = name.trim();
            File file = new File(name);
            boolean created;

            try {
                created = file.createNewFile();
                if (created && file.delete()) {
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
                + frameComponents.getActiveFolder()
                + "\n\nPlease use \"Save As\" in the file menu or manually delete the folder.",
            "Cannot delete active folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantOpenFolder() {
        JOptionPane.showMessageDialog(null, "Can't open evidence from folder! Evidence should be " + PNG.getExtension() + " files",
            "Can't open folder", JOptionPane.ERROR_MESSAGE);
    }

    public static void unableToOpenImageDialog(File file) {
        JOptionPane.showMessageDialog(null, "Unable to open image: " + file.getAbsolutePath(),
            "Can't open image", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantAddTabUnderRegression() {
        JOptionPane.showMessageDialog(null, "Cant create a Scenario in the regression tab",
            "Can't add Scenario", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantSaveGeneratedText(IOException e) {
        JOptionPane.showMessageDialog(null, "Cant save generated text to file: \n\n" + e.getMessage(),
            "Can't save generated text", JOptionPane.ERROR_MESSAGE);
    }

    public static void cantReadGeneratedTextFile(File generatedTextFile) {
        JOptionPane.showMessageDialog(null, "Cant read from generated text file: " + generatedTextFile.getAbsolutePath(),
            "Can't read file", JOptionPane.ERROR_MESSAGE);
    }

    public static void currentlySavingDialog() {
        JOptionPane.showMessageDialog(null, "Currently saving, please wait till it is saved to save again",
            "Currently saving", JOptionPane.ERROR_MESSAGE);
    }
}
