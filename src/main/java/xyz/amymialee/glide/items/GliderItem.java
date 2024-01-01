package xyz.amymialee.glide.items;

import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import xyz.amymialee.glide.Glide;

public class GliderItem extends TrinketItem {
    public GliderItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult mialib$checkEnchantment(EnchantmentTarget target, Enchantment enchantment) {
        if (enchantment == Glide.GUST || enchantment == Glide.CYCLONE) {
            return ActionResult.SUCCESS;
        }
        return super.mialib$checkEnchantment(target, enchantment);
    }

    @Override @Environment(EnvType.CLIENT)
    public void mialib$renderCustomBar(DrawContext drawContext, TextRenderer renderer, ItemStack stack, int x, int y, String countLabel) {
        if (MinecraftClient.getInstance().player == null) return;
        var component = Glide.GLIDING_COMPONENT.get(MinecraftClient.getInstance().player);
        if (component.hasRechargeBar(stack)) {
            var width = component.getRechargeBarWidth(stack);
            var color = component.getRechargeBarColor(stack);
            drawContext.fill(RenderLayer.getGuiOverlay(), x + 2, y + 13, x + 2 + 13, y + 13 + 2, 0xFF000000);
            drawContext.fill(RenderLayer.getGuiOverlay(), x + 2, y + 13, x + 2 + width, y + 13 + 1, color);
        }
    }
}