package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.ConfigAccordionId;
import io.github.moulberry.moulconfig.annotations.ConfigEditorAccordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class Crimson {

    @ConfigOption(name = "Display", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion2 = false;

    @ConfigOption(name = "Quests Display", desc = "Show Crimson daily quests from §dCommunity Center")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean displayQuestWidget = true;

    @ConfigOption(name = "Bosses Display", desc = "Show Crimson daily bosses for §5Reputation")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean displayDailyBossWidget = true;

    @ConfigOption(name = "Show only needed", desc = "Show only needed crimson dailys and bosses")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean displayAll = false;

    @ConfigOption(name = "Kuudra", desc = "")
    @ConfigEditorAccordion(id = 2)
    public boolean accordion3 = false;

    @ConfigOption(name = "Kuudra Display", desc = "Create a Custom Actionbar for Kuudra events")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 2)
    public static boolean displayKuudraWidget = true;

}
