package at.minify.skymini.api.api;

import at.minify.skymini.core.data.Server;

import java.util.ArrayList;
import java.util.List;

public class ModAPI {

    public List<String> tabListData = new ArrayList<>();
    public List<String> scoreboardData = new ArrayList<>();
    public String currentScoreboard = "null";
    public Server server = Server.NONE;
    public String region = "";
    public String serverId = "";
    public String language = "de";
    public boolean inSkyBlock = false;

}
