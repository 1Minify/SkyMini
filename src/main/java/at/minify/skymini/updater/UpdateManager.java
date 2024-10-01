package at.minify.skymini.updater;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.WebAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class UpdateManager {

    private static final String configPath = System.getProperty("user.dir") + "/config/skymini";
    private static String currentInstalledVersion = Main.VERSION;
    private static Thread thread = null;

    public static void checkUpdate() {
        new Thread(() -> {
            WebAPI webAPI = new WebAPI("https://api.github.com/repos/1Minify/SkyMini/releases/latest");
            //WebAPI webAPI = new WebAPI("http://api.github.com/repos/1Minify/SkyMini/releases/latest");
            System.out.println(webAPI.getResponse());
            JsonObject object = webAPI.getJsonResponse();
            if(!object.has("name")) {
                System.out.println("[Error] SkyMini-Updater: can't find object 'name'");
                return;
            }
            if(!object.has("assets")) {
                System.out.println("[Error] SkyMini-Updater: can't find object 'asses'");
                return;
            }
            String version = object.get("name").getAsString();
            if(version.equalsIgnoreCase(currentInstalledVersion)) {
                System.out.println("[Error] SkyMini-Updater: mod is up-to-date");
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
                return;
            }
            copyBackup(downloadUrl, version);
        }).start();
    }

    public static void copyBackup(String downloadUrl, String version) {
        try {
            File tempModFile = new File(configPath, "updated.jar");
            URL modDownloadUrl = new URL(downloadUrl.replace("https", "http"));
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
                    //info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &afound! installing... &8[&e" + progress + "%&8]";
                }
            }
            progress = 100;
            //info = "&aUpdate &8" + Main.VERSION + "➜&2" + newest + " &ainstalled!";
            currentInstalledVersion = version;
            loadUpdate();
        } catch (IOException e) {
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

    public static File getUpdatedFile() {
        return new File(System.getProperty("user.dir") + "/config/skymini", "updated.jar");
    }

    public static void createProcess() throws IOException, URISyntaxException {
        File file = getUpdatedFile();
        if(!file.exists()) {
            System.out.println("updater file is null");
            return;
        }

        boolean isWindows = System.getProperty("os.name", "").startsWith("Windows");
        File javaBinary = new File(System.getProperty("java.home"), "bin/java" + (isWindows ? ".exe" : ""));
        List<String> args = new ArrayList<>();
        args.add(javaBinary.getAbsolutePath());
        args.add("-jar");
        args.add(file.getPath());
        args.add(UpdateExecutor.class.getName());
        args.add(getCurrentVersionPath());
        System.out.println("Running post updater using: " + String.join(" ", args));
        Runtime.getRuntime().exec(args.toArray(new String[0]));


        /*this.process = (new ProcessBuilder(args))
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .start();*/
    }

    private static String getCurrentVersionPath() throws URISyntaxException {
        return UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        //return new File(path).getParentFile();
    }

}
