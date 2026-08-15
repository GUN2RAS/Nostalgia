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
    float depth = texture(Sampler1, texCoord0).r;
    
    
    if (depth < 0.9999) {
        fragColor = vec4(0.0);
        return;
    }

    vec4 ndc = vec4(texCoord0.x * 2.0 - 1.0, texCoord0.y * 2.0 - 1.0, depth, 1.0);
    vec4 worldHomogeneous = InverseViewProj * ndc;
    vec3 camRelativePos = worldHomogeneous.xyz / worldHomogeneous.w;
    vec3 absoluteWorldPos = camRelativePos + CamPosData.xyz;

    vec3 PortalPos = BeaconAndTimer.xyz;
    PortalPos.y += 90.0; 
    
    
    
    if (camRelativePos.y <= 0.001) {
        fragColor = vec4(0.0); 
        return;
    }
    
    float t = (PortalPos.y - CamPosData.y) / camRelativePos.y;
    vec3 hitPos = CamPosData.xyz + camRelativePos * t;
    
    
    vec3 blockPos = floor(hitPos) + vec3(0.5); 
    
    vec2 planePos = blockPos.xz;
    vec2 centerPos = PortalPos.xz;
    
    
    float noise = fract(sin(dot(blockPos, vec3(12.9898, 78.233, 45.164))) * 43758.5453);
    
    
    float TransitionTime = BeaconAndTimer.w; 
    float maxRadius = 120.0; 
    float currentRadius = min(TransitionTime * 40.0, maxRadius); 
    
    
    float innerRadius = currentRadius - 5.0; 
    
    float dist = distance(planePos, centerPos) + (noise - 0.5) * 8.0;
    
    if (dist < innerRadius) {
        
        float distToEdge = innerRadius - dist;
        if (distToEdge < 3.0) {
            
            float glowAlpha = pow(1.0 - (distToEdge / 3.0), 2.0);
            fragColor = vec4(1.0, 1.0, 1.0, glowAlpha);
        } else {
            
            fragColor = vec4(0.0, 0.0, 0.0, 1.0);
        }
    } else if (dist < currentRadius) {
        
        float sphereAlpha = 1.0 - smoothstep(currentRadius - 3.0, currentRadius, dist);
        float innerAlpha = smoothstep(innerRadius, innerRadius + 2.0, dist);
        
        float waveAlpha = min(sphereAlpha, innerAlpha);
        
        
        
        float pulse1 = sin(planePos.x * 0.4 + TransitionTime * 3.0);
        float pulse2 = cos(planePos.y * 0.4 - TransitionTime * 2.5);
        float smoothShimmer = (pulse1 * pulse2 + 1.0) * 0.5; 
        
        if (waveAlpha > 0.01) {
            
            fragColor = vec4(1.0, 1.0, 1.0, waveAlpha * (0.8 + 0.2 * smoothShimmer));
        } else {
            fragColor = vec4(0.0);
        }
    } else {
        fragColor = vec4(0.0);
    }
}
