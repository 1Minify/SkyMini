package at.minify.skymini.util.JVM;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ForkedData {

    public static void main(String[] args) throws IOException {
        String mcDir = System.getProperty("user.dir");
        String backupDir = mcDir + "/config/skymini";
        File backup = new File(backupDir, "backup.jar");
        Thread delayThread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(backup.exists()) {
                File modsFolder = new File(mcDir + "/mods");
                File[] modFiles = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
                for (File mod : modFiles) {
                    if (mod.getName().contains("SkyMini")) {
                        try {
                            Files.delete(mod.toPath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            Files.copy(backup.toPath(), mod.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }
            }

        });
        delayThread.start();
    }

    public static void createError(String message) {
        //JOptionPane.showInputDialog(message);
        JOptionPane.showMessageDialog(null, message, "SkyMini", 0);
    }


}
