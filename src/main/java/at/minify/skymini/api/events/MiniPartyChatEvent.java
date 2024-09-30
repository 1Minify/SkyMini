package at.minify.skymini.api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.Event;

@AllArgsConstructor
@Getter
public class MiniPartyChatEvent extends Event {

    private final String name;
    private final String message;

    public String getWord(String input, int pos) {
        String[] words = input.split("\\s+");
        if (pos > 0 && pos <= words.length) {
            return words[pos - 1];
        } else {
            return null;
        }
    }

}
