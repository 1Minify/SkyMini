package at.minify.skymini.core.widgets.manager;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniChatEvent;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.widgets.KuudraWidget;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MiniRegistry(server = Server.KUUDRA)
public class KuudraWidgetManager {

    KuudraWidget kuudraWidget = WidgetService.getWidget(KuudraWidget.class);

    @SubscribeEvent
    public void s(MiniTickEvent event) {
        if(!event.second(1)) {
            return;
        }
        if(!Main.getAPI().scoreboardData.toString().contains("defeat Kuudra")) {
            return;
        }
        String old = kuudraWidget.getText();
        if(!old.contains("Fuel Cells")) {
            kuudraWidget.setText("&eFuel Cells &8(&a0%&8)");
        }
    }

    @SubscribeEvent
    public void chat(MiniChatEvent event) {
        String message = event.getMessage();
        if(message.contains("recovered a Fuel Cell and charged the")) {
            String supplyCount = message.replaceAll(".*\\((\\d+)%\\).*", "$1");
            kuudraWidget.setText("&eFuel Cells &8(&a" + supplyCount + "%&8)");
        }
    }

}
