package xyz.amymialee.glide.mixin;

import com.mojang.authlib.GameProfile;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.glide.Glide;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private void glide$detectJump(CallbackInfo ci) {
        var itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        if (!itemStack.isOf(Items.ELYTRA)) {
            if (!this.isOnGround() && !this.isFallFlying() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.LEVITATION)) {
                var trinketOptional = TrinketsApi.getTrinketComponent(this);
                trinketOptional.ifPresent(trinketComponent -> {
                    if (trinketComponent.isEquipped(Glide.GLIDER)) {
                        var glidingComponent = Glide.GLIDING_COMPONENT.get(this);
                        glidingComponent.clientGliderInput();
                    }
                });
            }
        }
    }
}