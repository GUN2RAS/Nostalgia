#version 150

uniform sampler2D Sampler1; 

layout(std140) uniform WhiteoutData {
    vec4 BeaconAndTimer; 
    mat4 InverseViewProj;
    vec4 ExtraData; 
    vec4 CamPosData; 
    vec4 SkyColor; 
};

in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec3 BeaconPos = BeaconAndTimer.xyz;
    float TransitionTime = BeaconAndTimer.w;
    vec3 CamPos = CamPosData.xyz;
    
    float depth = texture(Sampler1, texCoord0).r;

    vec4 ndc = vec4(texCoord0.x * 2.0 - 1.0, texCoord0.y * 2.0 - 1.0, depth, 1.0);
    vec4 worldHomogeneous = InverseViewProj * ndc;
    
    vec3 camRelativePos = worldHomogeneous.xyz / worldHomogeneous.w;
    
    
    vec3 absoluteWorldPos = camRelativePos + CamPos;

    
    
    if (depth >= 0.9999 || depth <= 0.0001) {
        fragColor = vec4(0.0);
        return;
    }
    

    
    vec3 blockPos = floor(absoluteWorldPos) + vec3(0.5); 
    
    
    float noise = fract(sin(dot(blockPos, vec3(12.9898, 78.233, 45.164))) * 43758.5453);
    
    
    vec3 diff = blockPos - BeaconPos;
    float dist = length(diff) + (noise - 0.5) * 4.0;
    
    float outerRadius = ExtraData.z; 
    float innerRadius = ExtraData.w; 
    float globalFade = ExtraData.x; 
    
    
    if (dist < innerRadius) {
        
        
        float distToEdge = innerRadius - dist;
        if (distToEdge < 4.0) {
            float glowAlpha = pow(1.0 - (distToEdge / 4.0), 2.0); 
            fragColor = vec4(1.0, 1.0, 1.0, glowAlpha * globalFade);
        } else {
            fragColor = vec4(0.0);
        }
    } else if (dist < outerRadius) {
        
        if (absoluteWorldPos.y > BeaconPos.y) {
            
            fragColor = vec4(1.0, 1.0, 1.0, globalFade);
        } else {
            
            float sphereAlpha = 1.0 - smoothstep(outerRadius - 10.0, outerRadius, dist);
            
            float innerAlpha = smoothstep(innerRadius, innerRadius + 5.0, dist);
            
            float waveAlpha = min(sphereAlpha, innerAlpha);
            if (waveAlpha > 0.01) {
                fragColor = vec4(1.0, 1.0, 1.0, waveAlpha * globalFade);
            } else {
                fragColor = vec4(0.0);
            }
        }
    } else {
        
        fragColor = vec4(0.0);
    }
}
