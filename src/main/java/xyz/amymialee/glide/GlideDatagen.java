package xyz.amymialee.glide;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.mialib.data.MDataGen;

public class GlideDatagen extends MDataGen {
    @Override
    protected void generateTranslations(MLanguageProvider provider, FabricLanguageProvider.@NotNull TranslationBuilder builder) {
        builder.add(Glide.GLIDER, "Glider");
        builder.add(Glide.GLIDER_FABRIC, "Glider Fabric");
        builder.add(Glide.GUST, "Gust");
        builder.add(Glide.CYCLONE, "Cyclone");
        builder.add("trinkets.slot.chest.glider", "Glider");
    }

    @Override
    protected void generateItemModels(MModelProvider provider, @NotNull ItemModelGenerator generator) {
        generator.register(Glide.GLIDER, Models.GENERATED);
        generator.register(Glide.GLIDER_FABRIC, Items.MOJANG_BANNER_PATTERN, Models.GENERATED);
    }

    @Override
    protected void generateBlockTags(@NotNull MBlockTagProvider provider, RegistryWrapper.WrapperLookup arg) {
        provider.getOrCreateTagBuilder(Glide.HEAT_SOURCES)
                .addOptionalTag(BlockTags.FIRE)
                .addOptionalTag(BlockTags.CAMPFIRES)
                .add(Blocks.LAVA)
                .add(Blocks.LAVA_CAULDRON)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.FURNACE)
                .add(Blocks.SMOKER)
                .add(Blocks.BLAST_FURNACE);
    }

    @Override
    protected void generateRecipes(MRecipeProvider provider, RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Glide.GLIDER_FABRIC)
                .input('W', ItemTags.WOOL)
                .input('S', Items.STRING)
                .pattern(" S ")
                .pattern("SWS")
                .pattern("S S")
                .criterion(FabricRecipeProvider.hasItem(Items.STRING), FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Glide.GLIDER)
                .input('F', Glide.GLIDER_FABRIC)
                .input('#', Items.BONE)
                .pattern(" # ")
                .pattern("#F#")
                .criterion(FabricRecipeProvider.hasItem(Glide.GLIDER_FABRIC), FabricRecipeProvider.conditionsFromItem(Glide.GLIDER_FABRIC))
                .offerTo(exporter);
    }
}