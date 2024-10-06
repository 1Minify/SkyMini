package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.*;

public class Display {

    @ConfigOption(name = "Custom HUD", desc = "Create a custom HUD Display")
    @ConfigEditorBoolean
    public static boolean displayCustomHUD = false;

    @ConfigOption(name = "Gradient Speed", desc = "Set the speed for the color gradient in GUI's")
    @ConfigEditorSlider(minValue = 1, maxValue = 15, minStep = 1)
    public static int guiSpeed = 5;

    @ConfigOption(name = "Background", desc = "Set the opacity of the sidebar background")
    @ConfigEditorSlider(minValue = 0, maxValue = 100, minStep = 1)
    public static int widgetBGOpacity = 50;

    @ConfigOption(name = "Scoreboard", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion1 = false;

    @ConfigOption(name = "Custom Scoreboard", desc = "Create a custom sidebar")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean customScoreboard = true;

    @ConfigOption(name = "Create Bossbar", desc = "Create a custom bossbar to make the sidebar cleaner")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean customBossBar = true;

    @ConfigOption(name = "Carry Scoreboard", desc = "Show a custom sidebar for §eeman carrys")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean emanScoreboard = false;

    @ConfigOption(name = "Purse & Bits Customization", desc = "")
    @ConfigEditorAccordion(id = 5)
    @ConfigAccordionId(id = 1)
    public boolean accordion5 = false;

    @ConfigOption(name = "Purse Animation", desc = "Making a slide animation after every purse change to the new value")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 5)
    public static boolean purseAnimation = true;

    @ConfigOption(name = "Purse collect coins", desc = "Show collected coins behind the purse display")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 5)
    public static boolean purseCollect = true;

    @ConfigOption(name = "Purse Design", desc = "Customize the Purse Display format\nUse §6%purse% §7for the purse value")
    @ConfigEditorText
    @ConfigAccordionId(id = 5)
    public static String purseFormat = "i%coin%&fPurse: #f3bc30%purse%#ef6806";

    @ConfigOption(name = "Bits Design", desc = "Customize the Bits Display format\nUse §b%bits% §7for the bits value")
    @ConfigEditorText
    @ConfigAccordionId(id = 5)
    public static String bitsFormat = "i%bit%&fBits: #26bce2%bits%#22e479";

    @ConfigOption(name = "Scoreboard Title", desc = "")
    @ConfigEditorAccordion(id = 2)
    @ConfigAccordionId(id = 1)
    public boolean accordion2 = false;

    @ConfigOption(name = "Island Title", desc = "Show a Title with the Island Name with matching colors")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 2)
    public static boolean islandTitle = true;

    @ConfigOption(name = "Custom Title", desc = "Setting §cIsland Title §7overrite on enabled!\n§7Use color codes with '&' example: §d&dtest\n§7Set 2 hexcodes to create a gradient: §3#2a4858text title#c26c1b\n§7Use &u to set in Chroma: &u§4T§6E§aS§2T")
    @ConfigEditorText
    @ConfigAccordionId(id = 2)
    public static String customTitle = "#db42c7&lSKYMINI#955ac9";

    //@ConfigOption(name = "Purse Animation Time", desc = "Set the time until the purse animation end")
    //@ConfigEditorSlider(minValue = 0, maxValue = 20, minStep = 1)
    //@ConfigAccordionId(id = 1)
    public static int purseAnimationTime = 10;

    @ConfigOption(name = "Chroma Settings", desc = "")
    @ConfigEditorAccordion(id = 3)
    public boolean accordion3 = false;

    @ConfigOption(name = "Chroma Speed", desc = "Set the speed for the Chroma effect in seconds")
    @ConfigEditorSlider(minValue = 1, maxValue = 10, minStep = 1)
    @ConfigAccordionId(id = 3)
    public static int chromaSpeed = 5;

    @ConfigOption(name = "Chroma Progress", desc = "Set the progress until the end of a Chroma text")
    @ConfigEditorSlider(minValue = 1, maxValue = 360, minStep = 1)
    @ConfigAccordionId(id = 3)
    public static int chromaProgress = 30;

    //speed:0:11:0:255

    @ConfigOption(name = "GUI's", desc = "")
    @ConfigEditorAccordion(id = 4)
    public boolean accordion4 = false;

    @ConfigOption(name = "Boss Display", desc = "Show your own §eSlayer Bosses §7and §cCrimson Isle §7Bosses")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 4)
    public static boolean displayBossWidget = true;

    @ConfigOption(name = "Bazaar Flip Display", desc = "Show the turnover of Bazaar flipping in a time")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 4)
    public static boolean displayBazaarWidget = true;
}
