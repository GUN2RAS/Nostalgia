package net.nostalgia;

import java.lang.reflect.Method;
import net.minecraft.resources.ResourceKey;

public class TestResourceKey {
  public TestResourceKey() {
  }

  public static void main(String[] args) {
    for (Method m : ResourceKey.class.getDeclaredMethods()) {
      System.out.println(m.getName());
    }
  }
}
