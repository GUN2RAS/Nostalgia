package net.nostalgia.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class NostalgiaConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "nostalgia_client.json");

    public enum RitualType {
        CLASSIC,
        SEAMLESS_PORTAL
    }

    public boolean alphaLoadingScreen = true;
    public RitualType ritualType = RitualType.CLASSIC;

    private static NostalgiaConfig instance;

    public static NostalgiaConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                instance = GSON.fromJson(reader, NostalgiaConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
                instance = new NostalgiaConfig();
            }
        } else {
            instance = new NostalgiaConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(instance, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
