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


const float shatterTime      = 2.5;
const float BULGE_DURATION   = 1.8;
const float BULGE_MAX        = 4.0;
const float BULGE_RADIUS     = 180.0;
const float textureScale     = 900.0;

float bulgeProfile(float r) {
    float rn = clamp(r / BULGE_RADIUS, 0.0, 1.0);
    float c = cos(rn * 1.5707963);
    return c * c;
}

float bulgeAmp(float t) {
    float bulgeStart = shatterTime - BULGE_DURATION;
    if (t < bulgeStart) return 0.0;
    float tt = clamp((t - bulgeStart) / BULGE_DURATION, 0.0, 1.0);
    return tt * tt * (3.0 - 2.0 * tt);
}

void main() {
    vec2 ndc = texCoord0 * 2.0 - 1.0;
    vec4 unprojA = InverseViewProj * vec4(ndc, 0.4, 1.0);
    vec4 unprojB = InverseViewProj * vec4(ndc, 0.6, 1.0);
    vec3 rayPointA = unprojA.xyz / unprojA.w;
    vec3 rayPointB = unprojB.xyz / unprojB.w;
    vec3 rayDir = normalize(rayPointB - rayPointA);

    vec3 PortalPos = BeaconAndTimer.xyz;
    float crackPlaneY = PortalPos.y + 90.0;
    float TransitionTime = BeaconAndTimer.w;

    if (rayDir.y <= 0.001) discard;

    
    float bAmp = bulgeAmp(TransitionTime);

    float t_plane = (crackPlaneY - rayPointA.y) / rayDir.y;
    if (t_plane <= 0.01) discard;
    vec3 hitPlanePos = rayPointA + rayDir * t_plane;
    for (int bi = 0; bi < 3; bi++) {
        vec2 lp = hitPlanePos.xz - PortalPos.xz;
        float profile = bulgeProfile(length(lp));
        float bulgeY = BULGE_MAX * bAmp * profile;  
        t_plane = (crackPlaneY + bulgeY - rayPointA.y) / rayDir.y;
        if (t_plane <= 0.01) discard;
        hitPlanePos = rayPointA + rayDir * t_plane;
    }
    vec2 localPos = hitPlanePos.xz - PortalPos.xz;

    vec2 texUV = localPos / textureScale + vec2(0.5);
    if (texUV.x < 0.0 || texUV.x > 1.0 || texUV.y < 0.0 || texUV.y > 1.0) {
        discard;
    }

    vec4 riftData = texture(Sampler2, texUV);

    
    vec2 shardOffset = riftData.gb * 200.0 - vec2(100.0);
    vec2 shardCenterLocal = localPos + shardOffset;
    float physicalCellDist = length(shardCenterLocal);

    vec2 p2 = vec2(dot(shardCenterLocal, vec2(127.1, 311.7)), dot(shardCenterLocal, vec2(269.5, 183.3)));
    float h = fract(sin(p2.x) * 43758.5453);

    
    const float SHATTER_MAX_RADIUS = 75.0;
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

    
    bool isHole = (shardProgress >= 1.0)
               || (shardProgress > 0.0 && rawProgress >= 0.0);

    if (isHole) {
        
        discard;
    }

    
    
    mat4 ViewProj = inverse(InverseViewProj);
    vec4 clipHit = ViewProj * vec4(hitPlanePos, 1.0);
    float ndcZ = clipHit.z / clipHit.w;
    gl_FragDepth = ndcZ * 0.5 + 0.5;

    fragColor = vec4(0.0);  
}
