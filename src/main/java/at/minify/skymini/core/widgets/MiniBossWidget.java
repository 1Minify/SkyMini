package at.minify.skymini.core.widgets;

import at.minify.skymini.api.annotations.ServerWidget;
import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.core.GUI.categories.Slayer;
import at.minify.skymini.core.data.Server;

@ServerWidget(server = Server.THE_END)
public class MiniBossWidget extends Widget {

    @Override
    public boolean isEnabled() {
        return Slayer.displayMiniBoss;
    }
}
