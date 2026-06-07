#version 330

layout(std140) uniform LightmapInfo {
    float SkyFactor;
    float BlockFactor;
    float NightVisionFactor;
    float DarknessScale;
    float BossOverlayWorldDarkeningFactor;
    float BrightnessFactor;
    vec3 BlockLightTint;
    vec3 SkyLightColor;
    vec3 AmbientColor;
    vec3 NightVisionColor;
} lightmapInfo;

in vec2 texCoord;
out vec4 fragColor;

float get_brightness(float level) {
    return level / (4.0 - 3.0 * level);
}

vec3 notGamma(vec3 color) {
    float maxComponent = max(max(color.x, color.y), color.z);
    float maxInverted = 1.0f - maxComponent;
    float maxScaled = 1.0f - maxInverted * maxInverted * maxInverted * maxInverted;
    return color * (maxScaled / maxComponent);
}

float parabolicMixFactor(float level) {
    return (2.0 * level - 1.0) * (2.0 * level - 1.0);
}

void main() {
    
    
    
    if (abs(lightmapInfo.BlockLightTint.x - 0.1337) < 0.001 && abs(lightmapInfo.BlockLightTint.y - 0.420) < 0.001) {
        float block_level = floor(texCoord.x * 16.0) / 15.0;
        float sky_level = floor(texCoord.y * 16.0) / 15.0;

        
        float effective_skylevel = max(0.0, sky_level - (1.0 - lightmapInfo.SkyFactor));
        float effective_level = max(block_level, effective_skylevel);

        
        float f3 = 1.0 - effective_level;
        float alphaBrightness = (1.0 - f3) / (f3 * 3.0 + 1.0) * 0.95 + 0.05;

        
        alphaBrightness = max(alphaBrightness, lightmapInfo.NightVisionFactor);

        fragColor = vec4(alphaBrightness, alphaBrightness, alphaBrightness, 1.0);
        return;
    }
    

    
    float block_level = floor(texCoord.x * 16.0) / 15.0;
    float sky_level = floor(texCoord.y * 16.0) / 15.0;

    float block_brightness = get_brightness(block_level) * lightmapInfo.BlockFactor;
    float sky_brightness = get_brightness(sky_level) * lightmapInfo.SkyFactor;

    vec3 nightVisionColor = lightmapInfo.NightVisionColor * lightmapInfo.NightVisionFactor;
    vec3 color = max(lightmapInfo.AmbientColor, nightVisionColor);

    color += lightmapInfo.SkyLightColor * sky_brightness;

    vec3 BlockLightColor = mix(lightmapInfo.BlockLightTint, vec3(1.0), 0.9 * parabolicMixFactor(block_level));
    color += BlockLightColor * block_brightness;

    color = mix(color, color * vec3(0.7, 0.6, 0.6), lightmapInfo.BossOverlayWorldDarkeningFactor);
    color = color - vec3(lightmapInfo.DarknessScale);

    color = clamp(color, 0.0, 1.0);
    vec3 notGammaColor = notGamma(color);
    color = mix(color, notGammaColor, lightmapInfo.BrightnessFactor);

    fragColor = vec4(color, 1.0);
}
