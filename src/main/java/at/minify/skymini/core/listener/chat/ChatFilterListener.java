package at.minify.skymini.core.listener.chat;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.core.GUI.categories.Chat;
import at.minify.skymini.core.data.Server;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MiniRegistry(server = Server.SKYBLOCK)
public class ChatFilterListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(ClientChatReceivedEvent event) {
        if(Chat.chatFilter.isEmpty()) return;
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        for(String input : Chat.chatFilter) {
            if(message.contains(input)) {
                event.setCanceled(true);
                break;
            }
        }
    }

}
