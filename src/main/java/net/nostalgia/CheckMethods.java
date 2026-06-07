package net.nostalgia;

import java.lang.reflect.Method;

public class CheckMethods {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println("METHOD: " + m.getName());
        }
        for (Class<?> c : clazz.getDeclaredClasses()) {
            System.out.println("INNER_CLASS: " + c.getName());
            for (Method m : c.getDeclaredMethods()) {
                System.out.println("  " + c.getSimpleName() + " METHOD: " + m.getName());
            }
        }
    }
}
