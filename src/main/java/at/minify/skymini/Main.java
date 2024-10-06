package at.minify.skymini;

import at.minify.skymini.api.GUI.chatFilter.ConfigEditorChatFiler;
import at.minify.skymini.api.GUI.versionOption.ConfigEditorVersion;
import at.minify.skymini.api.GUI.chatFilter.GuiOptionEditorChatFiler;
import at.minify.skymini.api.GUI.versionOption.GuiOptionEditorVersion;
import at.minify.skymini.api.api.ModAPI;
import at.minify.skymini.api.service.EventExecutor;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.api.widgets.manager.Images;
import at.minify.skymini.core.GUI.config.GUIConfig;
import at.minify.skymini.core.GUI.mouldata;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.util.Config;
import at.minify.skymini.util.JVM.ForkedData;
import at.minify.skymini.util.JVM.ForkedJvm;
import at.minify.skymini.util.stats;
import io.github.moulberry.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.moulberry.moulconfig.processor.ConfigProcessorDriver;
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.IOException;


@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true)
public class Main {

    //TODO Islands.Hub.BZFlipper

    public static final String MODID = "SkyMini";
    public static final String VERSION = "1.0.15";
    public static GUIConfig GUIConfig;
    public static MoulConfigProcessor<GUIConfig> GUIConfigMoulConfigProcessor;
    public static ModAPI modAPI;

    public static EventExecutor eventExecutor;

    //public static GuiBetterChat chatGUI;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Images.loadResources();
        GUIConfig = new GUIConfig();
        GUIConfigMoulConfigProcessor = new MoulConfigProcessor<>(GUIConfig);
        Main.GUIConfigMoulConfigProcessor.registerConfigEditor(ConfigEditorVersion.class, (processedOption, configEditorVersion) -> new GuiOptionEditorVersion(processedOption));
        Main.GUIConfigMoulConfigProcessor.registerConfigEditor(ConfigEditorChatFiler.class, (processedOption, configEditorChatFiler) -> new GuiOptionEditorChatFiler(processedOption));
        //new MissingNEUFrame();

        ServiceContainer.serviceContainer = new ServiceContainer();
        loadFile();
        MinecraftForge.EVENT_BUS.register(this);
        modAPI = new ModAPI();

        //MinecraftForge.EVENT_BUS.register(new LicenseAPI());
        Config.load();
        WidgetService.load();
        //eman.loademan();
        stats.loadconfig();
        mouldata.load();
        //ItemAPI.loadhypixelitems();

        enableMod();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            mouldata.save();
            WidgetService.saveConfig();
            try {
                new ForkedJvm(ForkedData.class);
            } catch (IOException ignored) {
            }
        }));
    }

    public static void enableMod() {
        Images.loadResources();

        MinecraftForge.EVENT_BUS.register(new Chat());
        eventExecutor = new EventExecutor(modAPI, getServiceContainer());
        MinecraftForge.EVENT_BUS.register(eventExecutor);
        getServiceContainer().register();
    }

    public static void loadFile() {
        String mcDir = System.getProperty("user.dir");
        File folder = new File(mcDir + "/config", "skymini");
        if(!folder.exists()) {
            folder.mkdir();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        BuiltinMoulConfigGuis.addProcessors(GUIConfigMoulConfigProcessor);
        ConfigProcessorDriver.processConfig(GUIConfig.getClass(), GUIConfig, GUIConfigMoulConfigProcessor);
    }

    public static GuiScreen screenToOpen = null;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (screenToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
            screenToOpen = null;
        }
    }

    public static ModAPI getAPI() {
        return modAPI;
    }

    public static ServiceContainer getServiceContainer() {
        return ServiceContainer.serviceContainer;
    }

}
