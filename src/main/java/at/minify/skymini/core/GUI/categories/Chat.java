package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.*;

public class Chat {

    @ConfigOption(name = "Auto Party Warp", desc = "Warps automatically after 5 seconds")
    @ConfigEditorBoolean
    public static boolean autowarp = false;

    @ConfigOption(name = "Party Chat", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion1 = false;

    @ConfigOption(name = "Custom Party Chat", desc = "Show a custom Party chat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean cpchat = true;

    @ConfigOption(name = "Design", desc = "Customize your Party chat message.\n\u00a7d%name% \u00a77is replaced with the name\n\u00a7d%text% \u00a77is replaced with the message")
    @ConfigEditorText
    @ConfigAccordionId(id = 1)
    public static String pchatformat = "&8[&bPChat&8] %name%&7: &f%text%";

    @ConfigOption(name = "Show Rank", desc = "Show in the Chat message the rank in a color")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean pchatrank = true;

    @ConfigOption(name = "Guild Chat", desc = "")
    @ConfigEditorAccordion(id = 2)
    public boolean accordion2 = false;

    @ConfigOption(name = "Custom Guild Chat", desc = "Show a custom Guild chat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 2)
    public static boolean gchat = true;

    @ConfigOption(name = "Design", desc = "Customize your Guild chat message.\n\u00a7d%name% \u00a77is replaced with the name\n\u00a7d%text% \u00a77is replaced with the message")
    @ConfigEditorText
    @ConfigAccordionId(id = 2)
    public static String gchatformat = "&8[&2Guild&8] %name%&7: &f%text%";

    @ConfigOption(name = "Translate", desc = "")
    @ConfigEditorAccordion(id = 3)
    public boolean accordion3 = false;

    @ConfigOption(name = "Party Chat", desc = "Translate Party messages to german")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 3)
    public static boolean transparty = false;

    @ConfigOption(name = "Lobby Chat", desc = "Translate Lobby messages to german")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 3)
    public static boolean translobby = false;

    @ConfigOption(name = "Custom MSGs", desc = "Show custom MSG's")
    @ConfigEditorBoolean
    public static boolean cmsg = true;

    @ConfigOption(name = "Custom Chat", desc = "Test")
    @ConfigEditorBoolean
    public static boolean testchat = false;

}
