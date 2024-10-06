package at.minify.skymini.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdateExecutor {

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(3000);
        File updated = new File(System.getProperty("user.dir") + "/config/skymini/updater", "updated.jar");
        if(!updated.exists()) return;
        if(args.length == 0) return;
        File current = new File(args[0]).getParentFile();
        if(!current.exists()) return;
        Files.delete(current.toPath());
        Files.copy(updated.toPath(), current.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
