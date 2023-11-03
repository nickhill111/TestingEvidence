package org.nickhill111.taskManager.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tasks extends LinkedList<Task> {
}
