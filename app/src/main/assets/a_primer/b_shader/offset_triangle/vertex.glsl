attribute vec4 aPos;
attribute vec4 aColor;

varying vec4 ourColor;

uniform float horizontalOffset;

void main() {
    gl_Position = vec4(aPos.x + horizontalOffset, aPos.y, aPos.z, aPos.w);
    ourColor = aColor;
}