package at.minify.skymini.core.widgets.manager;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.api.BazaarAPI;
import at.minify.skymini.api.api.ItemAPI;
import at.minify.skymini.api.events.MiniClickGUIEvent;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.core.GUI.categories.Bazaar;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.BazaarWidget;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.minecraft.util.EnumChatFormatting.getTextWithoutFormattingCodes;

@MiniRegistry
public class BazaarWidgetManager {

    BazaarWidget bazaarWidget = WidgetService.getWidget(BazaarWidget.class);

    public int value = 0;
    public int countdown = 0;
    String item = "";
    public Map<String, Integer> itemcount = new HashMap<String, Integer>();
    public Map<String, String> itemdisplay = new HashMap<String, String>();
    public Map<String, Integer> itemmoney = new HashMap<String, Integer>();
    public Map<String, Float> buyoffer = new HashMap<String, Float>();
    public Map<String, Float> selloffer = new HashMap<String, Float>();
    public Map<String, Integer> buyamount = new HashMap<String, Integer>();
    public Map<String, Integer> sellamount = new HashMap<String, Integer>();
    public Map<String, String> itemid = new HashMap<String, String>();
    public List<String> buyfilled = new ArrayList<>();
    public List<String> sellfilled = new ArrayList<>();

    public void resetBazaarData() {
        itemcount.clear();
        itemdisplay.clear();
        itemmoney.clear();
        buyoffer.clear();
        selloffer.clear();
        buyamount.clear();
        sellamount.clear();
        buyfilled.clear();
        sellfilled.clear();
        itemid.clear();
        countdown = 2;
        timer = 0;
        value = 0;
        Chat.send("&7You resetted the Bazaar data.");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if(message.contains("[Bazaar]")) {
            if(message.contains("Sell Offer Setup") || message.contains("Buy Order Setup")) {
                item = getitemname(message);
                if (itemdisplay.get(item) == null) {itemdisplay.put(item, "");}
                showbz();
            }
            if(message.contains("from selling") || message.contains("bought for")) {
                showbz();
                item = getitemname(message);
                if (itemdisplay.get(item) == null) {itemdisplay.put(item, "");}
                int amount = getitemcount(message);
                float count = getamount(message, "coins");
                int amount1 = 0;
                if (message.contains("from selling") && message.contains("Claimed")) {
                    if(sellamount.get(item) != null) {sellamount.put(item, sellamount.get(item) - amount);}
                    value = value + Math.round(count);
                    if (itemmoney.get(item) != null) {
                        amount1 = itemmoney.get(item);
                    }
                    itemmoney.put(item, amount1 + Math.round(count));
                } else if (message.contains("bought for") && message.contains("Claimed")) {
                    if(buyamount.get(item) != null) {buyamount.put(item, buyamount.get(item) - amount);}
                    value = value - Math.round(count);
                    if (itemmoney.get(item) != null) {
                        amount1 = itemmoney.get(item);
                    }
                    itemmoney.put(item, amount1 - Math.round(count));
                    if (itemcount.get(item) != null) {
                        amount = amount + itemcount.get(item);
                    }
                    itemcount.put(item, amount);
                }
            }
            if(message.contains("from selling") || message.contains("bought for") || message.contains("Sell Offer Setup") || message.contains("Buy Order Setup")) {
                for (Map.Entry<String, String> entry : itemdisplay.entrySet()) {
                    String name = entry.getKey();
                    int icount = itemcount.getOrDefault(name, 0);
                    int imoney = itemmoney.getOrDefault(name, 0);
                    String imoney1 = new DecimalFormat("#,##0").format(imoney);
                    String icount1 = new DecimalFormat("#,##0").format(icount);
                    if(Bazaar.displayItemCoins) {
                        itemdisplay.put(name, "\n&a" + icount1 + "x &70xececec" + name/*.replaceAll("Enchanted ","&lE &r&7")*/ + /*" &8\u00BB*/"0x9b9b9b &6" + Chat.formatNumber(imoney));
                    } else {
                        itemdisplay.put(name, "\n&a" + icount1 + "x &70xececec" + name/*.replaceAll("Enchanted ","&lE &r&7")*/ + "0x9b9b9b");
                    }
                }
            }
            if(message.contains("Buy Order") && message.contains("was filled")) {
                item = getitemname(message);
                buyfilled.add(item);
            }
            if(message.contains("Sell Offer") && message.contains("was filled")) {
                item = getitemname(message);
                sellfilled.add(item);
            }
            if(message.contains("cancelling Sell Offer")) {
                item = getitemname(message);
                sellamount.remove(item);
                selloffer.remove(item);
                sellfilled.remove(item);
            }
        }
    }
    boolean update = false;
    int ticks = 0;
    public int timer = 0;
    public String lastguiitem = "";

