package org.nickhill111.taskManager.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGHEST("Highest"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    LOWEST("Lowest"),;

    private final String value;

    public static String[] getAllValues() {
        return Arrays.stream(Priority.values()).map(Priority::getValue).toArray(String[]::new);
    }
}
