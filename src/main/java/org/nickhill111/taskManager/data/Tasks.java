package org.nickhill111.taskManager.data;

import java.util.HashMap;
import java.util.List;

public class Tasks extends HashMap<Integer, Task> {

    public List<String> getAllTaskNames() {
        return values().stream().map(Task::taskName).toList();
    }
}
