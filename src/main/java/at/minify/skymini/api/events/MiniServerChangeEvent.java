package at.minify.skymini.api.events;

import at.minify.skymini.core.data.Server;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MiniServerChangeEvent extends Event {

    public Server server;

    public MiniServerChangeEvent(Server server) {
        this.server = server;
    }

}
