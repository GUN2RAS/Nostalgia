#version 150

uniform sampler2D Sampler0;
uniform sampler2D DepthSampler;

uniform mat4 InverseProjMat;
uniform vec3 WaveCenter; 
uniform float WaveRadius;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float depth = texture(DepthSampler, texCoord).r;
    
    
    vec4 ndc = vec4(texCoord.x * 2.0 - 1.0, texCoord.y * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    
    
    vec4 viewPos = InverseProjMat * ndc;
    viewPos /= viewPos.w; 
    
    
    float dist = distance(viewPos.xyz, WaveCenter);
    
    
    
    float intensity = 1.0 - smoothstep(WaveRadius - 10.0, WaveRadius, dist);
    
    
    if (depth >= 0.99999) {
        
        
    }
    
    fragColor = vec4(1.0, 1.0, 1.0, clamp(intensity, 0.0, 1.0));
}
