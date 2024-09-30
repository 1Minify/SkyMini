package at.minify.skymini.api.frame;

import at.minify.skymini.Main;
import at.minify.skymini.api.service.ExecutorService;
import moe.nea.libautoupdate.CurrentVersion;
import moe.nea.libautoupdate.PotentialUpdate;
import moe.nea.libautoupdate.UpdateContext;
import moe.nea.libautoupdate.UpdateSource;
import moe.nea.libautoupdate.UpdateTarget;

import java.util.concurrent.CompletableFuture;

public class Test {

    public static PotentialUpdate potentialUpdate = null;
    public static UpdateContext updateContext;
    public static CompletableFuture<?> future;

    public Test() {
        updateContext = new UpdateContext(
                UpdateSource.githubUpdateSource("1minify", "SkyMini"),
                UpdateTarget.deleteAndSaveInTheSameFolder(this.getClass()),
                CurrentVersion.ofTag(Main.VERSION),
                Main.MODID
        );
    }

    public static void checkUpdate() {
        String updateStream = "full"; //none //beta //full
        future = updateContext.checkUpdate(updateStream)
                .thenAcceptAsync(updateResult -> {
                    System.out.println("Check update completed successfully");
                    potentialUpdate = updateResult;
                    if(updateResult.isUpdateAvailable()) {
                        System.out.println("available");
                        loadUpdate();
                    }
                }, ExecutorService.onThisThread());
    }

    public static void loadUpdate() {
        new Thread(() -> {
            try {
                potentialUpdate.prepareUpdate();
                System.out.println("finished");
                potentialUpdate.executePreparedUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage(), e);
            }
        }).start();
    }

    /*public void checkUpdate1() throws IOException {
        String updateStream = "full"; //none //beta //full
        future = updateContext.checkUpdate(updateStream)
                .thenAcceptAsync(updateResult -> {
                    System.out.println("Check update completed successfully");
                    potentialUpdate = updateResult;
                    if(updateResult.isUpdateAvailable()) {
                        System.out.println("available");
                    }
                }, ExecutorService.onThisThread());
        future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Update download started");
            try {
                return potentialUpdate.prepareUpdate();
            } catch (IOException e) {
                return CompletableFuture.completedFuture(null);;
            }
        }).thenAcceptAsync(updateResult -> {
            potentialUpdate.executePreparedUpdate();
        }, ExecutorService.onThisThread());
    }*/


}
