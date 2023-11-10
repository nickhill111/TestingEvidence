package org.nickhill111.taskManager.data;

import java.time.LocalDate;
import java.util.List;

public record Task(String taskName, String priority, boolean isBlocked, LocalDate dateCreated, List<Comment> comments) {
}
