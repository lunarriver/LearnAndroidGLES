attribute vec4 aPos;
attribute vec4 aColor;

varying vec4 ourColor;

void main() {
    gl_Position = aPos;
    ourColor = aColor;
}