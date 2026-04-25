package net.nostalgia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class NostalgiaRuLanguageProvider extends FabricLanguageProvider {

    protected NostalgiaRuLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "ru_ru", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("gui.nostalgia.time_machine.launch", "ЗАПУСК");
        translationBuilder.add("gui.nostalgia.time_machine.launch_tooltip", "ИНИЦИАЛИЗАЦИЯ");
        translationBuilder.add("gui.nostalgia.time_machine.charge", "ЗАРЯД");

        translationBuilder.add("gui.nostalgia.warning.title", "АЛЬФА ПРОТОКОЛ");
        translationBuilder.add("gui.nostalgia.warning.text", "ЭТОТ МОД СОДЕРЖИТ МИГАЮЩИЕ ЭЛЕМЕНТЫ! ЛЮДЯМ С РЕАКЦИЕЙ НА СВЕТ И ЭПИЛЕПСИЕЙ ПРОСЬБА НЕ ПОЛЬЗОВАТЬСЯ МОДОМ!");
        translationBuilder.add("gui.nostalgia.warning.continue", "ПРОДОЛЖИТЬ");
    }
}
