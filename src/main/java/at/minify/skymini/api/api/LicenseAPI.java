package at.minify.skymini.api.api;

import at.minify.skymini.Main;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.widgets.manager.WidgetManager;
import at.minify.skymini.core.GUI.functions;
import at.minify.skymini.core.manager.Chat;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static at.minify.skymini.core.widgets.util.WidgetExecutor.drawbackground;

public class LicenseAPI {

    static String api = "http://1minify.com/skymini/api.js";
    static String download = "http://1minify.com/skymini/SkyMini-1.0.jar";
    public static boolean hasversion = false;
    public static String sendinfo = "";
    static String jsonResponse = "";
    public static boolean haslicense = false;
    static String newest1 = "";

    private static List<String> licenses = new ArrayList<>();
    private static List<String> versions = new ArrayList<>();
    static String newest = "";
    static String changelog = "";

    public static void senddata(EntityPlayer player) {
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

    public static void loaddata(String type) {
        Thread updateThread = new Thread(() -> {
            try {
                URL url = new URL(api);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                conn.disconnect();
                jsonResponse = response.toString();
                changelog = getlist(jsonResponse, "changelog: [");
                licenses = Arrays.asList(getlist(jsonResponse, "licenses: [").split(","));
                versions = Arrays.asList(getlist(jsonResponse, "versions: [").split(","));
                newest = versions.get(versions.size() - 1);
                if(type.contains("update")) {
                    if(!newest1.equals(newest)) {
                        if (!newest.equals(Main.VERSION)) {
                            sendinfo = "&7Found an Update! &aVersion &8" + Main.VERSION + "➜&a" + newest + " &8- &7Installing...";
                            newest1 = newest;
                            copybackup();
                        }
                    }
                }
                if(type.contains("load")) {
                    newest1 = versions.get(versions.size()-1);
                    haslicense = false;
                    senddata(Minecraft.getMinecraft().thePlayer);
                    for (String name : licenses) {
                        if (Minecraft.getMinecraft().thePlayer.getName().equals(name)) {
                            haslicense = true;
                        }
                        if (Minecraft.getMinecraft().thePlayer.getUniqueID().equals(name)) {
                            haslicense = true;
                        }
                        if(name.equals("all")) {
                            haslicense = true;
                        }
                    }
                    for (String ver : versions) {
                        if (Main.VERSION.equals(ver)) {
                            hasversion = true;
                            break;
                        }
                    }
                    if (hasversion & haslicense) {
                        Main.enableMod();
                        try {
                            String mcDir = System.getProperty("user.dir");
                            String backupDir = mcDir + "/config/skymini";
                            File backup = new File(backupDir, "backup.jar");
                            if(backup.exists()) {
                                Files.delete(backup.toPath());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (!newest.equals(Main.VERSION) && haslicense && hasversion) {
                        sendinfo = "&7Found an Update! &aVersion &8" + Main.VERSION + "➜&a" + newest + " &8- &7Installing...";
                        copybackup();
                    }
                    if (!haslicense) { sendinfo = "&cYour account doesn't has a license for this Mod!"; }
                    if (haslicense & !hasversion) {
                        sendinfo = "&8&m----------&d SkyMini Info &8&m----------\n&8- &cYou are using an invalid version of SkyMini!\n&7Installing update...\n&8&m----------&d SkyMini Info &8&m----------";
                        copybackup();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(type.contains("load")) {
                    sendinfo = "Couldn't load the Mod! No connection to Data-API";
                }
            }
        });
        updateThread.start();
    }

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(event.second(120) && Minecraft.getMinecraft().thePlayer != null && Main.getAPI().inSkyBlock) {
            loaddata("update");
        }
    }

    boolean loadmc = false;
    public static boolean updatemod = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            if(!sendinfo.isEmpty()) {
                if(sendinfo.contains("SkyMini Info")) {
                    String coloredInput = sendinfo.replace("&", "\u00a7");
                    IChatComponent textComponent = new ChatComponentText(coloredInput);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
                } else {
                    Chat.send(sendinfo);
                    updatemod = true;
                }
                sendinfo = "";
            }
            if(!loadmc) {
                loadmc = true;
                loaddata("load");
            }
        }
    }

    public static void copybackup() {
        try {
            String versionUrl = download;
            String currentDir = System.getProperty("user.dir");
            String modsDir = currentDir + File.separator + "config/skymini";
            File tempModFile = new File(modsDir, "backup.jar");
            URL modDownloadUrl = new URL(versionUrl);
            InputStream downloadStream = modDownloadUrl.openStream();
            int fileSize = modDownloadUrl.openConnection().getContentLength();
            int totalBytesRead = 0;
            byte[] buffer = new byte[1024];
            int bytesReadThisTime;
            try (OutputStream out = new FileOutputStream(tempModFile)) {
                while ((bytesReadThisTime = downloadStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesReadThisTime);
                    totalBytesRead += bytesReadThisTime;
                    progress = (int) ((totalBytesRead / (double) fileSize) * 100);
                    info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &afound! installing... &8[&e" + progress + "%&8]";
                }
            }
            progress = 100;
            infotime = 5;
            info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &ainstalled!";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void copybackup() {
        try {
            String versionUrl = download;
            String currentDir = System.getProperty("user.dir");
            String modsDir = currentDir + File.separator + "config\\skymini";
            File tempModFile = new File(modsDir, "backup.jar");
            URL modDownloadUrl = new URL(versionUrl);
            try (InputStream in = modDownloadUrl.openStream()) {
                Files.copy(in, tempModFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            sendinfo = "&aUpdate installed!";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public static String info = "";
    public static int infotime = -1;
    static int progress = 0;

    int tick = 0;


    @SubscribeEvent
    public void s(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        tick++;
        if(tick >= 20) {
            tick = 0;
            if(infotime > 0) { infotime--; }
            if(infotime == 0) { infotime = -1; info = ""; }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void render(RenderGameOverlayEvent.Text event) {
        if(!info.isEmpty()) {
            String info1 = Chat.colored(info);
            if(infotime != -1) { info1 = info1 + " §7(" + infotime + ")"; }
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            int length = WidgetManager.stringlength(info1);
            int x = functions.setx(50) - (length / 2);
            int y = 7;
            int fontheight = fontRenderer.FONT_HEIGHT;
            drawbackground(x, x + length, y, y + fontheight);
            fontRenderer.drawStringWithShadow(info1, x, y, 0xFFFFFF);
        }
    }

    public static String getlist(String text, String startWord) {
        int startIndex = text.indexOf(startWord);
        int endIndex = text.indexOf("]", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            return text.substring(startIndex + startWord.length(), endIndex).trim().replace("\"", "").replace(" ","");
        }
        return "";
    }

}
