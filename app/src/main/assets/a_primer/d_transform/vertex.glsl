attribute vec3 aPos;
attribute vec2 aTexCoord;

varying vec2 texCoord;

uniform mat4 transform;

void main() {
    gl_Position = transform * vec4(aPos, 1.0);
//    gl_Position = vec4(aPos, 1.0);
    texCoord = vec2(aTexCoord.x, aTexCoord.y);
}