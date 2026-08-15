package net.nostalgia.mixin.client.ritual;

import net.caffeinemc.mods.sodium.client.gl.shader.ShaderLoader;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ShaderLoader.class, remap = false)
public class ShaderLoaderMixin {

    @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
    private static void onGetShaderSource(Identifier name, CallbackInfoReturnable<String> cir) {
        String path = name.toString();

        if (path.endsWith(".vsh") && path.contains("block_layer")) {
            String src = cir.getReturnValue();

            if (src.contains("uniform vec3 u_RegionOffset;")) {
                src = src.replace(
                    "uniform vec3 u_RegionOffset;",
                    "uniform vec3 u_RegionOffset;\n" +
                    "uniform vec3 u_RitualCenter;\n" +
                    "uniform float u_RitualRadius;\n"
                );

                java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("v_FragDistance\\s*=\\s*getFragDistance\\([^)]+\\);")
                    .matcher(src);

                if (m.find()) {
                    String match = m.group();
                    String replacement = match + "\n" +
                        "    if (u_RitualRadius > 0.0 && position.y > u_RitualCenter.y - 15.0 && length(position - u_RitualCenter) <= u_RitualRadius) {\n" +
                        "        v_FragDistance = vec2(0.0);\n" +
                        "    }";
                    src = src.replace(match, replacement);
                    cir.setReturnValue(src);
                }
            }
        }
    }
}
