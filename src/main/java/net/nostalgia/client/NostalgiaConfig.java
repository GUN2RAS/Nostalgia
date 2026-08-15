package net.nostalgia.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.fabricmc.loader.api.FabricLoader;

public class NostalgiaConfig {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "nostalgia_client.json");
  public boolean alphaLoadingScreen = true;
  public NostalgiaConfig.RitualType ritualType = NostalgiaConfig.RitualType.CLASSIC;
  private static NostalgiaConfig instance;

  public NostalgiaConfig() {
  }

  public static NostalgiaConfig get() {
    if (instance == null) {
      load();
    }

    return instance;
  }

  public static void load() {
    if (FILE.exists()) {
      try (FileReader reader = new FileReader(FILE)) {
        instance = (NostalgiaConfig)GSON.fromJson(reader, NostalgiaConfig.class);
      } catch (Exception var5) {
        var5.printStackTrace();
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
    } catch (Exception var5) {
      var5.printStackTrace();
    }
  }

  public static enum RitualType {
    CLASSIC,
    SEAMLESS_PORTAL;

    private RitualType() {
    }
  }
}
