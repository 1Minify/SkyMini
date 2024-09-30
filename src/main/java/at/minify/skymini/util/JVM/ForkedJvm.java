package at.minify.skymini.util.JVM;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//AUS Skytils ForkedJvm
//C:\Program Files (x86)\Minecraft Launcher\runtime\jre-legacy\windows-x64\jre-legacy\bin\java -cp
//C:\Program Files (x86)\Minecraft Launcher\runtime\jre-legacy\windows-x64\jre-legacy\bin>java -cp "C:/Users/cleme/AppData/Roaming/.minecraft/mods/SkyMini-1.0.jar" at.minify.skymini.util.JVM.ForkedData

public class ForkedJvm /*implements AutoCloseable*/ {
    private static final Logger LOGGER = LogManager.getLogger(ForkedJvm.class);

    public final Process process;

    public ForkedJvm(Class<?> main) throws IOException {
        String mcDir = System.getProperty("user.dir");
        String backupDir = mcDir + "/config/skymini";
        File backup = new File(backupDir, "backup.jar");
        String classpath = backup.getPath();
        /*String classpath;
        CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
        System.out.println(String.valueOf(codeSource));
        if (codeSource == null)
            throw new UnsupportedOperationException("Failed to get CodeSource for Essential stage1 loader");
        URL essentialJarUrl = codeSource.getLocation();
        if (essentialJarUrl == null)
            throw new UnsupportedOperationException("Failed to get location of Essential stage1 loader jar");
        try {
            System.out.println(String.valueOf(essentialJarUrl.toURI()));
            //classpath = Paths.get(essentialJarUrl.toURI()).toAbsolutePath().toString();
            //classpath = essentialJarUrl.toURI().toString();
            String[] classes = essentialJarUrl.toURI().toString().split("!");
            classpath = "\"" + classes[0].replace("jar:file:/","") + "\"";
            System.out.println(classpath);
        } catch (URISyntaxException e) {
            throw new UnsupportedOperationException("Failed to parse " + essentialJarUrl + " as file path:", e);
        }//jar:file:/C:/Users/cleme/AppData/Roaming/.minecraft/mods/SkyMini-1.0.jar!/at/minify/skymini/util/ForkedJvm.class at.minify.skymini.util.JVM.ForkedData*/

        List<String> cmd = new ArrayList<>();
        cmd.add(Paths.get(System.getProperty("java.home"), new String[0])
                .resolve("bin")
                .resolve("java")
                .toAbsolutePath()
                .toString());
        cmd.add("-cp");
        cmd.add(classpath);
        cmd.add(main.getName());
        LOGGER.debug("Starting forked JVM: " + String.join(" ", cmd));
        System.out.println("Starting forked JVM: " + String.join(" ", cmd));
        this.process = (new ProcessBuilder(cmd))
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .start();
    }

    public static void createError(String message) {
        JOptionPane.showMessageDialog(null, message, "SkyMini", 0);
    }

    public void close() {
        this.process.destroy();
    }
}