    @SubscribeEvent
    public void click(MiniClickGUIEvent event) {
        String lore = getTextWithoutFormattingCodes(event.getLore().replace(",",""));
        if(event.getName().contains("BUY ")) {
            lastguiitem = getTextWithoutFormattingCodes(event.getName().replaceAll("BUY ",""));
        }
        if(event.getName().contains("Cancel Order") && lore.contains("missing items")) {
            buyamount.remove(lastguiitem);
            buyoffer.remove(lastguiitem);
            buyfilled.remove(lastguiitem);
        }
        if(lore.contains("Bazaar") && lore.contains("Click to submit")) {
            showbz();
            if(event.getName().contains("Buy Order")) {
                float unit = Float.parseFloat(getbetween(lore,"unit:","coins").replace(",",""));
                String item = getitemname(lore);
                int amount = getitemcount(lore);
                //buyamount.merge(item, amount, Integer::sum);
                buyamount.put(item,amount);
                buyoffer.put(item,unit);
            }
            if(event.getName().contains("Sell Offer")) {
                float unit = Float.parseFloat(getbetween(lore,"unit:","coins").replace(",",""));
                String item = getitemname(lore);
                int amount = getitemcount(lore);
                //sellamount.merge(item, amount, Integer::sum);
                sellamount.put(item,amount);
                selloffer.put(item,unit);
            }
        }
    }
    public void showbz() {
        //stats.remove("gui.hidden1.bzflip");
        bazaarWidget.setEnabled(true);
        countdown = 60;
        update = true;
    }


