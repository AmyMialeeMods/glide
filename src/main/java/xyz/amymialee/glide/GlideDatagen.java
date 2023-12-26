package xyz.amymialee.glide;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.mialib.data.MDataGen;

public class GlideDatagen extends MDataGen {
    @Override
    protected void generateTranslations(MLanguageProvider provider, FabricLanguageProvider.@NotNull TranslationBuilder builder) {
        builder.add(Glide.GLIDER, "Glider");
    }

    @Override
    protected void generateItemModels(MModelProvider provider, @NotNull ItemModelGenerator generator) {
        generator.register(Glide.GLIDER, Models.GENERATED);
    }
}
