#version 150











uniform sampler2D Sampler1; 

layout(std140) uniform GlassData {
    vec4 Info1;          
    mat4 InverseViewProj;
    vec4 GlassDims;      
    vec4 Pad;            
    vec4 SkyColor;
};


bool isHorizontal() { return Pad.w > 0.5; }





vec3 worldToGlassLocal(vec3 v) {
    return isHorizontal() ? v.xzy : v;
}




float bulgeSign() { return isHorizontal() ? 1.0 : -1.0; }

in vec2 texCoord0;
out vec4 fragColor;


const int MAX_CX = 5;
const int MAX_CY = 3;

const float EDGE_WIDTH = 0.12;


const float MAX_BULGE = 1.2;

const float BULGE_DURATION = 1.2;




float bulgeProfile(vec2 uv) {
    vec2 centerB = vec2(GlassDims.x, GlassDims.y) * 0.5;
    float r = length(uv - centerB);
    float rMax = min(GlassDims.x, GlassDims.y) * 0.5;
    float n = clamp(r / rMax, 0.0, 1.0);
    float c = cos(n * 1.5707963); 
    return c * c;
}



float bulgeAmp() {
    float totalTime = Pad.z;
    float intactTime = Pad.x;
    float bulgeStart = intactTime - BULGE_DURATION;
    if (totalTime < bulgeStart) return 0.0;
    float t = clamp((totalTime - bulgeStart) / BULGE_DURATION, 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}


float computeBulgeZ(vec2 uv) {
    return bulgeSign() * MAX_BULGE * bulgeAmp() * bulgeProfile(uv);
}


vec2 rand(vec2 v) {
    vec2 r;
    r.x = fract(sin(v.x) * 43758.5453);
    r.y = fract(sin(v.y) * 1234.5678);
    return r;
}




vec2 pointInCell(ivec2 ic) {
    float blockW = GlassDims.x;
    float blockH = GlassDims.y;
    float cellsX = GlassDims.z;
    float cellsY = GlassDims.w;
    vec2 cellSize = vec2(blockW / cellsX, blockH / cellsY);
    vec2 centerB = vec2(blockW, blockH) * 0.5;

    
    vec2 cellNominal = (vec2(ic) + vec2(0.5)) * cellSize;
    vec2 vd = cellNominal - centerB;
    float rMax = min(blockW, blockH) / 2.0;
    float r = length(vd);
    float d = min(r / rMax, 1.0);
    float fScale = pow(d, 4.0);

    
    vec2 vRand = (rand(vec2(ic)) * 0.8 + 0.1) * cellSize;

    return centerB + (vd + vRand) * fScale;
}




vec2 cellOffsetBlockSpace(ivec2 c) {
    float blockW = GlassDims.x;
    float blockH = GlassDims.y;
    float cellsX = GlassDims.z;
    float cellsY = GlassDims.w;
    vec2 cellSize = vec2(blockW / cellsX, blockH / cellsY);
    vec2 centerB = vec2(blockW, blockH) * 0.5;
    vec2 cellNominal = (vec2(c) + vec2(0.5)) * cellSize;
    return cellNominal - centerB;
}



vec3 moveOverTime(ivec2 c) {
    vec2 cOff = cellOffsetBlockSpace(c);
    float t = Info1.w;

    
    
    float centerProx = clamp(1.0 - length(cOff) / 5.0, 0.0, 1.0);
    float magnitude = pow(centerProx, 1.5) * 1.4; 

    
    
    vec2 randOff = (rand(vec2(c)) - 0.5) * 2.0;
    vec2 radialDir = normalize(cOff + randOff * 0.35 + vec2(0.001));
    
    vec3 burstDir = normalize(vec3(radialDir * 1.5, -0.3));

    
    float settle = 1.0 - exp(-t * 3.0);
    vec3 burst = burstDir * magnitude * settle;

    
    vec3 creep = vec3(0.0, 0.0, -0.04) * t;

    return burst + creep;
}

mat3 matAxisAngle(vec3 axis, float a) {
    float s = sin(a);
    float c = cos(a);
    vec3 as = axis * axis;
    mat3 m;
    m[0][0] = as.x + (1.0 - as.x) * c;
    m[0][1] = axis.x * axis.y * (1.0 - c) + axis.z * s;
    m[0][2] = axis.z * axis.x * (1.0 - c) - axis.y * s;
    m[1][0] = axis.x * axis.y * (1.0 - c) - axis.z * s;
    m[1][1] = as.y + (1.0 - as.y) * c;
    m[1][2] = axis.y * axis.z * (1.0 - c) + axis.x * s;
    m[2][0] = axis.z * axis.x * (1.0 - c) + axis.y * s;
    m[2][1] = axis.y * axis.z * (1.0 - c) - axis.x * s;
    m[2][2] = as.z + (1.0 - as.z) * c;
    return m;
}



mat3 rotationOverTime(ivec2 c) {
    vec2 cOff = cellOffsetBlockSpace(c);
    vec3 axis = normalize(vec3(cOff.y, -cOff.x, 0.0) + vec3(0.001));
    float magnitude = max(4.0 - length(cOff), 0.0);
    magnitude = pow(magnitude, 2.0) * 0.05; 
    float t = Info1.w;
    float settle = 1.0 - exp(-t * 2.5);
    return matAxisAngle(axis, magnitude * settle);
}





void piecePlane(ivec2 c, out vec4 pl, out vec3 center, out vec3 px, out vec3 py) {
    vec2 originXY = pointInCell(c);
    vec3 pieceOrigin = vec3(originXY, computeBulgeZ(originXY));
    mat3 mr = rotationOverTime(c);
    vec3 move = moveOverTime(c);
    vec3 finalCenter = pieceOrigin + move;

    vec3 pz = mr[2];
    px = mr[0];
    py = mr[1];
    pl = vec4(pz, -dot(pz, finalCenter));
    center = finalCenter;
}


float voronoi(vec2 pos, ivec2 ic) {
    float mind = 1e6;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            vec2 pc = pointInCell(ic + ivec2(i, j));
            vec2 vd = pc - pos;
            float d = dot(vd, vd);
            mind = min(d, mind);
        }
    }
    return sqrt(mind);
}


