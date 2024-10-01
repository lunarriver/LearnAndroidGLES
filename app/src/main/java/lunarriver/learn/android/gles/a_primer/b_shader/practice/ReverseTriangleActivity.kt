package lunarriver.learn.android.gles.a_primer.b_shader.practice

import android.opengl.GLES20
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.utils.BaseGLSurfaceActivity
import lunarriver.learn.android.gles.utils.ShaderProgram
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ReverseTriangleActivity : BaseGLSurfaceActivity() {
    companion object {
        // 三角形顶点
        val VERTICES = floatArrayOf(
            // positions         // colors
            0.5f, 0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  // top right
            -0.5f, 0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  // top left
            0.0f,  -0.5f, 0.0f,  0.0f, 0.0f, 1.0f   // bottom
        )

        // 顶点使用的float数目
        const val FLOATS_OF_VERTEX = 3
        const val FLOATS_OF_COLOR = 3

        // float占据的字节数
        const val BYTES_OF_FLOAT = 4
    }

    private lateinit var shaderProgram: ShaderProgram

    override fun layoutResource() = R.layout.activity_only_glsurfaceview

    override fun onSurfaceCreated() {
        shaderProgram = ShaderProgram(
            this@ReverseTriangleActivity,
            "a_primer/b_shader/shaders_interpolation/vertex.glsl",
            "a_primer/b_shader/shaders_interpolation/fragment.glsl"
        )
        initVertexBuffer()
    }

    private fun initVertexBuffer() {
        val vbo = IntBuffer.allocate(1)
        GLES20.glGenBuffers(1, vbo)

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0])

        val vertexData = FloatBuffer.wrap(VERTICES)
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            VERTICES.size * BYTES_OF_FLOAT,
            vertexData,
            GLES20.GL_STATIC_DRAW
        )
    }

    override fun onDrawFrame() {
        GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        shaderProgram.use()

        val aPosLocation = GLES20.glGetAttribLocation(shaderProgram.program, "aPos")
        GLES20.glEnableVertexAttribArray(aPosLocation)
        GLES20.glVertexAttribPointer(
            aPosLocation, FLOATS_OF_VERTEX, GLES20.GL_FLOAT, false,
            (FLOATS_OF_VERTEX + FLOATS_OF_COLOR) * BYTES_OF_FLOAT, 0
        )

        val aColorPosition = GLES20.glGetAttribLocation(shaderProgram.program, "aColor")
        GLES20.glEnableVertexAttribArray(aColorPosition)
        GLES20.glVertexAttribPointer(
            aColorPosition, FLOATS_OF_COLOR, GLES20.GL_FLOAT, false,
            (FLOATS_OF_VERTEX + FLOATS_OF_COLOR) * BYTES_OF_FLOAT,
            FLOATS_OF_VERTEX * BYTES_OF_FLOAT
        )

        GLES20.glDrawArrays(
            GLES20.GL_TRIANGLES, 0,
            VERTICES.size / (FLOATS_OF_VERTEX + FLOATS_OF_COLOR))
    }
}