package at.minify.skymini.api.api;

import at.minify.skymini.core.manager.Chat;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BazaarAPI {

    public static JsonObject bazaarJson = null;

    public static float get(String id, String type) {
        //itemName = itemName.toUpperCase().replace(" ", "_");s
        String itemId = transformHypixelBazaarToItemId(id);
        if(itemId.equals("null")) { return 0.0F; }
        if (bazaarJson != null && bazaarJson.has(itemId)) {
            JsonObject itemInfo = bazaarJson.getAsJsonObject(itemId);
            if(type.contains("sell")) {return itemInfo.get("curr_buy").getAsFloat();}
            if(type.contains("buy")) {return itemInfo.get("curr_sell").getAsFloat();}
            return 0.0F;
        } else {
            return 0.0F;
        }
    }


    int ticks = 0;
    public static boolean updateapi = false;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            ticks++;
            if (ticks >= 600) {ticks = 0;}
            if (ticks == 60 /*&& stats.get("gui.hidden1.bzflip") == null*/) {
                updateBazaar();
            }
            if(updateapi) {
                updateapi = false;
                updateBazaar();
                Chat.send("&aBazaar API updated");
            }
        }
    }

    public void updateBazaar() {
        Thread updateThread = new Thread(() -> {
            try {
                URL url = new URL("https://api.hypixel.net/skyblock/bazaar");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JsonObject jsonObject = new JsonObject();
                    // Verarbeiten Sie hier das JSON-Datenobjekt aus 'response' entsprechend Ihrer Anforderungen.
                    // Zum Beispiel:
                    jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);
                    bazaarJson = new JsonObject();
                    // Hier setzen wir die Verarbeitung aus Ihrem ursprünglichen Code ein
                    JsonObject products = jsonObject.get("products").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : products.entrySet()) {
                        if (entry.getValue().isJsonObject()) {
                            JsonObject productInfo = new JsonObject();

                            JsonObject product = entry.getValue().getAsJsonObject();
                            JsonObject quickStatus = product.get("quick_status").getAsJsonObject();
                            productInfo.addProperty("avg_buy", quickStatus.get("buyPrice").getAsFloat());
                            productInfo.addProperty("avg_sell", quickStatus.get("sellPrice").getAsFloat());

                            for (JsonElement element : product.get("sell_summary").getAsJsonArray()) {
                                if (element.isJsonObject()) {
                                    JsonObject sellSummaryFirst = element.getAsJsonObject();
                                    productInfo.addProperty("curr_sell", sellSummaryFirst.get("pricePerUnit").getAsFloat());
                                    break;
                                }
                            }

                            for (JsonElement element : product.get("buy_summary").getAsJsonArray()) {
                                if (element.isJsonObject()) {
                                    JsonObject sellSummaryFirst = element.getAsJsonObject();
                                    productInfo.addProperty("curr_buy", sellSummaryFirst.get("pricePerUnit").getAsFloat());
                                    break;
                                }
                            }

                            bazaarJson.add(transformHypixelBazaarToItemId(entry.getKey()), productInfo);
                        }
                    }

                } else {
                    // Handle den Fall, wenn die Anfrage fehlschlägt.
                    System.out.println("Fehler beim Abrufen der Daten. Response-Code: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        updateThread.start();
    }

    private static final Pattern BAZAAR_ENCHANTMENT_PATTERN = Pattern.compile("ENCHANTMENT_(\\D*)_(\\d+)");
    public static String transformHypixelBazaarToItemId(String hypixelId) {
        if(hypixelId == null) { return "null"; }
        Matcher matcher = BAZAAR_ENCHANTMENT_PATTERN.matcher(hypixelId);
        if (matcher.matches()) {
            return matcher.group(1) + ";" + matcher.group(2);
        } else {
            return hypixelId.replace(":", "-");
        }
    }

}