vec2 voronoiTwoNearest(vec2 pos, ivec2 ic) {
    float d1 = 1e6, d2 = 1e6;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            vec2 pc = pointInCell(ic + ivec2(i, j));
            float d = length(pc - pos);
            if (d < d1) { d2 = d1; d1 = d; }
            else if (d < d2) { d2 = d; }
        }
    }
    return vec2(d1, d2);
}




bool rayHitPiece(vec3 vo, vec3 vd, ivec2 c, out float dist, out vec3 norm, out float edgeDist) {
    vec4 plane;
    vec3 center, px, py;
    piecePlane(c, plane, center, px, py);

    float dn = dot(vd, plane.xyz);
    if (abs(dn) < 0.0001) return false;
    dist = (-plane.w - dot(vo, plane.xyz)) / dn;
    if (dist < 0.001) return false;

    vec3 p = vo + dist * vd;
    vec3 pieceOrigin = vec3(pointInCell(c), 0.0);
    vec3 pLocal = p - center;
    vec2 vg = vec2(dot(pLocal, px), dot(pLocal, py)) + pieceOrigin.xy;

    if (vg.x < 0.0 || vg.x > GlassDims.x ||
        vg.y < 0.0 || vg.y > GlassDims.y) {
        return false;
    }

    
    vec2 nd = voronoiTwoNearest(vg, c);
    float d1 = nd.x;
    float d2 = nd.y;

    
    vec2 vc = pointInCell(c);
    float distToOur = length(vc - vg);
    if (abs(distToOur - d1) > 0.01) return false;

    norm = plane.xyz;
    edgeDist = d2 - d1; 
    return true;
}




