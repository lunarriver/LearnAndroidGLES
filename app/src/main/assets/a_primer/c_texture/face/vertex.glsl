attribute vec3 aPos;
attribute vec2 aTexCoord;

varying vec2 texCoord;

void main() {
    gl_Position = vec4(aPos, 1.0);
    texCoord = vec2(aTexCoord.x, aTexCoord.y);
}