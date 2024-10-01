attribute vec4 aPos;

varying vec4 ourColor;

void main() {
    gl_Position = aPos;
    ourColor = aPos;
}