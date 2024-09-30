package at.minify.skymini.api.widgets.manager;

import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.GUI.categories.Display;
import io.github.moulberry.moulconfig.ChromaColour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class Gradient {

    private static int getGradientColor(int startColor, int endColor, int steps, int currentStep, int progress) {
        float percent = (float) currentStep / (float) steps;
        float progress1 = (float)progress/100;
        float out = percent+progress1;
        return getcolor(startColor,endColor, out);
    }

    public static int colorshift(int originalColor, float degrees) {
        Color original = new Color(originalColor);

        float[] hsb = Color.RGBtoHSB(original.getRed(), original.getGreen(), original.getBlue(), null);
        float hue = (hsb[0] + degrees / 360.0f) % 1;

        return Color.HSBtoRGB(hue, hsb[1], hsb[2]) & 0x00FFFFFF;
    }

    private static int getcolor(int startColor, int endColor, float percent) {
        if(percent > 1 && percent < 2) { percent = 2-percent; }
        if(percent >= 2 && percent < 3) { percent = percent-2; }
        if(percent >= 3) { percent = 4-percent;}
        int r1 = (startColor >> 16) & 0xFF;
        int g1 = (startColor >> 8) & 0xFF;
        int b1 = startColor & 0xFF;
        int r2 = (endColor >> 16) & 0xFF;
        int g2 = (endColor >> 8) & 0xFF;
        int b2 = endColor & 0xFF;
        int red = (int) (r1 * (1 - percent) + r2 * percent);
        int green = (int) (g1 * (1 - percent) + g2 * percent);
        int blue = (int) (b1 * (1 - percent) + b2 * percent);
        return (red << 16) | (green << 8) | blue;
    }


    private static int getchroma(String text) {
        if(text.contains("chroma:")) {
            for(String word : text.split(" ")) {
                if(word.contains("chroma:")) {
                    return Integer.parseInt(word.replaceAll("chroma:",""));
                }
            }
        }
        return -1;
    }

    public static void renderchroma(String text,int x, int y,int color, int progress) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        boolean bold = false;
        //int chroma = getchroma(text);
        //if(chroma != -1) { text = text.replaceAll("chroma:" + chroma + " ","").replaceAll("&u",""); }
        text = text.replaceAll("§u","");
        if(text.contains("§l")) { text = text.replaceAll("§l",""); bold = true; }
        int currentWidth = 0;
        String newcolor = "";
        String c2 = "";
        int skip = 0;
        char[] chars = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            String c = String.valueOf(chars[i]);
            if(i+1 < text.length()) { c2 = String.valueOf(chars[i+1]); }
            if(String.valueOf(chars[i]).equals("§") && !c2.equals("l") && !c2.equals("u")) { newcolor = "§" + c2; skip = 2; }
            if(skip != 0) { skip--; continue; }
            float value = (float) progress/text.length();
            int gradientColor = colorshift(color,value*(i+1));
            if(bold) {
                wordWidth = fontRenderer.getStringWidth("§l" + c);
                fontRenderer.drawStringWithShadow(newcolor + "§l" + c, x + currentWidth, y, gradientColor);
            } else {
                wordWidth = fontRenderer.getStringWidth(c);
                fontRenderer.drawStringWithShadow(newcolor + c, x + currentWidth, y, gradientColor);
            }
            currentWidth += wordWidth;
        }
    }

    public static void tick(MiniTickEvent event) {
        if(event.second(1)) {
            String chromac = Math.round(255-((float) (Display.chromaspeed-1) /59*255)) + ":0:255:0:255";
            chromacolor = ChromaColour.forLegacyString(chromac);
        }
    }

    public static ChromaColour chromacolor;

    public static int rendergradient(String text, int x, int y, int ani, int length, int color1, int color2) {
        boolean bold = false;
        int chroma = getchroma(text);
        if(chroma != -1) { text = text.replaceAll("chroma:" + chroma + " ",""); }
        if(text.contains("§l")) { text = text.replaceAll("§l",""); bold = true; }
        if(length == 0) { length = text.length(); } else {
            if(length < text.length()) {
                length = text.length();
            }
        }
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int currentWidth = 0;
        char[] chars = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            int gradientColor = getGradientColor(color1, color2, length, i, ani);
            if(chroma != -1) {
                float value = (float) chroma/length;
                gradientColor = colorshift(color1,value*(i+1));
            }

            if(bold) {
                wordWidth = fontRenderer.getStringWidth("§l" + chars[i]);
                fontRenderer.drawStringWithShadow("§l" + String.valueOf(text.charAt(i)), x + currentWidth, y, gradientColor);
            } else {
                wordWidth = fontRenderer.getStringWidth(chars[i] + "");
                fontRenderer.drawStringWithShadow(String.valueOf(text.charAt(i)), x + currentWidth, y, gradientColor);
            }
            currentWidth += wordWidth;
        }
        return currentWidth;
    }
    static int wordWidth = 0;

}
