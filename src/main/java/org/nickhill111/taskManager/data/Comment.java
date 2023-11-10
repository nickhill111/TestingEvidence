package org.nickhill111.taskManager.data;

public record Comment(String text, String timeCreated) {

    @Override
    public String toString() {
        return text() + " " + timeCreated();
    }
}
