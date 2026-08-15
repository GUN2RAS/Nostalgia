#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform mat4 InverseProjMat;
uniform mat4 InverseViewMat;
uniform vec3 WaveCenter;
uniform float WaveRadius;
uniform float WhiteoutAlpha;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(DiffuseSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;

    
    vec4 ndc = vec4(texCoord.x * 2.0 - 1.0, texCoord.y * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    
    
    vec4 viewPos = InverseProjMat * ndc;
    viewPos /= viewPos.w; 
    
    
    
    vec4 worldPos = InverseViewMat * viewPos;
    
    
    float dist = distance(worldPos.xyz, WaveCenter);
    
    
    float waveEdge = smoothstep(WaveRadius - 5.0, WaveRadius + 0.5, dist);
    
    float tintIntensity = 1.0 - waveEdge;
    
    
    float totalIntensity = min(1.0, tintIntensity + WhiteoutAlpha);

    
    if (depth >= 0.99999) {
        
        totalIntensity = WhiteoutAlpha;
    }

    fragColor = mix(texColor, vec4(1.0, 1.0, 1.0, 1.0), totalIntensity);
}
