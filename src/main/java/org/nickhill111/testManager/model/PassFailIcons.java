package org.nickhill111.testManager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

import java.util.Arrays;

import static org.nickhill111.common.data.Icons.FAILED_ICON;
import static org.nickhill111.common.data.Icons.PASSED_ICON;

@Getter
@AllArgsConstructor
public enum PassFailIcons {
    PASSED(PASSED_ICON, "(/)", "PASSED"),
    FAILED(FAILED_ICON, "(x)", "FAILED");

    private final ImageIcon icon;
    private final String generatedTextValue;
    private final String generatedWordDocumentValue;

    public static PassFailIcons getValueFromIcon(Icon icon) {
        return Arrays.stream(values()).filter(v -> v.getIcon().equals(icon)).findFirst().orElse(null);
    }

    public static PassFailIcons getValueFromGeneratedTextValue(String generatedTextValue) {
        return Arrays.stream(values()).filter(v -> v.getGeneratedTextValue().equals(generatedTextValue)).findFirst().orElse(null);
    }
}
