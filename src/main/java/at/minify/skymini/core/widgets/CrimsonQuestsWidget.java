package at.minify.skymini.core.widgets;

import at.minify.skymini.api.annotations.ServerWidget;
import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.core.GUI.categories.Crimson;
import at.minify.skymini.core.data.Server;

@ServerWidget(server = Server.CRIMSON_ISLE)
public class CrimsonQuestsWidget extends Widget {

    @Override
    public boolean isEnabled() {
        return Crimson.displayquests;
    }
}