void renderSolidGlass(vec2 ndc, vec3 rayPointA, vec3 rayDir, vec3 glassOriginRel) {
    float blockW = GlassDims.x;
    float blockH = GlassDims.y;
    float totalTime = Pad.z;
    float intactTime = Pad.x;
    float globalFade = Pad.y;

    
    vec3 voLoc = worldToGlassLocal(rayPointA - glassOriginRel);
    vec3 vdLoc = worldToGlassLocal(rayDir);

    if (abs(vdLoc.z) < 0.0001) { fragColor = vec4(0.0); return; }

    
    
    
    float tHit = -voLoc.z / vdLoc.z;
    if (tHit < 0.001) { fragColor = vec4(0.0); return; }
    vec3 hit = voLoc + tHit * vdLoc;
    for (int iter = 0; iter < 3; iter++) {
        float bZ = computeBulgeZ(hit.xy);
        tHit = (bZ - voLoc.z) / vdLoc.z;
        if (tHit < 0.001) { fragColor = vec4(0.0); return; }
        hit = voLoc + tHit * vdLoc;
    }

    if (hit.x < 0.0 || hit.x > blockW || hit.y < 0.0 || hit.y > blockH) {
        fragColor = vec4(0.0);
        return;
    }

    
    float depth = texture(Sampler1, texCoord0).r;
    vec4 ndcPix = vec4(ndc, depth * 2.0 - 1.0, 1.0);
    vec4 unprojPix = InverseViewProj * ndcPix;
    vec3 pixelWorldPos = unprojPix.xyz / unprojPix.w;
    float worldBlockDist = length(pixelWorldPos - rayPointA);
    if (worldBlockDist < tHit * 0.9) { fragColor = vec4(0.0); return; }

    vec2 uv = hit.xy;

    
    
    float eps = 0.1;
    float bZ  = computeBulgeZ(uv);
    float bZx = computeBulgeZ(uv + vec2(eps, 0.0));
    float bZy = computeBulgeZ(uv + vec2(0.0, eps));
    vec3 surfaceNormal = normalize(vec3(-(bZx - bZ) / eps, -(bZy - bZ) / eps, 1.0));

    float fresnel = pow(1.0 - abs(dot(-vdLoc, surfaceNormal)), 2.0);

    
    float borderDist = min(min(uv.x, blockW - uv.x), min(uv.y, blockH - uv.y));
    float border = 1.0 - smoothstep(0.0, 0.12, borderDist);

    
    float tensionAmp = bulgeAmp();

    
    
    
    
    float tensionCrack = 0.0;
    float freshFlash = 0.0;
    if (tensionAmp > 0.001) {
        float cellSizeX = blockW / GlassDims.z;
        float cellSizeY = blockH / GlassDims.w;
        ivec2 ic = ivec2(floor(uv.x / cellSizeX), floor(uv.y / cellSizeY));
        vec2 nd = voronoiTwoNearest(uv, ic);
        float edgeToVoronoi = nd.y - nd.x;
        float crackLine = 1.0 - smoothstep(0.0, EDGE_WIDTH, edgeToVoronoi);

        if (crackLine > 0.01) {
            
            vec2 centerB = vec2(blockW, blockH) * 0.5;
            float distFromImpact = length(uv - centerB);
            float rMax = length(centerB);
            float radialNorm = distFromImpact / rMax;

            
            float jitter = (rand(floor(uv * 2.5)).x - 0.5) * 0.25;
            float activation = clamp(radialNorm + jitter, 0.0, 1.0);

            
            float activated = smoothstep(activation, activation + 0.02, tensionAmp);

            
            float birthWindow = 0.12;
            float fresh = smoothstep(activation + birthWindow, activation, tensionAmp);

            tensionCrack = crackLine * activated * 0.85;
            freshFlash = crackLine * fresh * activated * 1.5;
        }
    }

    float pulse = 0.0;
    if (tensionAmp > 0.001) {
        float freq = 2.0 + tensionAmp * 8.0;
        pulse = (sin(totalTime * freq * 6.2831853) * 0.5 + 0.5) * tensionAmp;
    }

    
    float bulgeA = bulgeAmp();
    float bulgeLocal = bulgeProfile(uv);
    float stretchGlow = bulgeA * bulgeLocal * 0.55;

    vec3 bodyColor = vec3(0.88, 0.94, 1.0);
    vec3 col = bodyColor * 0.38
             + vec3(1.0) * fresnel * 0.45
             + vec3(1.0) * border * 0.75
             + vec3(1.0) * tensionCrack * 0.95
             + vec3(1.0) * freshFlash * 1.2
             + vec3(0.85, 0.92, 1.0) * pulse * 0.25
             + vec3(0.95, 0.98, 1.0) * stretchGlow;

    float alpha = 0.48 + fresnel * 0.28 + border * 0.55 + tensionCrack * 0.8 + freshFlash * 0.4 + pulse * 0.2 + stretchGlow * 0.3;
    alpha = min(alpha, 0.97) * globalFade;

    fragColor = vec4(col, alpha);
}




