package org.nickhill111.util;

import java.awt.*;

public class GuiUtils {

    public static void refreshComponent(Component component) {
        component.revalidate();
        component.repaint();
    }
}
