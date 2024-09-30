package at.minify.skymini.core.commands;

import at.minify.skymini.api.api.TranslateAPI;
import at.minify.skymini.api.util.MiniCommand;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.command.ICommandSender;

public class TranslateCommand extends MiniCommand {

    @Override
    public String getCommandName() {
        return "s";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/s";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        StringBuilder text = new StringBuilder();
        for(String word : args) {
            text.append(word).append(" ");
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String text1 = TranslateAPI.callUrlAndParseResult("en", String.valueOf(text));
                    Chat.sendChat(text1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
