package org.nickhill111.taskManager.data;

import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;

public class Tasks extends HashMap<Integer, Task> {

    public List<String> getAllTaskNames() {
        return values().stream().map(Task::taskName).toList();
    }

    @Override
    public Task get(Object key) {
        Task task = super.get(key);

        if (isNull(task)) {
            task = new Task(null, null);
        }
        return task;
    }
}
