precision mediump float;

varying vec3 ourColor;
varying vec2 texCoord;

// texture sampler
uniform sampler2D containerTexture;

void main() {
    gl_FragColor = texture2D(containerTexture, texCoord) * vec4(ourColor, 1.0);
}