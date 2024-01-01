package xyz.amymialee.glide;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.glide.cca.GlidingComponent;
import xyz.amymialee.glide.items.GliderItem;
import xyz.amymialee.mialib.MRegistry;
import xyz.amymialee.mialib.templates.MEnchantment;

public class Glide implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "glide";
    public static final MRegistry REGISTRY = new MRegistry(MOD_ID);

    public static final ComponentKey<GlidingComponent> GLIDING_COMPONENT = ComponentRegistry.getOrCreate(id("gliding"), GlidingComponent.class);
    public static final Item GLIDER_FABRIC = REGISTRY.registerItem("glider_fabric", new Item(new FabricItemSettings().maxCount(1)), Registries.ITEM_GROUP.get(ItemGroups.TOOLS));
    public static final Item GLIDER = REGISTRY.registerItem("glider", new GliderItem(new FabricItemSettings().maxCount(1)), Registries.ITEM_GROUP.get(ItemGroups.TOOLS));
    public static final MEnchantment GUST = REGISTRY.register("gust", new MEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEARABLE));
    public static final MEnchantment CYCLONE = REGISTRY.register("cyclone", new MEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEARABLE));
    public static final TagKey<Block> HEAT_SOURCES = TagKey.of(Registries.BLOCK.getKey(), id("heat_sources"));

    @Override
    public void onInitialize() {
        GUST.setCanAccept((mEnchant, bool, enchant) -> bool && enchant != CYCLONE);
        CYCLONE.setCanAccept((mEnchant, bool, enchant) -> bool && enchant != GUST);
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, GLIDING_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(GlidingComponent::new);
    }

    public static @NotNull Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}