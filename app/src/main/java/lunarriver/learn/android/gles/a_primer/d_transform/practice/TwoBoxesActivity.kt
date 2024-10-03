package lunarriver.learn.android.gles.a_primer.d_transform.practice

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.utils.BaseGLSurfaceActivity
import lunarriver.learn.android.gles.utils.ShaderProgram
import lunarriver.learn.android.gles.utils.generateByteBuffersFromBitmap
import lunarriver.learn.android.gles.utils.generateMat4fv
import lunarriver.learn.android.gles.utils.loadBitmapFromAsset
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.sin

class TwoBoxesActivity: BaseGLSurfaceActivity() {
    companion object {
        // 三角形顶点
        val VERTICES = floatArrayOf(
            // positions          // texture coords
            0.5f, 0.5f, 0.0f,     1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f,    1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f,    0.0f, 1.0f  // top left
        )

        val INDICES = intArrayOf(
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
        )

        // 顶点使用的float数目
        const val FLOATS_OF_VERTEX = 3
        const val FLOATS_OF_TEXTURE_COORDINATE = 2

        // float占据的字节数
        const val BYTES_OF_FLOAT = 4
        const val BYTES_OF_INT = 4
    }

    private lateinit var shaderProgram: ShaderProgram

    private var containerTexture: Int = -1
    private var faceTexture: Int = -1

    private var rotateAngle = 0f

    override fun layoutResource() = R.layout.activity_only_glsurfaceview

    override fun onSurfaceCreated() {
        shaderProgram = ShaderProgram(
            this@TwoBoxesActivity,
            "a_primer/d_transform/vertex.glsl",
            "a_primer/d_transform/fragment.glsl"
        )
        initVertexBuffer()

        containerTexture = initTexture {
            val bitmap = loadBitmapFromAsset(this, "a_primer/c_texture/basic/container.jpg")!!
            val byteBuffer = generateByteBuffersFromBitmap(bitmap)

            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, bitmap.width, bitmap.height,
                0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, byteBuffer)
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        }

        faceTexture = initTexture {
            val origin = loadBitmapFromAsset(this, "a_primer/c_texture/face/face.png")!!
            val matrix = android.graphics.Matrix().apply {
                postRotate(180f)
            }
            val bitmap = Bitmap.createBitmap(origin, 0, 0, origin.width, origin.height, matrix, true)

            val byteBuffer = generateByteBuffersFromBitmap(bitmap)

            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, bitmap.width, bitmap.height,
                0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, byteBuffer)
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        }
    }

    private fun initVertexBuffer() {
        // VBO
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

        // EBO
        val ebo = IntBuffer.allocate(1)
        GLES20.glGenBuffers(1, ebo)

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo[0])

        val elementData = IntBuffer.wrap(INDICES)
        GLES20.glBufferData(
            GLES20.GL_ELEMENT_ARRAY_BUFFER,
            INDICES.size * BYTES_OF_INT,
            elementData,
            GLES20.GL_STATIC_DRAW
        )
    }

    private fun initTexture(bindData: () -> Unit): Int {
        val textureBuffer = IntBuffer.allocate(1)
        GLES20.glGenTextures(1, textureBuffer)
        val texture = textureBuffer[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        bindData()

        return texture
    }

    override fun onDrawFrame() {
        GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        shaderProgram.use()

        rotateAngle += 2.5f

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, containerTexture)
        shaderProgram.setInt("containerTexture", 0)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, faceTexture)
        shaderProgram.setInt("faceTexture", 1)

        val aPosLocation = GLES20.glGetAttribLocation(shaderProgram.program, "aPos")
        GLES20.glEnableVertexAttribArray(aPosLocation)
        GLES20.glVertexAttribPointer(
            aPosLocation,
            FLOATS_OF_VERTEX, GLES20.GL_FLOAT, false,
            (FLOATS_OF_VERTEX + FLOATS_OF_TEXTURE_COORDINATE) * BYTES_OF_FLOAT,
            0
        )

        val aTexCoordLocation = GLES20.glGetAttribLocation(shaderProgram.program, "aTexCoord")
        GLES20.glEnableVertexAttribArray(aTexCoordLocation)
        GLES20.glVertexAttribPointer(
            aTexCoordLocation,
            FLOATS_OF_TEXTURE_COORDINATE, GLES20.GL_FLOAT, false,
            (FLOATS_OF_VERTEX + FLOATS_OF_TEXTURE_COORDINATE) * BYTES_OF_FLOAT,
            (FLOATS_OF_VERTEX) * BYTES_OF_FLOAT
        )

        val mat4_1 = generateMat4fv()
        Matrix.translateM(mat4_1, 0, 0.5f, -0.5f, 0f)
        Matrix.rotateM(mat4_1, 0, rotateAngle, 0f, 0f, 1f)
        shaderProgram.setMat4("transform", mat4_1)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            6,
            GLES20.GL_UNSIGNED_INT,
            0
        )

        val mat4_2 = generateMat4fv()
        Matrix.translateM(mat4_2, 0, -0.5f, 0.5f, 0f)
        val scaleFactor = sin(rotateAngle * 10)
        Matrix.scaleM(mat4_2, 0, scaleFactor, scaleFactor, scaleFactor)
        shaderProgram.setMat4("transform", mat4_2)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            6,
            GLES20.GL_UNSIGNED_INT,
            0
        )
    }
}