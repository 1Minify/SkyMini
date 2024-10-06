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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(priority = 1)
@MiniRegistry(server = Server.NONE)
public class ScoreboardManager {

    public List<String> lines = new ArrayList<>();
    public List<String> lines1 = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("#,###");
    private double purse = 0;
    private double bits = 0;
    private String gotPurse = "";
    private String gotBits = "";

    public String getBits() {
        return df.format(bits).replaceAll("\\.",",");
    }

    public String getBits(String format) {
        return format.replaceAll("%bits%", df.format(bits).replaceAll("\\.",",")) + gotBits;
    }

    public String getPurse() {
        return df.format(purse).replaceAll("\\.",",");
    }

    public String getPurse(String format) {
        return format.replaceAll("%purse%", df.format(purse).replaceAll("\\.",",")) + gotPurse;
    }

    public String getvalue(List<String> all, String contains) {
        for(String line : all) {
            if(line.contains(contains)) {
                return line;
            }
        }
        return null;
    }

    public void updateBits(List<String> lines) {
        Pattern pattern = Pattern.compile(".*Bits: §b(.*)");
        for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()) continue;
            String count = matcher.group(1);
            String[] split = count.split(" ");
            if(split.length == 2) {
                count = split[0];
                gotBits = " " + split[1];
            } else {
                gotBits = "";
            }
            count = getCleanLine(count);
            bits = Integer.parseInt(count);
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
            float percent = (float) (purse1 - lastpurse) / (Display.purseAnimationTime *20);
            anipurse1 += percent;
            //int p1 = (Display.pursetime*20 - purseani) / (Display.pursetime*10) + 1;
            int p1 = (Display.purseAnimationTime *20 - purseani) / (Display.purseAnimationTime *10) + 1;
            if (purseani % p1 == 0)
                anipurse = anipurse1;
            purseani--;
        }
    }

    public void updatePurse(List<String> lines) {
        if(purseani != 0) {
            purse = anipurse;
            return;
        }
        Pattern pattern = Pattern.compile(".*(?:Purse|Piggy): §6(.*)");
        for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()) continue;
            String count = matcher.group(1);
            String[] split = count.split(" ");
            if(split.length == 2) {
                count = split[0];
                if(Display.purseCollect) {
                    gotPurse = " " + split[1];
                }
            } else {
                gotPurse = "";
            }
            count = getCleanLine(count);
            long moneyLong = Long.parseLong(count);
            if(moneyLong < Integer.MAX_VALUE) {
                int moneyInt = Integer.parseInt(count);
                if (purse1 > moneyInt + 1000 || purse1 < moneyInt - 1000) {
                    if (purse1 != 0 && Display.purseAnimation) {
                        purseani = Display.purseAnimationTime *20;
                        lastpurse = purse1;
                        anipurse = lastpurse;
                        anipurse1 = anipurse;
                    }
                }
                purse1 = moneyInt;
            }
            purse = moneyLong;
            return;
        }
    }

    private String getCleanLine(String line) {
        return line.replaceAll("§[0-9A-Za-z]", "").replaceAll("[^0-9]", "");
    }

}
