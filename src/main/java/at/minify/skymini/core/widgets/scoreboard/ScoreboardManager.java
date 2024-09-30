package at.minify.skymini.core.widgets.scoreboard;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.data.Server;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service(priority = 1)
@MiniRegistry(server = Server.NONE)
public class ScoreboardManager {

    public List<String> lines = new ArrayList<>();
    public List<String> lines1 = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("#,###");
    private double purse = 0;
    private double bits = 0;

    public String getBits() {
        return df.format(bits).replaceAll("\\.",",");
    }

    public String getPurse() {
        return df.format(purse).replaceAll("\\.",",");
    }

    public String getvalue(List<String> all, String contains) {
        for(String line : all) {
            if(line.contains(contains)) {
                return line;
            }
        }
        return null;
    }

    public void updateBits(List<String> all) {
        for(String line : all) {
            if (!line.contains("Bits:")) {
                continue;
            }
            if(line.contains(".")) {
                String[] split = line.split("\\.");
                bits = Integer.parseInt(split[0].replaceAll("§[0-9a-fA-Fklmnor]", "").replaceAll("[^0-9]", ""));
            } else {
                bits = Integer.parseInt(line.replaceAll("§[0-9a-fA-Fklmnor]", "").replaceAll("[^0-9]", ""));
            }
            return;
        }
    }



    private int purse1 = 0;
    private int purseani = 0;
    private int lastpurse = 0;
    private float anipurse = 0;
    private float anipurse1 = 0;

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if (purseani != 0) {
            //float percent = (float) (purse - lastpurse) / 200.0F;
            float percent = (float) (purse1 - lastpurse) / (Display.pursetime*20);
            anipurse1 += percent;
            //int p1 = (Display.pursetime*20 - purseani) / (Display.pursetime*10) + 1;
            int p1 = (Display.pursetime*20 - purseani) / (Display.pursetime*10) + 1;
            if (purseani % p1 == 0)
                anipurse = anipurse1;
            purseani--;
        }
    }

    public void updatePurse(List<String> all) {
        for(String line : all) {
            if(!line.contains("Purse:")) {
                continue;
            }
            if(purseani == 0) {
                if(line.contains("(")) {
                    return;
                    //return Display.purseformat.replaceAll("%purse%",df.format(purse).replaceAll("\\.",","));
                }
                String moneycount = line.replaceAll("§[0-9a-fA-Fklmnor]", "").replaceAll("[^0-9]", "");
                long longmoney = Long.parseLong(moneycount);
                if(longmoney < 2147483000) {
                    int newmoney = Integer.parseInt(line.replaceAll("§[0-9a-fA-Fklmnor]", "").replaceAll("[^0-9]", ""));
                    if (purse1 > newmoney + 1000 || purse1 < newmoney - 1000) {
                        if (purse1 != 0 && Display.purseani) {
                            purseani = Display.pursetime*20;
                            lastpurse = purse1;
                            anipurse = lastpurse;
                            anipurse1 = anipurse;
                        }
                    }
                    purse1 = newmoney;
                }
                purse = longmoney;
                return;
                //return Display.purseformat.replaceAll("%purse%",df.format(longmoney).replaceAll("\\.",","));
            } else {
                purse = anipurse;
                return;
                //return Display.purseformat.replaceAll("%purse%",df.format(anipurse).replaceAll("\\.",","));
                //return "§fPurse: §6" + df.format(anipurse).replaceAll("\\.",",");
            }
        }
        //return Display.purseformat.replaceAll("%purse%","0");
    }

}
