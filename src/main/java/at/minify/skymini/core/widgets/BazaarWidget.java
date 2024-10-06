package at.minify.skymini.core.widgets;

import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.core.GUI.categories.Display;

public class BazaarWidget extends Widget {

    @Override
    public boolean isEnabled() {
        if(!Display.displayBazaarWidget) {
            return false;
        }
        return super.isEnabled();
    }
}
