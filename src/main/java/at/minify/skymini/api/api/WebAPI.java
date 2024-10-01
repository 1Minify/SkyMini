package at.minify.skymini.api.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
public class WebAPI {

    private final String response;

    public WebAPI(String path) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(path).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                this.response = response.toString();
            } else {
                throw new RuntimeException("Failed to connect: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public JsonObject getJsonResponse() {
        return new Gson().fromJson(this.getResponse(), JsonObject.class);
    }

}
