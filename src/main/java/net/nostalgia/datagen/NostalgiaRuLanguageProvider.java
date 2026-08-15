package net.nostalgia.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder;
import net.minecraft.core.HolderLookup.Provider;

public class NostalgiaRuLanguageProvider extends FabricLanguageProvider {
  protected NostalgiaRuLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<Provider> registryLookup) {
    super(dataOutput, "ru_ru", registryLookup);
  }

  public void generateTranslations(Provider registryLookup, TranslationBuilder translationBuilder) {
    translationBuilder.add("gui.nostalgia.time_machine.launch", "\u0417\u0410\u041f\u0423\u0421\u041a");
    translationBuilder.add("gui.nostalgia.time_machine.launch_tooltip", "\u0418\u041d\u0418\u0426\u0418\u0410\u041b\u0418\u0417\u0410\u0426\u0418\u042f");
    translationBuilder.add("gui.nostalgia.time_machine.charge", "\u0417\u0410\u0420\u042f\u0414");
    translationBuilder.add("gui.nostalgia.time_machine.scanning", "\u0421\u041a\u0410\u041d");
    translationBuilder.add("gui.nostalgia.time_machine.scan_cooldown", "\u041f\u0415\u0420\u0415\u0417\u0420\u042f\u0414");
    translationBuilder.add("gui.nostalgia.warning.title", "\u0410\u041b\u042c\u0424\u0410 \u041f\u0420\u041e\u0422\u041e\u041a\u041e\u041b");
    translationBuilder.add(
      "gui.nostalgia.warning.text",
      "\u042d\u0422\u041e\u0422 \u041c\u041e\u0414 \u0421\u041e\u0414\u0415\u0420\u0416\u0418\u0422 \u041c\u0418\u0413\u0410\u042e\u0429\u0418\u0415 \u042d\u041b\u0415\u041c\u0415\u041d\u0422\u042b! \u041b\u042e\u0414\u042f\u041c \u0421 \u0420\u0415\u0410\u041a\u0426\u0418\u0415\u0419 \u041d\u0410 \u0421\u0412\u0415\u0422 \u0418 \u042d\u041f\u0418\u041b\u0415\u041f\u0421\u0418\u0415\u0419 \u041f\u0420\u041e\u0421\u042c\u0411\u0410 \u041d\u0415 \u041f\u041e\u041b\u042c\u0417\u041e\u0412\u0410\u0422\u042c\u0421\u042f \u041c\u041e\u0414\u041e\u041c!"
    );
    translationBuilder.add("gui.nostalgia.warning.continue", "\u041f\u0420\u041e\u0414\u041e\u041b\u0416\u0418\u0422\u042c");
    translationBuilder.add(
      "item.nostalgia.charged_amethyst", "\u0417\u0430\u0440\u044f\u0436\u0435\u043d\u043d\u044b\u0439 \u0410\u043c\u0435\u0442\u0438\u0441\u0442"
    );
    translationBuilder.add(
      "item.nostalgia.charged_amethyst.up",
      "\u00a7b\u0417\u0430\u0440\u044f\u0436\u0435\u043d\u043d\u044b\u0439 \u0410\u043c\u0435\u0442\u0438\u0441\u0442 \u16cf"
    );
    translationBuilder.add(
      "item.nostalgia.charged_amethyst.down",
      "\u00a75\u0417\u0430\u0440\u044f\u0436\u0435\u043d\u043d\u044b\u0439 \u0410\u043c\u0435\u0442\u0438\u0441\u0442 \u16e6"
    );
    translationBuilder.add(
      "item.nostalgia.charged_amethyst.left",
      "\u00a7a\u0417\u0430\u0440\u044f\u0436\u0435\u043d\u043d\u044b\u0439 \u0410\u043c\u0435\u0442\u0438\u0441\u0442 \u16b2"
    );
    translationBuilder.add(
      "item.nostalgia.charged_amethyst.right",
      "\u00a76\u0417\u0430\u0440\u044f\u0436\u0435\u043d\u043d\u044b\u0439 \u0410\u043c\u0435\u0442\u0438\u0441\u0442 \u16a6"
    );
  }
}
