package lunarriver.learn.android.gles.utils

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import java.nio.IntBuffer

class ShaderProgram(
    context: Context,
    vertexPath: String,
    fragmentPath: String
) {
    val program: Int

    init {
        val vertexSource = loadStringFromAsset(context, vertexPath)
        val fragmentSource = loadStringFromAsset(context, fragmentPath)

        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, vertexSource)
        GLES20.glCompileShader(vertexShader)

        val vertexShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, vertexShaderCompileStatus)
        if (vertexShaderCompileStatus.get(0) == 0) {
            Log.d(
                "initShaderProgram",
                "vertexShaderCompileLog: " + GLES20.glGetShaderInfoLog(vertexShader)
            )
        }

        val fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragmentShader, fragmentSource)
        GLES20.glCompileShader(fragmentShader)

        val fragmentShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, fragmentShaderCompileStatus)
        if (fragmentShaderCompileStatus.get(0) == 0) {
            Log.d(
                "initShaderProgram",
                "fragmentShaderCompileLog: " + GLES20.glGetShaderInfoLog(fragmentShader)
            )
        }

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        val programLinkStatus = IntBuffer.allocate(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, programLinkStatus)
        if (programLinkStatus.get(0) == 0) {
            Log.d("initShaderProgram", "programLinkLog: " + GLES20.glGetProgramInfoLog(program))
        }

        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragmentShader)
    }

    fun use() {
        GLES20.glUseProgram(program)
    }

    fun setInt(uniformName: String, value: Int) {
        val location = GLES20.glGetUniformLocation(program, uniformName)
        GLES20.glUniform1i(location, value)
    }

    fun setFloat(uniformName: String, value: Float) {
        val location = GLES20.glGetUniformLocation(program, uniformName)
        GLES20.glUniform1f(location, value)
    }

    fun setMat4(uniformName: String, mat4: FloatArray) {
        val location = GLES20.glGetUniformLocation(program, uniformName)
        GLES20.glUniformMatrix4fv(location, 1, false, mat4, 0)
    }
}