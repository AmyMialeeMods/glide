package xyz.amymialee.glide.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.glide.Glide;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow protected abstract PlayerEntity getCameraPlayer();
	@Shadow @Final private static Identifier HOTBAR_OFFHAND_LEFT_TEXTURE;
	@Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void glide$renderActiveGlider(float tickDelta, DrawContext context, CallbackInfo ci) {
		var player = this.getCameraPlayer();
		if (player == null) return;
		var component = Glide.GLIDING_COMPONENT.get(player);
		if (component.isGliding()) {
			var trinketComponent = TrinketsApi.TRINKET_COMPONENT.get(player);
			var gliders = trinketComponent.getEquipped(Glide.GLIDER);
			var stack = ItemStack.EMPTY;
			for (var glider : gliders) {
				stack = glider.getRight();
				if (EnchantmentHelper.getLevel(Glide.GUST, stack) > 0) {
					break;
				} else if (EnchantmentHelper.getLevel(Glide.CYCLONE, stack) > 0) {
					break;
				}
			}
			RenderSystem.enableBlend();
			context.getMatrices().push();
			context.getMatrices().translate(0.0F, 0.0F, -90.0F);
			context.drawGuiTexture(HOTBAR_OFFHAND_LEFT_TEXTURE, this.scaledWidth / 2 - 12, this.scaledHeight - 63, 29, 24);
			this.renderHotbarItem(context,  this.scaledWidth / 2 - 9, this.scaledHeight - 59, tickDelta, player, stack, 1);
			context.getMatrices().pop();
			RenderSystem.disableBlend();
		}
	}
}