package at.minify.skymini.core.listener;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.core.data.Server;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@MiniRegistry(server = Server.NONE)
public class DataAPI extends MiniEvent {

    private boolean sended = false;

    public static void sendData(EntityPlayer player) {
        Thread updateThread = new Thread(() -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date currentTime = new Date();
                String formattedTime = dateFormat.format(currentTime);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", player.getName());
                jsonObject.addProperty("version", Main.VERSION);
                jsonObject.addProperty("lastjoin", formattedTime);
                URL url = new URL("http://1minify.com/skymini/send.php" + "?uuid=" + URLEncoder.encode(String.valueOf(player.getUniqueID()), "UTF-8") + "&json=" + URLEncoder.encode(jsonObject.toString()));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        updateThread.start();
    }

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(!sended && api().inSkyBlock && Minecraft.getMinecraft().thePlayer != null) {
            sended = true;
            sendData(Minecraft.getMinecraft().thePlayer);
        }
    }

}
