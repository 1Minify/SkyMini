package at.minify.skymini.api.widgets.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class Images {

    public static HashMap<String, ResourceLocation> cache = new HashMap<>();

    public static ResourceLocation getImage(String name) {
        if(cache.containsKey(name)) {
            return cache.get(name);
        }
        return null;
    }

    public static void loadResources() {
        loadResource(ResourceCategory.WIDGET, "coin");
        loadResource(ResourceCategory.WIDGET,"bit");
        loadResource(ResourceCategory.WIDGET,"capy");
        loadResource(ResourceCategory.SOCIAL, "github");
    }

    private static void loadResource(ResourceCategory category, String name) {
        cache.put(name, new ResourceLocation("skymini", "textures/" + category.getFile() + "/" + name + ".png"));
    }

    @AllArgsConstructor
    @Getter
    public enum ResourceCategory {

        WIDGET("widget"),
        SOCIAL("social");

        String file;

    }

    /*public static void drawImageWithText(ResourceLocation image, String text, int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

        mc.getTextureManager().bindTexture(image);
        GL11.glEnable(GL11.GL_BLEND); // Alpha-Blending aktivieren
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Einstellung für Alpha-Blending
        Gui.drawModalRectWithCustomSizedTexture(x, y-1, 0, 0, 10, 10, 10, 10);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Farbe zurücksetzen
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        fontRenderer.drawStringWithShadow(text, 13, 0, 0xFFFFFF);
        GL11.glPopMatrix();
    }*/

}
