package at.minify.skymini.core.GUI.config;

import at.minify.skymini.core.GUI.categories.*;
import at.minify.skymini.Main;
import io.github.moulberry.moulconfig.annotations.Category;

public class GUIConfig extends io.github.moulberry.moulconfig.Config {
    @Category(name = "GUI Locations", desc = "GUI Settings")
    public GUILocations cat1 = new GUILocations();
    @Category(name = "Display", desc = "Display & informations shown ingame")
    public Display cat2 = new Display();
    @Category(name = "Crimson Isle", desc = "All stuff about Crimson Isle")
    public Crimson cat3 = new Crimson();
    @Category(name = "Garden", desc = "All stuff about Garden")
    public Garden cat5 = new Garden();
    @Category(name = "Dungeon", desc = "All stuff about Dungeons")
    public Dungeon cat6 = new Dungeon();
    @Category(name = "Slayer", desc = "All stuff about Slayers")
    public Slayer cat7 = new Slayer();
    @Category(name = "Bazaar", desc = "All stuff about Bazaar")
    public Bazaar cat9 = new Bazaar();
    @Category(name = "Chat", desc = "All stuff about ingame Chat")
    public Chat cat10 = new Chat();

    /*@Override
    public List<Social> getSocials() {
        return Arrays.asList(Social.forLink("Discord", GuiTextures.SKYMINI, "https://discord.gg/xEDXXtXbvP"));
    }*/



    @Override
    public boolean shouldAutoFocusSearchbar() {
        return true;
    }

    @Override
    public String getTitle() {
        return "\u00A77SkyMini " + Main.VERSION + " by §c1Minify§7, config by §5Moulberry §7and §5nea89";
    }
}
