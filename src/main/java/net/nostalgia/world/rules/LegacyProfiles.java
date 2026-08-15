package net.nostalgia.world.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.world.dimension.ModDimensions;

public class LegacyProfiles {
  private static final Map<String, LegacyProfile> REGISTRY = new HashMap<>();
  public static final LegacyProfile VANILLA = new LegacyProfile() {
    @Override
    public boolean hasWeather() {
      return true;
    }

    @Override
    public boolean isEternalSnow() {
      return false;
    }

    @Override
    public Integer flatSkyColor() {
      return null;
    }

    @Override
    public boolean disableSunriseSunsetGradient() {
      return false;
    }

    @Override
    public boolean classicStars() {
      return false;
    }

    @Override
    public boolean classicClouds() {
      return false;
    }

    @Override
    public Identifier cloudTexture() {
      return null;
    }
  };
  public static final LegacyProfile RD = new LegacyProfile() {
    @Override
    public boolean hasWeather() {
      return false;
    }

    @Override
    public boolean isEternalSnow() {
      return false;
    }

    @Override
    public Integer flatSkyColor() {
      return -8402689;
    }

    @Override
    public boolean disableSunriseSunsetGradient() {
      return true;
    }

    @Override
    public boolean classicStars() {
      return false;
    }

    @Override
    public boolean classicClouds() {
      return false;
    }

    @Override
    public Identifier cloudTexture() {
      return null;
    }
  };
  public static final LegacyProfile ALPHA = new LegacyProfile() {
    @Override
    public boolean hasWeather() {
      return this.isEternalSnow();
    }

    @Override
    public boolean isEternalSnow() {
      return new Random(AlphaEngineManager.getWorldSeed()).nextInt(4) == 0;
    }

    @Override
    public Integer flatSkyColor() {
      return null;
    }

    @Override
    public boolean disableSunriseSunsetGradient() {
      return false;
    }

    @Override
    public boolean classicStars() {
      return true;
    }

    @Override
    public boolean classicClouds() {
      return true;
    }

    @Override
    public Identifier cloudTexture() {
      return Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/clouds.png");
    }
  };

  public LegacyProfiles() {
  }

  public static void register(String dimId, LegacyProfile profile) {
    REGISTRY.put(dimId, profile);
  }

  public static LegacyProfile get(Level level) {
    if (level == null) {
      return VANILLA;
    } else {
      String dimId = level.dimension().identifier().toString();
      LegacyProfile profile = REGISTRY.get(dimId);
      return profile != null ? profile : VANILLA;
    }
  }

  static {
    register(ModDimensions.ALPHA_112_01_LEVEL_KEY.identifier().toString(), ALPHA);
    register(ModDimensions.RD_132211_LEVEL_KEY.identifier().toString(), RD);
  }
}
