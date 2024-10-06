package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dungeon {

    //@ConfigOption(name = "Dungeon Display", desc = "Show §5Entity count§7, §dKey state §7& §bMilestone §7inside a dungeon ")
    //@ConfigEditorBoolean
    //public static boolean displaydungeon = true;

    @ConfigOption(name = "Custom Party Finder", desc = "Display a custom Party finder menu")
    @ConfigEditorBoolean
    public static boolean customPartyFinder = false;

    @ConfigOption(name = "Clean Dungeon Chat", desc = "Disable useless Dungeon messages")
    @ConfigEditorBoolean
    public static boolean cleanDungeonChat = false;

    @ConfigOption(name = "Auto-Messages", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion1 = false;

    @ConfigOption(name = "Blood-Rush", desc = "Send automatically a message in partychat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean sendBloodRushMessage = false;

    @ConfigOption(name = "Blood-Rush Design", desc = "Customize the blood-rush message.\n\u00a7d%time% \u00a77is replaced with the time\n\u00a7d%keys% \u00a77is replaced with the key amount")
    @ConfigEditorText
    @ConfigAccordionId(id = 1)
    public static String bloodRushMessage = "Blood-Rush finished in %time% ┃ %keys% Keys";

    @ConfigOption(name = "270 Score", desc = "Send automatically a message in partychat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean sendSScoreMessage = false;

    @ConfigOption(name = "270 Score Design", desc = "Customize the 270 Score message.\n\u00a7d%time% \u00a77is replaced with the time\n\u00a7d%needed% \u00a77is replaced with longer time needed for 270")
    @ConfigEditorText
    @ConfigAccordionId(id = 1)
    public static String sScoreMessage = "Reached 270 in %time% ┃ %needed% more than Blood-rush";

    @ConfigOption(name = "300 Score", desc = "Send automatically a message in partychat")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean sendSSScoreMessage = false;

    @ConfigOption(name = "300 Score Design", desc = "Customize the 300 Score message.\n\u00a7d%time% \u00a77is replaced with the time\n\u00a7d%rush% \u00a77is replaced with longer time needed from Blood-Rush to S+\n\u00a7d%270% \u00a77is replaced with longer time needed from S to S+")
    @ConfigEditorText
    @ConfigAccordionId(id = 1)
    public static String ssScoreMessage = "Reached 300 in %time% ┃ %rush% more than Blood-rush ┃ %270% more than S";

    @ConfigOption(name = "Time Format", desc = "#1: 3.7m - 8s\n#2: 3m 40s - 8s")
    @ConfigEditorSlider(minValue = 1, maxValue = 2, minStep = 1)
    @ConfigAccordionId(id = 1)
    public static int bloodRushTimeFormat = 1;



    @ConfigOption(name = "Dungeon Scoreboard", desc = "")
    @ConfigEditorAccordion(id = 3)
    public boolean accordion3 = false;

    @ConfigOption(name = "Scoreboard", desc = "Customize the Dungeon scoreboard")
    @ConfigEditorDraggableList(exampleText = {"§701/01/24 §8m82CD", "§1", "§fKeys: §a0x §7(§c§lNot Found§7)", "§fTime: §b01s", "§fScore: §b121 §7(§9§lC§7) §c+39", "§2", "§fDeaths: §40x", "§fMilestone: §b-", "§3", "§e[A]§b1Minify §a15,181", "§4", "§ewww.hypixel.net"})
    @ConfigAccordionId(id = 3)
    public static List<Integer> scoreboardList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

    @ConfigOption(name = "Scoreboard needed score", desc = "Show needed score for get the next grade")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 3)
    public static boolean DisplayNeededScore = true;

    @ConfigOption(name = "Blood Timer", desc = "Set the countdown timer after blood opened in scoreboard")
    @ConfigEditorSlider(minValue = 0, maxValue = 100, minStep = 1)
    @ConfigAccordionId(id = 3)
    public static int bloodTimerTime = 100;





}
