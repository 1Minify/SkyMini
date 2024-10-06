package at.minify.skymini.core.GUI.categories;

import at.minify.skymini.api.GUI.chatFilter.ConfigEditorChatFiler;
import io.github.moulberry.moulconfig.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    @ConfigOption(name = "Chat Filter", desc = "")
    @ConfigEditorAccordion(id = 0)
    public boolean accordion0 = false;

    @ConfigEditorChatFiler
    @ConfigOption(name = "Hide Chat Messages", desc = "Hide messages from the chat that contain this filters")
    @ConfigAccordionId(id = 0)
    public static List<String> chatFilter = new ArrayList<>();

    @ConfigOption(name = "Auto Party Warp", desc = "Warps automatically after 5 seconds")
    @ConfigEditorBoolean
    public static boolean partyAutoWarp = false;

    @ConfigOption(name = "Custom MSGs", desc = "Show better messages")
    @ConfigEditorBoolean
    public static boolean customMSGs = true;

    @ConfigOption(name = "Party Chat", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion1 = false;

    @ConfigOption(name = "Custom Party Chat", desc = "Show a custom Party chat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean customPartyChat = true;

    @ConfigOption(name = "Design", desc = "Customize your Party chat message.\n\u00a7d%name% \u00a77is replaced with the name\n\u00a7d%text% \u00a77is replaced with the message")
    @ConfigEditorText
    @ConfigAccordionId(id = 1)
    public static String partyChatFormat = "&8[&bPChat&8] %name%&7: &f%text%";

    @ConfigOption(name = "Show Rank", desc = "Show in the Chat message the rank in a color")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean displayPartyRankColor = true;

    @ConfigOption(name = "Guild Chat", desc = "")
    @ConfigEditorAccordion(id = 2)
    public boolean accordion2 = false;

    @ConfigOption(name = "Custom Guild Chat", desc = "Show a custom Guild chat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 2)
    public static boolean customGuildChat = true;

    @ConfigOption(name = "Design", desc = "Customize your Guild chat message.\n\u00a7d%name% \u00a77is replaced with the name\n\u00a7d%text% \u00a77is replaced with the message")
    @ConfigEditorText
    @ConfigAccordionId(id = 2)
    public static String guildChatFormat = "&8[&2Guild&8] %name%&7: &f%text%";

    @ConfigOption(name = "Translate", desc = "")
    @ConfigEditorAccordion(id = 3)
    public boolean accordion3 = false;

    @ConfigOption(name = "Party Chat", desc = "Translate Party messages to german")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 3)
    public static boolean translatePartyChat = false;

    @ConfigOption(name = "Lobby Chat", desc = "Translate Lobby messages to german")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 3)
    public static boolean translateLobbyChat = false;

}
