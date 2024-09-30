package at.minify.skymini.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MiniTickEvent extends Event {

    private final int seconds;
    private final int ticks;

    public MiniTickEvent(int seconds, int ticks) {
        this.seconds = seconds;
        this.ticks = ticks;
    }

    public boolean second(int i) {
        return seconds % i == 0 && ticks == 5;
    }

    private void test() {

    }

}
