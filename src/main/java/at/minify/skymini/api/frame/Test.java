package at.minify.skymini.api.frame;



public class Test {

    /*public PotentialUpdate potentialUpdate = null;
    public UpdateContext updateContext;
    public CompletableFuture<?> future;


    public void checkUpdate() {
        updateContext = new UpdateContext(
                UpdateSource.githubUpdateSource("1minify", "SkyMini"),
                UpdateTarget.deleteAndSaveInTheSameFolder(this.getClass()),
                CurrentVersion.ofTag(Main.VERSION),
                Main.MODID
        );
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
    }*/

    /*public void loadUpdate() {
        new Thread(() -> {
            try {
                potentialUpdate.prepareUpdate();
                System.out.println("finished");
                potentialUpdate.executePreparedUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage(), e);
            }
        }).start();
    }*/









    /*public CompletableFuture<Void> launchUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.executeUpdate();
                return null;
            } catch (IOException var2) {
                throw new CompletionException(var2);
            }
        });
    }*/

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
