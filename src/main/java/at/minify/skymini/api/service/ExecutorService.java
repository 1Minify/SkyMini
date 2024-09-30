package at.minify.skymini.api.service;

import net.minecraft.client.Minecraft;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class ExecutorService {

    public static Executor onThisThread() {
        return command -> {
            Minecraft minecraft = Minecraft.getMinecraft();
            if(minecraft.isCallingFromMinecraftThread()) {
                command.run();
            } else {
                Minecraft.getMinecraft().addScheduledTask(command);
            }
        };
    }

    public static Executor onOtherThread() {
        return command -> {
            Minecraft minecraft = Minecraft.getMinecraft();
            if(minecraft.isCallingFromMinecraftThread()) {
                ForkJoinPool.commonPool().execute(command);
            } else {
                command.run();
            }
        };
    }

}
