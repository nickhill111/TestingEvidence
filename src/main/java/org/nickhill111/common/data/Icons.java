package org.nickhill111.common.data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.nonNull;
import static org.apache.poi.common.usermodel.PictureType.PNG;

public class Icons {
    public static final ImageIcon PASSED_ICON = getImageIconFromResource("PassIcon");
    public static final ImageIcon FAILED_ICON = getImageIconFromResource("FailIcon");
    public static final ImageIcon PREVIEW_ICON = getImageIconFromResource("PreviewIcon");
    public static final ImageIcon REMOVE_ICON = getImageIconFromResource("RemoveIcon");
    public static final ImageIcon SAVE_ICON = getImageIconFromResource("SaveIcon");
    public static final ImageIcon ADD_USER_ICON = getImageIconFromResource("AddUserIcon");
    public static final ImageIcon SCREENSHOT_ICON = getImageIconFromResource("ScreenshotIcon");
    public static final ImageIcon ADD_SCENARIO_ICON = getImageIconFromResource("AddScenarioIcon");
    public static final ImageIcon ADD_ICON = getImageIconFromResource("AddIcon");
    public static final ImageIcon DELETE_ICON = getImageIconFromResource("DeleteIcon");
    public static final ImageIcon UP_ICON = getImageIconFromResource("UpIcon");
    public static final ImageIcon DOWN_ICON = getImageIconFromResource("DownIcon");

    private static ImageIcon getImageIconFromResource(String fileName) {
        InputStream inputStream = Icons.class.getClassLoader().getResourceAsStream("Icons/" + fileName + PNG.getExtension());

        if (nonNull(inputStream)) {
            try {
                return new ImageIcon(ImageIO.read(inputStream));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }
}
