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

    float bAmp = bulgeAmp(TransitionTime);

    float R = CamPosData.w + 10.0;
    float bottomY = crackPlaneY;
    float topY = crackPlaneY + 400.0;
    
    vec3 L0 = rayPointA - vec3(PortalPos.x, 0.0, PortalPos.z);
    vec3 D = rayDir;
    
    float A = D.x*D.x + D.z*D.z;
    float B = 2.0 * (L0.x*D.x + L0.z*D.z);
    float C_eq = L0.x*L0.x + L0.z*L0.z - R*R;
    
    float t_cyl1 = -1.0;
    float t_cyl2 = -1.0;
    if (abs(A) > 0.0001) {
        float disc = B*B - 4.0*A*C_eq;
        if (disc >= 0.0) {
            float sqrtDisc = sqrt(disc);
            t_cyl1 = (-B - sqrtDisc) / (2.0*A);
            t_cyl2 = (-B + sqrtDisc) / (2.0*A);
        }
    }
    
    float t_bottom = -1.0;
    if (abs(D.y) > 0.0001) {
        t_bottom = (bottomY - rayPointA.y) / D.y;
    }
    
    float t_top = -1.0;
    if (abs(D.y) > 0.0001) {
        t_top = (topY - rayPointA.y) / D.y;
    }
    
    float min_t = 999999.0;
    int hitFace = 0;
    
    if (t_bottom > 0.01) {
        vec3 p = L0 + D * t_bottom;
        if (p.x*p.x + p.z*p.z <= R*R) {
            min_t = t_bottom;
            hitFace = 1;
        }
    }
    
    if (t_top > 0.01 && t_top < min_t) {
        vec3 p = L0 + D * t_top;
        if (p.x*p.x + p.z*p.z <= R*R) {
            min_t = t_top;
            hitFace = 2;
        }
    }
    
    if (t_cyl1 > 0.01 && t_cyl1 < min_t) {
        float py = rayPointA.y + D.y * t_cyl1;
        if (py >= bottomY && py <= topY) {
            min_t = t_cyl1;
            hitFace = 3;
        }
    }
    
    if (t_cyl2 > 0.01 && t_cyl2 < min_t) {
        float py = rayPointA.y + D.y * t_cyl2;
        if (py >= bottomY && py <= topY) {
            min_t = t_cyl2;
            hitFace = 3;
        }
    }
    
    if (hitFace == 0) discard;
    
    float t_plane = min_t;
    vec3 hitPlanePos = rayPointA + rayDir * t_plane;

    if (hitFace == 1) {
        for (int bi = 0; bi < 3; bi++) {
            vec2 lp = hitPlanePos.xz - PortalPos.xz;
            float profile = bulgeProfile(length(lp));
            float bulgeY = BULGE_MAX * bAmp * profile;  
            float t_new = (bottomY + bulgeY - rayPointA.y) / rayDir.y;
            if (t_new > 0.01) {
                t_plane = t_new;
                hitPlanePos = rayPointA + rayDir * t_plane;
            }
        }
        
        vec2 localPos = hitPlanePos.xz - PortalPos.xz;
        vec2 texUV = localPos / textureScale + vec2(0.5);
        if (texUV.x >= 0.0 && texUV.x <= 1.0 && texUV.y >= 0.0 && texUV.y <= 1.0) {
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
            if (riftData.a > 0.5) shardProgress = 0.0;
            bool isHole = (shardProgress >= 1.0) || (shardProgress > 0.0 && rawProgress >= 0.0);
            if (isHole) discard;
        }
    }

    
    
    float existingDepth = texture(Sampler1, texCoord0).r;
    mat4 ViewProj = inverse(InverseViewProj);
    vec4 clipHit = ViewProj * vec4(hitPlanePos, 1.0);
    float ndcZ = clipHit.z / clipHit.w;
    float planeDepth = ndcZ * 0.5 + 0.5;

    
    if (existingDepth < planeDepth - 0.0005) {
        discard;  
    }

    
    gl_FragDepth = 1.0;
    fragColor = vec4(0.0);
}
