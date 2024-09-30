package at.minify.skymini.mixins.GuiChat;

import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({GuiNewChat.class})
public abstract class GuiNewChatMixin {

    /*@Shadow
    protected abstract int getLineCount();
    @Shadow protected abstract boolean getChatOpen();
    @Shadow protected abstract int getChatWidth();
    @Shadow protected abstract float getChatScale();
    @Shadow protected int scrollPos;
    @Shadow protected java.util.List<ChatLine> drawnChatLines;
    @Shadow protected net.minecraft.client.Minecraft mc;

    protected boolean isScrolled() {
        return false;
    }

    @Shadow private boolean isScrolled;

    @Inject(method = "drawChat", at = @At("HEAD"), cancellable = true)
    private void modifyChatRendering(int updateCounter, CallbackInfo ci) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean bl = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (k > 0) {
                if (this.getChatOpen()) {
                    bl = true;
                }

                float g = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / g);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(g, g, 1.0F);

                int m;
                int n;
                int o;
                int p;
                for(m = 0; m + this.scrollPos < this.drawnChatLines.size() && m < i; ++m) {
                    ChatLine chatLine = (ChatLine)this.drawnChatLines.get(m + this.scrollPos);
                    if (chatLine != null) {
                        IChatComponent chatComponent = chatLine.getChatComponent();
                        String text = chatComponent.getUnformattedText();
                        if (text.contains("joined")) {
                            n = updateCounter - chatLine.getUpdatedCounter();
                            if (n < 200 || bl) {
                                double d = (double)n / 200.0;
                                d = 1.0 - d;
                                d *= 10.0;
                                d = MathHelper.clamp_double(d, 0.0, 1.0);
                                d *= d;
                                o = (int)(255.0 * d);
                                if (bl) {
                                    o = 255;
                                }

                                o = (int)((float)o * f);
                                ++j;
                                if (o > 3) {
                                    p = 0;
                                    int q = -m * 9;
                                    GuiNewChat.drawRect(p, q - 9, p + l + 4, q, o / 2 << 24);
                                    GlStateManager.enableBlend();
                                    this.mc.fontRendererObj.drawStringWithShadow(text, (float)p, (float)(q - 8), 16777215 + (o << 24));
                                    GlStateManager.disableAlpha();
                                    GlStateManager.disableBlend();
                                }
                            }
                        }
                    }
                }

                if (bl) {
                    m = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int r = k * m + k;
                    n = j * m + j;
                    int s = this.scrollPos * n / k;
                    int t = n * n / r;
                    if (r != n) {
                        o = s > 0 ? 170 : 96;
                        p = this.isScrolled() ? 13382451 : 3355562;
                        GuiNewChat.drawRect(0, -s, 2, -s - t, p + (o << 24));
                        GuiNewChat.drawRect(2, -s, 1, -s - t, 13421772 + (o << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
        ci.cancel();
    }



    private float percentComplete;
    private int newLines;
    private long prevMillis = System.currentTimeMillis();
    private boolean configuring;
    private float animationPercent;
    private int lineBeingDrawn;
    private List<ChatLine> chatLines;

    public GuiNewChatMixin() {
    }

    public void setConfiguring(boolean configuring) {
        this.configuring = configuring;
    }

    private void updatePercentage(long diff) {
        if (this.percentComplete < 1.0F)
            this.percentComplete += 0.004F * (float)diff;
        this.percentComplete = GuiManager.clamp(this.percentComplete, 0.0F, 1.0F);
    }

    @Inject(method = {"drawChat"}, at = {@At("HEAD")}, cancellable = true)
    private void modifyChatRendering(CallbackInfo ci) {
        if (this.configuring) {
            ci.cancel();
            return;
        }
        long current = System.currentTimeMillis();
        long diff = current - this.prevMillis;
        this.prevMillis = current;
        updatePercentage(diff);
        float t = this.percentComplete;
        this.animationPercent = GuiManager.clamp(1.0F - --t * t * t * t, 0.0F, 1.0F);
    }

    @Inject(method = {"drawChat"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0, shift = At.Shift.AFTER)})
    private void translate(CallbackInfo ci) {
        float y = (BetterChat.getSettings()).yOffset;
        if ((BetterChat.getSettings()).smooth)
            y += (9.0F - 9.0F * this.animationPercent) * func_146244_h();
        GlStateManager.func_179109_b((BetterChat.getSettings()).xOffset, y, 0.0F);
    }

    @Redirect(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void transparentBackground(int left, int top, int right, int bottom, int color) {
        if (!(BetterChat.getSettings()).clear)
            func_73734_a(left, top, right, bottom, color);
    }

    @ModifyArg(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false), index = 0)
    private int getLineBeingDrawn(int line) {
        this.lineBeingDrawn = line;
        return line;
    }

    @ModifyArg(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"), index = 3)
    private int modifyTextOpacity(int original) {
        if (this.lineBeingDrawn <= this.newLines) {
            int opacity = original >> 24 & 0xFF;
            opacity = (int)(opacity * this.animationPercent);
            return original & 0xFFFFFF | opacity << 24;
        }
        return original;
    }

    @Inject(method = {"printChatMessageWithOptionalDeletion"}, at = {@At("HEAD")})
    private void resetPercentage(CallbackInfo ci) {
        this.percentComplete = 0.0F;
    }

    @ModifyVariable(method = {"setChatLine"}, at = @At("STORE"), ordinal = 0)
    private List<IChatComponent> setNewLines(List<IChatComponent> original) {
        this.newLines = original.size() - 1;
        return original;
    }*/

}
