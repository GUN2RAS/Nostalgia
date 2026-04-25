#version 150

out vec2 texCoord0;

uniform Projection {
    mat4 projectionMatrix;
};

void main() {
    float x = -1.0 + float((gl_VertexID & 1) << 2);
    float y = -1.0 + float((gl_VertexID & 2) << 1);
    
    gl_Position = vec4(x, y, 0.0, 1.0);
    texCoord0 = vec2((x + 1.0) * 0.5, (y + 1.0) * 0.5);
}
