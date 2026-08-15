#version 150

uniform sampler2D Sampler1; 

layout(std140) uniform TimestopData {
    vec4 Zones[4]; 
    vec4 ZoneFades[4]; 
    mat4 InverseViewProj;
    vec4 CamPosData; 
    vec4 ExtraData;  
};

in vec2 texCoord0;
out vec4 fragColor;

float hash13(vec3 p3) {
    p3  = fract(p3 * .1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

void main() {
    float depth = texture(Sampler1, texCoord0).r;
    
    vec2 ndc = texCoord0 * 2.0 - 1.0;
    vec4 ndcPix = vec4(ndc, depth * 2.0 - 1.0, 1.0);
    vec4 unprojPix = InverseViewProj * ndcPix;
    vec3 pixelWorldPos = unprojPix.xyz / unprojPix.w;
    
    vec4 uA = InverseViewProj * vec4(ndc, -1.0, 1.0);
    vec4 uB = InverseViewProj * vec4(ndc, 1.0, 1.0);
    vec3 rayPointA = uA.xyz / uA.w;
    vec3 rayPointB = uB.xyz / uB.w;
    vec3 rayDir = normalize(rayPointB - rayPointA);
    
    float t_scene = length(pixelWorldPos - rayPointA);
    if (depth >= 0.99999) {
        t_scene = 100000.0; 
    }

    int activeZones = int(ExtraData.x);
    if (activeZones <= 0) {
        fragColor = vec4(0.0);
        return;
    }
    
    float closestHit = 100000.0;
    vec3 hitZoneCenter = vec3(0.0);
    float fadeProgress = 0.0;
    float bestTNear = 0.0;
    float bestTFar = 0.0;
    
    
    for(int i = 0; i < 4; i++) {
        if (i >= activeZones) break;
        
        vec3 center = Zones[i].xyz;
        float radius = Zones[i].w;
        
        
        vec3 boxMin = center - vec3(radius, 500.0, radius); 
        vec3 boxMax = center + vec3(radius, 320.0, radius); 
        
        vec3 invDir = 1.0 / rayDir;
        vec3 t0 = (boxMin - rayPointA) * invDir;
        vec3 t1 = (boxMax - rayPointA) * invDir;
        
        vec3 tmin = min(t0, t1);
        vec3 tmax = max(t0, t1);
        
        float tNear = max(max(tmin.x, tmin.y), tmin.z);
        float tFar = min(min(tmax.x, tmax.y), tmax.z);
        
        if (tNear < tFar && tFar > 0.0) {
            float t_wall = tNear < 0.0 ? tFar : tNear; 
            if (t_wall > 0.0 && t_wall < closestHit) {
                closestHit = t_wall;
                hitZoneCenter = center;
                fadeProgress = ZoneFades[i].x;
                bestTNear = tNear;
                bestTFar = tFar;
            }
        }
    }
    
    if (closestHit > 99999.0) {
        fragColor = vec4(0.0);
        return;
    }
    
    vec3 color = vec3(0.5, 0.1, 0.9);
    float totalOpacity = 0.0;
    
    
    float t0_fog = max(0.0, bestTNear);
    float t1_fog = min(t_scene, bestTFar);
    
    if (t1_fog > t0_fog) {
        float distInVolume = t1_fog - t0_fog;
        
        
        float fogFactor = 1.0 - exp(-distInVolume * 0.02);
        
        
        vec3 midPos = rayPointA + rayDir * ((t0_fog + t1_fog) * 0.5) + CamPosData.xyz;
        float time = CamPosData.w;
        float smokeTime = time * 0.5;
        float smokeNoise = sin(midPos.x * 0.1 + midPos.y * 0.2 + smokeTime) + sin(midPos.z * 0.15 - midPos.y * 0.1 - smokeTime * 0.8);
        smokeNoise = smokeNoise * 0.25 + 0.5; 
        
        
        fogFactor *= (0.03 + 0.08 * smokeNoise);
        
        
        fogFactor *= (1.0 - fadeProgress);
        
        vec3 fogColor = vec3(0.5, 0.1, 0.9);
        color = mix(color, fogColor, fogFactor);
        totalOpacity = min(1.0, totalOpacity + fogFactor);
    }
    
    closestHit -= 0.02;
    
    
    
    if (closestHit <= t_scene) {
        float diff = max(0.0, t_scene - closestHit);
        float intersectionGlow = exp(-diff * 2.5); 
        
        vec3 hitPos = rayPointA + rayDir * closestHit;
        vec3 absoluteHitPos = hitPos + CamPosData.xyz;
        
        vec3 localPos = hitPos - hitZoneCenter; 
        float yCenter = (-500.0 + 320.0) * 0.5; 
        float yHalfExtents = (320.0 - (-500.0)) * 0.5; 
        
        float currentRadius = max(abs(localPos.x), abs(localPos.z));
        
        
        
        
        vec3 fakedNormal = vec3(
            sign(localPos.x) * pow(abs(localPos.x / currentRadius), 6.0),
            0.0,
            sign(localPos.z) * pow(abs(localPos.z / currentRadius), 6.0)
        );
        
        
        if (length(fakedNormal) < 0.001) fakedNormal = vec3(1.0, 0.0, 0.0);
        
        vec3 normal = normalize(fakedNormal);
        
        float rim = 1.0 - abs(dot(rayDir, normal));
        rim = pow(rim, 1.5); 
        
        
        float baseOpacity = 0.02 + rim * 0.08; 
        float wallOpacity = max(baseOpacity, intersectionGlow * 0.5); 
        
        
        
        
        vec3 brightGlowColor = vec3(1.0, 0.5, 0.95); 
        color = mix(color, brightGlowColor, intersectionGlow * 0.9);
        
        totalOpacity = min(1.0, totalOpacity + wallOpacity);
        
        float heightFade = 1.0 - smoothstep(150.0, 320.0, absoluteHitPos.y);
        totalOpacity *= heightFade;
        
        
        
        if (bestTNear > 0.0) {
            
            float distanceFade = 1.0 - smoothstep(4.0, 24.0, bestTNear);
            totalOpacity *= distanceFade;
        }
        
        
        if (fadeProgress > 0.0) {
            float normalizedY = clamp((absoluteHitPos.y + 64.0) / 384.0, 0.0, 1.0);
            vec3 cellId = floor(absoluteHitPos * 0.7);
            float decayNoise = hash13(cellId) * 0.4;
            float threshold = 1.4 - fadeProgress * 1.8;
            
            if (normalizedY + decayNoise > threshold) {
                
                totalOpacity = max(0.0, totalOpacity - wallOpacity * heightFade);
            } else {
                float edgeDist = threshold - (normalizedY + decayNoise);
                if (edgeDist < 0.08) {
                    color = mix(vec3(1.0, 0.2, 0.8), vec3(1.0, 1.0, 1.0), 1.0 - (edgeDist / 0.08));
                    totalOpacity = 1.0;
                    color = mix(color, vec3(1.0, 0.8, 1.0), 0.5);
                }
            }
        }
    }
    
    if (totalOpacity <= 0.0) {
        fragColor = vec4(0.0);
        return;
    }
    
    fragColor = vec4(color, totalOpacity);
}
