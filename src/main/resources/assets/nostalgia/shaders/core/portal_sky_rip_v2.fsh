#version 150












uniform sampler2D Sampler1; 
uniform sampler2D Sampler2; 

layout(std140) uniform WhiteoutData {
    vec4 BeaconAndTimer; 
    mat4 InverseViewProj;
    vec4 ExtraData;
    vec4 CamPosData;
    vec4 SkyColor;
};

in vec2 texCoord0;
out vec4 fragColor;


const float shatterTime       = 2.5;    
const float BULGE_DURATION    = 1.8;    
const float BULGE_MAX         = 4.0;
const float BULGE_RADIUS      = 180.0;
const float textureScale      = 900.0;
const float waveExpansionTime = 0.8;    
const float maxShatterRadius  = 450.0;
const float fadeStartRadius   = 150.0;

const float SHATTER_MAX_RADIUS = 75.0;

const float CRACK_SPREAD_RADIUS = 450.0;

float bulgeProfile(float r) {
    float rn = clamp(r / BULGE_RADIUS, 0.0, 1.0);
    float c = cos(rn * 1.5707963);
    return c * c;
}

float bulgeAmp(float t) {
    float bs = shatterTime - BULGE_DURATION;
    if (t < bs) return 0.0;
    float tt = clamp((t - bs) / BULGE_DURATION, 0.0, 1.0);
    return tt * tt * (3.0 - 2.0 * tt);
}

