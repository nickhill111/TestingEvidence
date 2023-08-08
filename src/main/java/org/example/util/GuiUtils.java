package org.example.util;

import java.awt.*;

public class GuiUtils {

    public static void refreshComponent(Component component) {
        component.revalidate();
        component.repaint();
    }
}
