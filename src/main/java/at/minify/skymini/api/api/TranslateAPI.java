package at.minify.skymini.api.api;

import at.minify.skymini.Main;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateAPI {

    public static void send(String prefix, String text, String suffix) {
        Thread thread = new Thread(() -> {
            try {
                String text1 = callUrlAndParseResult(Main.getAPI().language, text);
                String input1 = prefix + text1 + suffix;
                String coloredInput = input1.replace("&", "\u00a7");
                IChatComponent textComponent = new ChatComponentText(coloredInput);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


    public static String readURL(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        String response = IOUtils.toString(con.getInputStream(), "UTF-8");
        return response;
    }

    public static String callUrlAndParseResult(String langTo, String text) throws Exception {
        String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + langTo + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + URLEncoder.encode(text, "UTF-8");
        String s = readURL(url);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(s, JsonArray.class);
        jsonArray = jsonArray.get(0).getAsJsonArray();
        jsonArray = jsonArray.get(0).getAsJsonArray();
        JsonElement jsonElement = jsonArray.get(0);
        String translation = jsonElement.getAsString().trim();
        if (translation.startsWith("\"") && translation.endsWith("\"")) {
            translation = translation.substring(1, translation.length() - 1);
        }
        return translation;
    }

}