void main() {
    vec2 ndc = texCoord0 * 2.0 - 1.0;
    vec4 uA = InverseViewProj * vec4(ndc, 0.4, 1.0);
    vec4 uB = InverseViewProj * vec4(ndc, 0.6, 1.0);
    vec3 rayPointA = uA.xyz / uA.w;
    vec3 rayPointB = uB.xyz / uB.w;
    vec3 rayDir = normalize(rayPointB - rayPointA);

    vec3 PortalPos = BeaconAndTimer.xyz;
    float crackPlaneY = PortalPos.y + 90.0;
    float TransitionTime = BeaconAndTimer.w;

    if (abs(rayDir.y) <= 0.001) {
        fragColor = vec4(0.0);
        return;
    }

    
    float bAmp = bulgeAmp(TransitionTime);
    float t_plane = (crackPlaneY - rayPointA.y) / rayDir.y;
    if (t_plane <= 0.01) { fragColor = vec4(0.0); return; }
    vec3 hitPlanePos = rayPointA + rayDir * t_plane;
    for (int bi = 0; bi < 3; bi++) {
        vec2 lp = hitPlanePos.xz - PortalPos.xz;
        float by = BULGE_MAX * bAmp * bulgeProfile(length(lp));
        t_plane = (crackPlaneY + by - rayPointA.y) / rayDir.y;
        if (t_plane <= 0.01) { fragColor = vec4(0.0); return; }
        hitPlanePos = rayPointA + rayDir * t_plane;
    }
    vec2 localPos = hitPlanePos.xz - PortalPos.xz;

    vec2 texUV = localPos / textureScale + vec2(0.5);
    if (texUV.x < 0.0 || texUV.x > 1.0 || texUV.y < 0.0 || texUV.y > 1.0) {
        fragColor = vec4(0.0);
        return;
    }

    vec4 riftData = texture(Sampler2, texUV);

    float edgeDist = (1.0 - riftData.r) * 0.15;

    
    vec2 shardOffset = riftData.gb * 200.0 - vec2(100.0);
    vec2 shardCenterLocal = localPos + shardOffset;
    float physicalCellDist = length(shardCenterLocal);

    vec2 p2 = vec2(dot(shardCenterLocal, vec2(127.1, 311.7)), dot(shardCenterLocal, vec2(269.5, 183.3)));
    float h = fract(sin(p2.x) * 43758.5453);

    float shatterDelay = (physicalCellDist / 75.0) * 0.8 + h * 0.35;
    float rawProgress = (TransitionTime - shatterTime - shatterDelay) / 0.65;
    float shardProgress = clamp(rawProgress, 0.0, 1.0);

    
    if (physicalCellDist > SHATTER_MAX_RADIUS) {
        shardProgress = 0.0;
        rawProgress = -1.0;
    }

    
    if (riftData.a > 0.5) {
        shardProgress = 0.0;
    }

    
    if (shardProgress >= 1.0) {
        fragColor = vec4(0.0);
        return;
    }
    
    else if (shardProgress > 0.0 && rawProgress >= 0.0) {
        fragColor = vec4(0.0);
        return;
    }

    
    float depth = texture(Sampler1, texCoord0).r;
    vec4 ndcPix = vec4(ndc, depth * 2.0 - 1.0, 1.0);
    vec4 unprojPix = InverseViewProj * ndcPix;
    vec3 pixelWorldPos = unprojPix.xyz / unprojPix.w;
    float blockDist = length(pixelWorldPos - rayPointA);
    float crackDist = length(hitPlanePos - rayPointA);
    if (blockDist < crackDist - 0.25) {
        fragColor = vec4(0.0);
        return;
    }

    
    
    float currentWaveTime = max(0.0, TransitionTime - shatterTime);
    float waveRadius = (currentWaveTime / waveExpansionTime) * maxShatterRadius;
    if (currentWaveTime <= 0.001) waveRadius = -1000.0;

    int layers = 24;
    float wallHeight = 12.0;

    float totalCrackAlpha = 0.0;
    vec3 totalCrackColor = vec3(0.0);

    for (int i = 0; i < layers; i++) {
        int layerIndex = (rayDir.y > 0.0) ? i : (layers - 1 - i);
        float hOffset = float(layerIndex) * (wallHeight / float(layers - 1));
        
        float layerBulgeY = BULGE_MAX * bAmp * bulgeProfile(length(localPos));
        float t_layer = (crackPlaneY + hOffset + layerBulgeY - rayPointA.y) / rayDir.y;
        if (t_layer <= 0.01) continue;
        vec2 layerLocalPos = (rayPointA + rayDir * t_layer).xz - PortalPos.xz;
        float layerRawDist = length(layerLocalPos);

        vec2 layerTexUV = layerLocalPos / textureScale + vec2(0.5);
        if (layerTexUV.x < 0.0 || layerTexUV.x > 1.0 || layerTexUV.y < 0.0 || layerTexUV.y > 1.0) continue;

        vec4 layerRiftData = texture(Sampler2, layerTexUV);
        vec2 rotUV = vec2(layerTexUV.y, 1.0 - layerTexUV.x);
        vec4 layerRiftData2 = texture(Sampler2, rotUV);

        float heightRatio = float(layerIndex) / float(layers);
        float alpha1 = pow(layerRiftData.r, 3.0);
        float alpha2 = pow(layerRiftData2.r, 3.0);
        float localAlpha = max(alpha1, alpha2);

        
        
        float radialNorm = clamp(layerRawDist / CRACK_SPREAD_RADIUS, 0.0, 1.0);
        float crackActivation = smoothstep(radialNorm, radialNorm + 0.08, bAmp);
        localAlpha *= crackActivation;

        if (localAlpha > 0.01) {
            float distToWaveFront = layerRawDist - waveRadius;
            float passedFactor = 1.0 - smoothstep(-25.0, 5.0, distToWaveFront);
            float pulse = exp(-pow(distToWaveFront / 25.0, 2.0));

            float globalFade = 1.0 - smoothstep(fadeStartRadius, maxShatterRadius, layerRawDist);

            vec3 baseColor = vec3(1.0);
            vec3 pulseColor = vec3(0.7, 0.95, 1.0);
            vec3 finalColor = mix(baseColor, pulseColor, min(pulse * 1.5, 1.0));

            float intensity = pow(1.0 - heightRatio, 1.5);
            if (layerIndex == 0) intensity = 1.0;

            float currentThicknessAlpha = mix(0.15, 1.15, passedFactor);
            float layerCrackAlpha = min(localAlpha * (currentThicknessAlpha + pulse * 2.8), 1.0) * globalFade * intensity;

            if (layerIndex > 0) layerCrackAlpha *= 0.25;

            totalCrackColor = totalCrackColor + finalColor * layerCrackAlpha * (1.0 - totalCrackAlpha);
            totalCrackAlpha = totalCrackAlpha + layerCrackAlpha * (1.0 - totalCrackAlpha);
        }

        if (totalCrackAlpha > 0.99) break;
    }

    
    for (int flyI = 0; flyI < 10; flyI++) {
        float flyHeight = float(flyI + 1) * 8.0;
        float flyPlaneY = crackPlaneY + flyHeight;
        float t_fly = (flyPlaneY - rayPointA.y) / rayDir.y;
        if (t_fly <= 0.01) continue;

        vec3 flyHitPos = rayPointA + rayDir * t_fly;
        vec2 flyLocalPos = flyHitPos.xz - PortalPos.xz;
        float spreadFactor = 1.0 + flyHeight * 0.015;
        vec2 origLocalPos = flyLocalPos / spreadFactor;

        vec2 flyTexUV = origLocalPos / textureScale + vec2(0.5);
        if (flyTexUV.x < 0.0 || flyTexUV.x > 1.0 || flyTexUV.y < 0.0 || flyTexUV.y > 1.0) continue;

        vec4 flyData = texture(Sampler2, flyTexUV);
        if (flyData.a > 0.5) continue;

        vec2 flyShardOffset = flyData.gb * 200.0 - vec2(100.0);
        vec2 flyShardCenter = origLocalPos + flyShardOffset;
        float flyPhysDist = length(flyShardCenter);

        vec2 flyP2 = vec2(dot(flyShardCenter, vec2(127.1, 311.7)), dot(flyShardCenter, vec2(269.5, 183.3)));
        float flyH = fract(sin(flyP2.x) * 43758.5453);
        float flyDelay = (flyPhysDist / 75.0) * 0.8 + flyH * 0.35;
        float flyRawProgress = (TransitionTime - shatterTime - flyDelay) / 0.65;
        float flyProgress = clamp(flyRawProgress, 0.0, 1.0);

        if (flyProgress <= 0.01 || flyProgress >= 1.0) continue;

        float flyT = flyProgress * 3.0;
        float explosionForce = pow(max(1.0 - flyPhysDist / 80.0, 0.0), 2.0) * 150.0;
        float velY = explosionForce * (0.5 + flyH * 0.8);
        float expectedHeight = velY * flyT - 50.0 * flyT * flyT * 0.5;
        if (abs(expectedHeight - flyHeight) > 12.0) continue;

        float flyEdgeDist = (1.0 - flyData.r) * 0.15;
        float fadeOut = pow(1.0 - flyProgress, 0.7);
        float flash = exp(-flyProgress * 5.0) * 2.0;
        float edgeGlow = exp(-flyEdgeDist * 30.0);

        vec3 shardColor = vec3(0.2, 0.6, 0.9) * fadeOut + vec3(0.9, 0.97, 1.0) * edgeGlow * fadeOut + vec3(1.0) * flash;
        float shardAlpha = min(fadeOut * 0.9 + flash * 0.5 + edgeGlow * 0.2, 1.0) * 0.8;
        shardAlpha *= max(1.0 - flyHeight / 100.0, 0.1);

        totalCrackColor = totalCrackColor + shardColor * shardAlpha * (1.0 - totalCrackAlpha);
        totalCrackAlpha = totalCrackAlpha + shardAlpha * (1.0 - totalCrackAlpha);

        if (totalCrackAlpha > 0.95) break;
    }

    
    float crashT = TransitionTime - shatterTime;
    if (crashT >= 0.0 && crashT < 0.25) {
        float cp = clamp(1.0 - length(localPos) / BULGE_RADIUS, 0.0, 1.0);
        float fb = (1.0 - crashT / 0.25) * (0.3 + cp * 0.7) * 0.9;
        totalCrackColor = totalCrackColor + vec3(1.0) * fb * (1.0 - totalCrackAlpha);
        totalCrackAlpha = totalCrackAlpha + fb * (1.0 - totalCrackAlpha);
    }

    
    if (bAmp > 0.001 && TransitionTime < shatterTime) {
        float cp = clamp(1.0 - length(localPos) / BULGE_RADIUS, 0.0, 1.0);
        float sg = bAmp * pow(cp, 1.5) * 0.25;
        totalCrackColor = totalCrackColor + vec3(0.95, 0.98, 1.0) * sg * (1.0 - totalCrackAlpha);
        totalCrackAlpha = totalCrackAlpha + sg * (1.0 - totalCrackAlpha);
    }

    
    if (totalCrackAlpha > 0.001) {
        fragColor = vec4(totalCrackColor / max(totalCrackAlpha, 0.0001), totalCrackAlpha);
    } else {
        fragColor = vec4(0.0);
    }
}
