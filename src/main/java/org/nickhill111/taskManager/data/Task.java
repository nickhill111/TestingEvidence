package org.nickhill111.taskManager.data;

import java.time.LocalDate;

public record Task(String taskName, String priority, boolean isBlocked, LocalDate dateCreated, String taskText) {
}
