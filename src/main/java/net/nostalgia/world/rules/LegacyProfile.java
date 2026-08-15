package net.nostalgia.world.rules;

import net.minecraft.resources.Identifier;

public interface LegacyProfile {
  boolean hasWeather();

  boolean isEternalSnow();

  Integer flatSkyColor();

  boolean disableSunriseSunsetGradient();

  boolean classicStars();

  boolean classicClouds();

  Identifier cloudTexture();
}
