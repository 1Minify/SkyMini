package at.minify.skymini.api.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ItemAPI {

    private static HashMap<String, String> ItemList = new HashMap<String, String>();

    public static String getid(String name) {
        if(ItemList != null) {
            for (Map.Entry<String, String> entry : ItemList.entrySet()) {
                if(name.equalsIgnoreCase(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public static void loadhypixelitems() {
        Thread updateThread = new Thread(() -> {
            try {
                // URL zur Liste der Items
                URL url = new URL("https://api.hypixel.net/resources/skyblock/items");
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

                    // JSON-String in ein JsonObject umwandeln
                    JsonObject jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);

                    // Items aus dem JsonObject in das Itemjson-Objekt speichern
                    JsonArray items = jsonObject.get("items").getAsJsonArray();
                    for (JsonElement element : items) {
                        JsonObject itemJson = element.getAsJsonObject();
                        String itemName = itemJson.get("name").getAsString();
                        String itemId = itemJson.get("id").getAsString();
                        //System.out.println(itemName + " - " + itemId);
                        ItemList.put(itemName,itemId);

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

}
