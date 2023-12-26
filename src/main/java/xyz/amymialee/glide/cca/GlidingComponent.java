package xyz.amymialee.glide.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.glide.Glide;

public class GlidingComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final int MAX_GUST_COOLDOWN = 6 * 20;
    private static final int MAX_CYCLONE_COOLDOWN = 12 * 20;
    private final PlayerEntity player;
    private int abilityCooldown = 0;
    private boolean gliding = false;

    public GlidingComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        var clientPlayer = MinecraftClient.getInstance().player;
        if (this.player != clientPlayer) return;
        if (this.gliding) {
            this.player.addVelocity(0, 0.03, 0);
        }
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
    }

    @Override
    public void tick() {
        if (this.abilityCooldown > 0) {
            this.abilityCooldown--;
            if (this.abilityCooldown == 0) {
                Glide.GLIDING_COMPONENT.sync(this.player);
            }
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.gliding = tag.getBoolean("gliding");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putBoolean("gliding", this.gliding);
    }
}