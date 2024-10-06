package at.minify.skymini.core.widgets;

import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.core.GUI.categories.Display;

public class BossWidget extends Widget {

    @Override
    public boolean isEnabled() {
        if(!Display.displayBossWidget) {
            return false;
        }
        return super.isEnabled();
    }
}