void renderShatteredGlass(vec2 ndc, vec3 rayPointA, vec3 rayDir, vec3 glassOriginRel) {
    
    vec3 voLoc = worldToGlassLocal(rayPointA - glassOriginRel);
    vec3 vdLoc = worldToGlassLocal(rayDir);

    float depth = texture(Sampler1, texCoord0).r;
    vec4 ndcPix = vec4(ndc, depth * 2.0 - 1.0, 1.0);
    vec4 unprojPix = InverseViewProj * ndcPix;
    vec3 pixelWorldPos = unprojPix.xyz / unprojPix.w;
    float worldBlockDist = length(pixelWorldPos - rayPointA);

    float bestDist = 1e6;
    bool hit = false;
    vec3 bestNorm;
    float bestEdge = 0.0;

    
    for (int cy = -1; cy < MAX_CY; cy++) {
        for (int cx = -1; cx < MAX_CX; cx++) {
            float d;
            vec3 n;
            float e;
            if (rayHitPiece(voLoc, vdLoc, ivec2(cx, cy), d, n, e)) {
                if (d < bestDist) {
                    bestDist = d;
                    bestNorm = n;
                    bestEdge = e;
                    hit = true;
                }
            }
        }
    }

    if (!hit) {
        fragColor = vec4(0.0);
        return;
    }

    if (worldBlockDist < bestDist * 0.9) {
        fragColor = vec4(0.0);
        return;
    }

    float crashTime = Info1.w;
    float globalFade = Pad.y;

    
    
    float rawEdge = 1.0 - smoothstep(0.0, EDGE_WIDTH, bestEdge);
    float edgeFactor = rawEdge;

    float fresnel = pow(1.0 - max(dot(-rayDir, bestNorm), 0.0), 2.0);

    vec3 bodyColor = vec3(0.88, 0.94, 1.0);
    vec3 rimColor = vec3(1.0);
    vec3 col = bodyColor * 0.22
             + rimColor * edgeFactor * 0.95
             + vec3(1.0) * fresnel * 0.45;

    float bodyAlpha = 0.14 + fresnel * 0.28;
    float edgeAlpha = edgeFactor * 0.95;
    float alpha = min(bodyAlpha + edgeAlpha, 0.92);

    
    if (crashTime < 0.25) {
        float flash = 1.0 - crashTime / 0.25;
        col += vec3(1.0) * flash * 0.9;
        alpha = min(alpha + flash * 0.35, 0.98);
    }

    alpha *= globalFade;

    fragColor = vec4(col, alpha);
}


void main() {
    vec2 ndc = texCoord0 * 2.0 - 1.0;
    vec4 unprojA = InverseViewProj * vec4(ndc, 0.4, 1.0);
    vec4 unprojB = InverseViewProj * vec4(ndc, 0.6, 1.0);
    vec3 rayPointA = unprojA.xyz / unprojA.w;
    vec3 rayPointB = unprojB.xyz / unprojB.w;
    vec3 rayDir = normalize(rayPointB - rayPointA);

    float globalFade = Pad.y;
    if (globalFade < 0.001) { fragColor = vec4(0.0); return; }

    vec3 glassCenterRel = Info1.xyz;
    
    
    vec3 halfSize = isHorizontal()
        ? vec3(GlassDims.x * 0.5, 0.0, GlassDims.y * 0.5)
        : vec3(GlassDims.x * 0.5, GlassDims.y * 0.5, 0.0);
    vec3 glassOriginRel = glassCenterRel - halfSize;

    float crashTime = Info1.w;
    if (crashTime > 0.0) {
        renderShatteredGlass(ndc, rayPointA, rayDir, glassOriginRel);
    } else {
        renderSolidGlass(ndc, rayPointA, rayDir, glassOriginRel);
    }
}