    String ani = "&e";
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            ticks++;
            if (ticks >= 200) {
                ticks = 0;
            }
            if (ticks % 20 == 0 && Display.displayBazaarWidget) {
                if(ani.equals("&e")) { ani = "&f"; } else { ani = "&e"; }
                if(Bazaar.displayAlways) {
                    countdown = 2;
                    update = true;
                    //stats.remove("gui.hidden1.bzflip");
                    bazaarWidget.setEnabled(true);
                }
                if(countdown > 0) {
                    countdown--;
                    timer++;
                    //out = new DecimalFormat("#,##0").format(value);
                    float time = (float) timer / 60;
                    String ftime = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US)).format(time);
                    StringBuilder text = new StringBuilder();
                    for (Map.Entry<String, String> entry : itemdisplay.entrySet()) {
                        String name = entry.getKey();
                        String value = entry.getValue();
                        String id = ItemAPI.getid(name);
                        //if(id == null || buyoffer.get(name) == null || selloffer.get(name) == null) { continue; }
                        if(itemid.get(name) == null) {itemid.put(name, id);}
                        text.append(value);
                        if(buyoffer.get(name) != null && buyamount.get(name) != null) {
                            if(buyamount.get(name) <= 0) { buyamount.remove(name); buyoffer.remove(name); buyfilled.remove(name); } else {
                                if(buyfilled.contains(name)) { text.append(" &8[" + ani + "&lB FILLED &7" + buyamount.get(name) + "x&8]"); }
                                else {
                                    if (BazaarAPI.get(id, "buy") <= buyoffer.get(name)) {
                                        text.append(" &8[&aB");
                                    } else {
                                        if(BazaarAPI.get(id, "buy") == 0.0F) {
                                            text.append(" &8[&6B");
                                        } else {
                                            text.append(" &8[&cB");
                                        }
                                    }
                                    if (Bazaar.displayUnitPrice) {
                                        text.append(buyoffer.get(name) + "&7 " + buyamount.get(name) + "&7x&8]");
                                    } else {
                                        text.append("&7 " + buyamount.get(name) + "&7x&8]");
                                    }
                                }
                            }
                        }
                        if(selloffer.get(name) != null && sellamount.get(name) != null) {
                            if(sellamount.get(name) <= 0) { sellamount.remove(name); selloffer.remove(name); sellfilled.remove(name); } else {
                                if(sellfilled.contains(name)) { text.append(" &8[" + ani + "&lS FILLED &7" + sellamount.get(name) + "x&8]"); }
                                else {
                                    if (BazaarAPI.get(id, "sell") >= selloffer.get(name)) {
                                        text.append(" &8[&aS");
                                    } else {
                                        if(BazaarAPI.get(id, "sell") == 0.0F) {
                                            text.append(" &8[&6S");
                                        } else {
                                            text.append(" &8[&cS");
                                        }
                                    }
                                    if (Bazaar.displayUnitPrice) {
                                        text.append(selloffer.get(name) + "&7 " + sellamount.get(name) + "&7x&8]");
                                    } else {
                                        text.append("&7 " + sellamount.get(name) + "&7x&8]");
                                    }
                                }
                            }
                        }

                    }
                    //stats.put("gui.text.bzflip", "&6Bazaar &8\u00BB &7You flipped &6" + ItemCount.formatNumber(value) + " coins &7in &a" + ftime + "&a minutes" + text);
                    bazaarWidget.setText("&6Bazaar &8\u00BB &7You flipped &6" + Chat.formatNumber(value) + " coins &7in &a" + ftime + "&a minutes" + text);
                }
                if(update) {
                    if(countdown == 0) {
                        update = false;
                        //stats.put("gui.hidden1.bzflip",true);
                        bazaarWidget.setEnabled(false);
                    }
                }
            }
        }
    }

    public String getitemstring(String message) {
        if(message.contains("Sell Offer Setup!")) { return getbetween(message,"Setup!","for "); }
        else if(message.contains("Buy Order Setup!")) { return getbetween(message,"Setup!","for "); }
        else if(message.contains("Order:")) { return getbetween(message,"Order:","Total "); }
        else if(message.contains("Selling:")) { return getbetween(message,"Selling:","You "); }
        else if(message.contains("Claimed") && message.contains("from selling")) { return getbetween(message,"selling","at "); }
        else if(message.contains("Claimed") && message.contains("bought for")) { return getbetween(message,"Claimed","worth"); }
        else if(message.contains("cancelling Sell Offer")) { return getbetween(message,"Refunded","from"); }
        else if(message.contains("Your") && message.contains("was filled")) { return getbetween(message,"for","was "); }
        return "";
    }

    public String getbetween(String text, String startWord, String endWord) {
        int startIndex = text.indexOf(startWord);
        int endIndex = text.indexOf(endWord);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return text.substring(startIndex + startWord.length(), endIndex).trim();
        }
        return "";
    }


    public float getamount(String input, String keyword) {
        String[] words = input.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].contains(keyword)) {
                String out = words[i - 1].replaceAll("[^0-9.]", "");
                //out = out.replace('.', '.');
                return Float.parseFloat(out);
            }
        }
        return 0.0f;
    }

    public String getitemname(String message) {
        String item = getitemstring(message);
        String[] words = item.split("x ");
        item = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
        return item;
    }

    public int getitemcount(String message) {
        String item = getitemstring(message);
        String[] words = item.split("x ");
        words[0] = words[0].replaceAll("[^0-9.]", "");
        int amountout = Integer.parseInt(words[0]);
        return amountout;
    }

}
