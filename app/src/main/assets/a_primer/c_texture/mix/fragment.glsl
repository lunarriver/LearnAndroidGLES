precision mediump float;

varying vec2 texCoord;

// texture sampler
uniform sampler2D containerTexture;
uniform sampler2D faceTexture;

uniform float ratio;

void main() {
    gl_FragColor = mix(texture2D(containerTexture, texCoord), texture2D(faceTexture, texCoord), ratio);
}