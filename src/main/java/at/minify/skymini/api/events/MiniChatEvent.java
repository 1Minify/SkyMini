package at.minify.skymini.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MiniChatEvent extends Event {

    private final String message;

    public MiniChatEvent(String message) {
        this.message = message;
    }

    public String getFullMessage() {
        return message;
    }

    public String getMessage() {
        return message.replaceAll("§[0-9A-Za-z]","");
    }

    public String getWord(String input, int pos) {
        String[] words = input.split("\\s+");
        if (pos > 0 && pos <= words.length) {
            return words[pos - 1];
        } else {
            return null;
        }
    }
}
