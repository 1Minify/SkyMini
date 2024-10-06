package at.minify.skymini.updater;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.WebAPI;
import at.minify.skymini.api.GUI.versionOption.GuiOptionEditorVersion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateManager {

    private static final String configPath = System.getProperty("user.dir") + "/config/skymini/updater";
    private static String currentInstalledVersion = Main.VERSION;
    private static Thread thread = null;
    private static boolean ssl = false;

    public static void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception ignored) {}
    }

    public static void checkUpdate() {
        if(!ssl) {
            ssl = true;
            disableSSL();
        }
        new Thread(() -> {
            WebAPI webAPI = new WebAPI("https://api.github.com/repos/1Minify/SkyMini/releases/latest");
            //WebAPI webAPI = new WebAPI("http://api.github.com/repos/1Minify/SkyMini/releases/latest");
            JsonObject object = webAPI.getJsonResponse();
            if(!object.has("name")) {
                System.out.println("[Error] SkyMini-Updater: can't find object 'name'");
                GuiOptionEditorVersion.resetButtonText("Error 501");
                return;
            }
            if(!object.has("assets")) {
                System.out.println("[Error] SkyMini-Updater: can't find object 'asses'");
                GuiOptionEditorVersion.resetButtonText("Error 502");
                return;
            }
            String version = object.get("name").getAsString();
            if(version.equalsIgnoreCase(currentInstalledVersion)) {
                System.out.println("[Error] SkyMini-Updater: mod is up-to-date");
                GuiOptionEditorVersion.resetButtonText("Restart Game to load Update");
                return;
            }
            JsonArray assetsArray = object.get("assets").getAsJsonArray();
            String downloadUrl = null;
            for (int i = 0; i < assetsArray.size(); i++) {
                JsonObject asset = assetsArray.get(i).getAsJsonObject();
                if(asset.has("browser_download_url")) {
                    downloadUrl = asset.get("browser_download_url").getAsString();
                    break;
                }
            }
            if(downloadUrl == null) {
                System.out.println("[Error] SkyMini-Updater: can't find the download-url");
                GuiOptionEditorVersion.resetButtonText("Error 504");
                return;
            }
            File file = new File(configPath);
            if(!file.exists()) {
                file.mkdirs();
            }

            GuiOptionEditorVersion.text = "§8" + Main.VERSION + "➜§a" + version;
            GuiOptionEditorVersion.buttonText = "Downloading Update...";
            copyBackup(downloadUrl, version);
        }).start();
    }

    public static void downloadFile(String downloadUrl, File position) {
        try {
            URL url = new URL(downloadUrl);
            InputStream from = url.openStream();
            FileOutputStream to = new FileOutputStream(position);
            try {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while((bytesRead = from.read(buffer)) != -1) {
                    to.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                to.close();
                from.close();
                System.out.println("Error by installing " + position.getName() + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
            to.close();
            from.close();
            System.out.println("Installed " + position.getName() + " successfully!");
        } catch (IOException e) {
            System.out.println("Error by installing " + position.getName() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void copyBackup(String downloadUrl, String version) {
        File updater = getUpdaterFile();
        if(!updater.exists()) {
            downloadFile("https://github.com/1Minify/SkyMini/releases/download/1.0/updater.jar", getUpdaterFile());
            //InputStream from = Main.class.getResourceAsStream("/updater.jar");
        }
        try {
            File tempModFile = new File(configPath, "updated.jar");
            URL modDownloadUrl = new URL(downloadUrl);
            InputStream downloadStream = modDownloadUrl.openStream();
            int fileSize = modDownloadUrl.openConnection().getContentLength();
            int totalBytesRead = 0;
            byte[] buffer = new byte[1024];
            int bytesReadThisTime;
            int progress;
            try (OutputStream out = Files.newOutputStream(tempModFile.toPath())) {
                while ((bytesReadThisTime = downloadStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesReadThisTime);
                    totalBytesRead += bytesReadThisTime;
                    progress = (int) ((totalBytesRead / (double) fileSize) * 100);
                    GuiOptionEditorVersion.buttonText = "Installing - " + progress + "%";
                    //info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &afound! installing... &8[&e" + progress + "%&8]";
                }
            }
            progress = 100;
            //info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &ainstalled!";
            currentInstalledVersion = version;
            System.out.println("installed updated.jar successfully!");
            GuiOptionEditorVersion.resetButtonText("Installed successfully");
            loadUpdate();
        } catch (IOException e) {
            System.out.println("Error by installing updated.jar: " + e.getMessage());
            GuiOptionEditorVersion.resetButtonText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadUpdate() {

        if(thread != null) {
            Runtime.getRuntime().removeShutdownHook(thread);
        }
        thread = new Thread(() -> {
            try {
                createProcess();
            } catch (Exception ignored) {
            }
        });
        Runtime.getRuntime().addShutdownHook(thread);
    }

    public static File getNewestVersionFile() {
        return new File(configPath, "updated.jar");
    }

    public static File getUpdaterFile() {
        return new File(configPath, "updater.jar");
    }

    public static void createProcess() throws IOException, URISyntaxException {
        File file = getNewestVersionFile();
        if(!file.exists()) {
            System.out.println("updater file is null");
            return;
        }

        boolean isWindows = System.getProperty("os.name", "").startsWith("Windows");
        File javaBinary = new File(System.getProperty("java.home"), "bin/java" + (isWindows ? ".exe" : ""));
        List<String> args = new ArrayList<>();
        /*args.add(javaBinary.getAbsolutePath()); // C:\Program Files (x86)\Minecraft Launcher\runtime\jre-legacy\windows-x64\jre-legacy\bin\java.exe
        args.add("-jar"); // -jar
        args.add(file.getPath()); // C:\Users\cleme\AppData\Roaming\.minecraft\config\skymini \ updated.jar
        args.add(UpdateExecutor.class.getName()); // at.minify.skymini.updater.UpdateExecutor
        args.add(getCurrentFile(Main.class).getPath()); // C:\Users\cleme\AppData\Roaming\.minecraft\mods\SkyMini.jar*/

        args.add(javaBinary.getAbsolutePath());
        args.add("-jar");
        args.add(getUpdaterFile().getPath());
        args.add(getCurrentFile(Main.class).getPath());
        args.add(getNewestVersionFile().getPath());

        System.out.println(Arrays.toString(args.toArray(new String[0])));
        System.out.println("Starting updater: " + String.join(" ", args));
        Runtime.getRuntime().exec(args.toArray(new String[0]));
    }

    public static File getCurrentFile(Class<?> clazz) {
        try {
            URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
            if(location == null) return null;
            String path = location.toString();
            path = path.split("!", 2)[0];
            if (path.startsWith("jar:")) {
                path = path.substring(4);
            }
            return new File(new URI(path));
        } catch (URISyntaxException var4) {
            var4.printStackTrace();
            return null;
        }
    }

}
