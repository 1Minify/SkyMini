package at.minify.skymini.api.widgets.manager;

import net.minecraft.client.Minecraft;

import java.util.List;

public class WidgetManager {

    public static int longest(List<String> all) {
        int length = 0;
        for(String line : all) {
            int i = 0;
            if(line.contains("i%")) {
                String[] split = line.split("%");
                if(split[0].length() > 1) {
                    i = 2;
                }
                if(split.length == 3) {
                    line = line.replace("i%" + split[1] + "%", "");
                    i += 12;
                }
            }
            int linelength = stringlength(line)+i;
            if(length < linelength) {
                length = linelength;
            }
        }
        return length;
    }

    public static int stringlength(String text) {
        text = text.replaceAll("0x[0-9A-Fa-f]{6}","").replaceAll("#[0-9A-Fa-f]{6}","").replaceAll("&","§").replaceAll("chroma:[1-9][0-9]{0,2}","").replaceAll("center:true","");
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

}
