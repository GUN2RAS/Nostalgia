package net.nostalgia;
public class TestResourceKey {
    public static void main(String[] args) {
        for(java.lang.reflect.Method m : net.minecraft.resources.ResourceKey.class.getDeclaredMethods()) {
            System.out.println(m.getName());
        }
    }
}
