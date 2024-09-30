package at.minify.skymini.mixins.GuiChat;

public class GuiManager {

    public static float clamp(float number, float min, float max) {
        return (number < min) ? min : Math.min(number, max);
    }

}
