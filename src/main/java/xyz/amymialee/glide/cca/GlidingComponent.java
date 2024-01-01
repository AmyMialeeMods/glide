package xyz.amymialee.glide.cca;

import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.glide.Glide;
import xyz.amymialee.glide.util.BlockCastFinder;

public class GlidingComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final int MAX_GUST_COOLDOWN = 12 * 20;
    private static final int MAX_CYCLONE_COOLDOWN = 6 * 20;
    private final PlayerEntity player;
    private int abilityCooldown = 0;
    private int gustTime = 0;
    private int cycloneTime = 0;
    private boolean gliding = false;

    public GlidingComponent(PlayerEntity player) {
        this.player = player;
    }

    @Environment(EnvType.CLIENT)
    public void clientGliderInput() {
        ClientPlayNetworking.send(Glide.id("glide"), PacketByteBufs.empty());
    }

    public void serverGliderInput() {
        if (!this.gliding) {
            this.setGliding(true);
        } else {
            if (this.abilityCooldown <= 0) {
                var component = TrinketsApi.TRINKET_COMPONENT.get(this.player);
                var gliders = component.getEquipped(Glide.GLIDER);
                for (var glider : gliders) {
                    var stack = glider.getRight();
                    if (EnchantmentHelper.getLevel(Glide.GUST, stack) > 0) {
                        this.abilityCooldown = MAX_GUST_COOLDOWN;
                        this.gustTime = 10;
                        Glide.GLIDING_COMPONENT.sync(this.player);
                        break;
                    } else if (EnchantmentHelper.getLevel(Glide.CYCLONE, stack) > 0) {
                        this.abilityCooldown = MAX_CYCLONE_COOLDOWN;
                        this.cycloneTime = 10;
                        Glide.GLIDING_COMPONENT.sync(this.player);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
        if (this.gliding) {
            this.player.fallDistance = 0;
            if (this.player.isOnGround() || this.player.isSneaking() || this.player.isFallFlying() || this.player.getAbilities().flying) {
                this.setGliding(false);
            }
        }
    }

    @Override
    public void tick() {
        if (this.abilityCooldown > 0) {
            this.abilityCooldown--;
            if (this.abilityCooldown == 0) {
                Glide.GLIDING_COMPONENT.sync(this.player);
            }
        }
        if (this.gliding) {
            var speed = this.player.isSneaking() ? -0.32 : -0.16;
            if (this.player.getVelocity().y < speed) {
                this.player.setVelocity(this.player.getVelocity().x, speed, this.player.getVelocity().z);
            }
            var blocks = BlockCastFinder.castRayForGridPoints(this.player.getPos(), new Vec3d(Direction.DOWN.getUnitVector()), 1.6f, 6);
            var headDistance = -1f;
            for (var block : blocks) {
                var state = this.player.getWorld().getBlockState(block);
                if (state.isIn(Glide.HEAT_SOURCES)) {
                    if (state.getProperties().contains(Properties.LIT) && !state.get(Properties.LIT)) {
                        continue;
                    }
                    var distance = (float) this.player.getPos().distanceTo(Vec3d.ofCenter(block));
                    if (distance < headDistance || headDistance < 0f) {
                        headDistance = distance;
                    }
                }
            }
            if (headDistance >= 0) {
                this.player.addVelocity(0, 1 / headDistance, 0);
            }
            if (this.gustTime > 0) {
                this.gustTime--;
                this.player.addVelocity(0, 0.35, 0);
                if (this.gustTime == 0) {
                    Glide.GLIDING_COMPONENT.sync(this.player);
                }
            }
            if (this.cycloneTime > 0) {
                this.cycloneTime--;
                var look = this.player.getRotationVector();
                look = new Vec3d(look.x, 0, look.z).normalize();
                this.player.addVelocity(look.x * 0.5f, 0, look.z * 0.5f);
                if (this.cycloneTime == 0) {
                    Glide.GLIDING_COMPONENT.sync(this.player);
                }
            }
        }
    }

    public boolean isGliding() {
        return this.gliding;
    }

    public void setGliding(boolean gliding) {
        this.gliding = gliding;
        Glide.GLIDING_COMPONENT.sync(this.player);
    }

    public boolean hasRechargeBar(ItemStack stack) {
        return EnchantmentHelper.getLevel(Glide.GUST, stack) > 0 || EnchantmentHelper.getLevel(Glide.CYCLONE, stack) > 0;
    }

    public int getRechargeBarWidth(ItemStack stack) {
        if (EnchantmentHelper.getLevel(Glide.GUST, stack) > 0) {
            return (int) MathHelper.clamp(((MAX_GUST_COOLDOWN - this.abilityCooldown) / ((float) MAX_GUST_COOLDOWN)) * 12, 0, 12);
        } else if (EnchantmentHelper.getLevel(Glide.CYCLONE, stack) > 0) {
            return (int) MathHelper.clamp(((MAX_CYCLONE_COOLDOWN - this.abilityCooldown) / ((float) MAX_CYCLONE_COOLDOWN)) * 12, 0, 12);
        }
        return 0;
    }

    public int getRechargeBarColor(ItemStack stack) {
        if (this.abilityCooldown == 0) {
            if (EnchantmentHelper.getLevel(Glide.GUST, stack) > 0) {
                return 0xFFFF6BD2;
            } else if (EnchantmentHelper.getLevel(Glide.CYCLONE, stack) > 0) {
                return 0xFF6BD2FF;
            }
        }
        return 0xFF808080;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.gliding = tag.getBoolean("gliding");
        this.abilityCooldown = tag.getInt("abilityCooldown");
        this.gustTime = tag.getInt("gustTime");
        this.cycloneTime = tag.getInt("cycloneTime");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putBoolean("gliding", this.gliding);
        tag.putInt("abilityCooldown", this.abilityCooldown);
        tag.putInt("gustTime", this.gustTime);
        tag.putInt("cycloneTime", this.cycloneTime);
    }

    static {
        ServerPlayNetworking.registerGlobalReceiver(Glide.id("glide"), (server, player, handler, buf, responseSender) -> Glide.GLIDING_COMPONENT.get(player).serverGliderInput());
    }
}