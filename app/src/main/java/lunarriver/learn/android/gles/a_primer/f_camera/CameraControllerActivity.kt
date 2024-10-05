package lunarriver.learn.android.gles.a_primer.f_camera

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import android.os.Bundle
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.utils.BaseGLSurfaceActivity
import lunarriver.learn.android.gles.utils.CameraController
import lunarriver.learn.android.gles.utils.MoveAction
import lunarriver.learn.android.gles.utils.ShaderProgram
import lunarriver.learn.android.gles.utils.generateByteBuffersFromBitmap
import lunarriver.learn.android.gles.utils.generateMat4fv
import lunarriver.learn.android.gles.utils.getScreenAspectRatio
import lunarriver.learn.android.gles.utils.loadBitmapFromAsset
import lunarriver.learn.android.gles.utils.vec3MultiplyNum
import lunarriver.learn.android.gles.utils.vec3Plus
import lunarriver.learn.android.gles.utils.vec3cross
import lunarriver.learn.android.gles.utils.vec3normalize
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CameraControllerActivity: BaseGLSurfaceActivity() {
    companion object {
        // 三角形顶点
        val VERTICES = floatArrayOf(
            // positions          // texture coords
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        )

        val CUBE_POSITIONS = listOf(
            floatArrayOf( 0.0f,  0.0f,  0.0f),
            floatArrayOf( 2.0f,  5.0f, -15.0f),
            floatArrayOf(-1.5f, -2.2f, -2.5f),
            floatArrayOf(-3.8f, -2.0f, -12.3f),
            floatArrayOf( 2.4f, -0.4f, -3.5f),
            floatArrayOf(-1.7f,  3.0f, -7.5f),
            floatArrayOf( 1.3f, -2.0f, -2.5f),
            floatArrayOf( 1.5f,  2.0f, -2.5f),
            floatArrayOf( 1.5f,  0.2f, -1.5f),
            floatArrayOf(-1.3f,  1.0f, -1.5f)
        )

        // 顶点使用的float数目
        const val FLOATS_OF_VERTEX = 3
        const val FLOATS_OF_TEXTURE_COORDINATE = 2

        // float占据的字节数
        const val BYTES_OF_FLOAT = 4
        const val BYTES_OF_INT = 4

        const val ANGLE_SHIFT_SENSITIVITY = 0.05f
        const val MOVE_SENSITIVITY = 0.01f
    }

    private lateinit var shaderProgram: ShaderProgram

    private var containerTexture: Int = -1
    private var faceTexture: Int = -1

    private var fov = 45f
    private var aspect = 1f

    private var cameraPos = floatArrayOf(0f, 0f, 3f)
    private var cameraFront = floatArrayOf(0f, 0f, -1f)
    private var cameraUp = floatArrayOf(0f, 1f, 0f)

    private var yaw = -90f
    private var pitch = 0f

    private var deltaTime = 0L
    private var lastFrame = 0L

    override fun layoutResource() = R.layout.activity_camera_controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lastFrame = System.currentTimeMillis()

        val cameraController: CameraController = findViewById(R.id.cameraController)
        cameraController.setOnActionListener(object: CameraController.OnActionListener {
            override fun onAngleShift(xOffset: Float, yOffset: Float) {
                yaw += xOffset * ANGLE_SHIFT_SENSITIVITY
                pitch += yOffset * ANGLE_SHIFT_SENSITIVITY

                if (pitch > 89.0f) {
                    pitch = 89.0f
                }
                if (pitch < -89.0f) {
                    pitch = -89.0f
                }

                cameraFront = vec3normalize(
                    floatArrayOf(
                        (cos(yaw * PI / 180) * cos(pitch * PI / 180)).toFloat(),
                        sin(pitch * PI / 180).toFloat(),
                        (sin(yaw * PI / 180) * cos(pitch * PI / 180)).toFloat()
                    )
                )
            }

            override fun onMove(action: MoveAction) {
                when (action) {
                    MoveAction.FORWARD -> {
                        cameraPos = vec3Plus(cameraPos, vec3MultiplyNum(cameraFront, MOVE_SENSITIVITY * deltaTime))
                    }
                    MoveAction.BACKWARD -> {
                        cameraPos = vec3Plus(cameraPos, vec3MultiplyNum(cameraFront, -MOVE_SENSITIVITY * deltaTime))
                    }
                    MoveAction.LEFT -> {
                        cameraPos = vec3Plus(
                            cameraPos,
                            vec3MultiplyNum(
                                vec3normalize(vec3cross(cameraFront, cameraUp)),
                                -MOVE_SENSITIVITY * deltaTime
                            )
                        )
                    }
                    MoveAction.RIGHT -> {
                        cameraPos = vec3Plus(
                            cameraPos,
                            vec3MultiplyNum(
                                vec3normalize(vec3cross(cameraFront, cameraUp)),
                                MOVE_SENSITIVITY * deltaTime
                            )
                        )
                    }
                }
            }

            override fun onFovChange(value: Float) {
                fov = value
            }
        })
    }

    override fun onSurfaceCreated() {
        aspect = getScreenAspectRatio(this)

        shaderProgram = ShaderProgram(
            this@CameraControllerActivity,
            "a_primer/f_camera/vertex.glsl",
            "a_primer/f_camera/fragment.glsl"
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

        // 启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
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
        val currentTime = System.currentTimeMillis()
        deltaTime = currentTime - lastFrame
        lastFrame = currentTime

        GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        shaderProgram.use()

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

        // 投影矩阵
        val projection = generateMat4fv()
        Matrix.perspectiveM(projection, 0, fov, aspect, 0.1f, 100f)
        shaderProgram.setMat4("projection", projection)

        // 观察矩阵
        val view = generateMat4fv()
        Matrix.setLookAtM(view, 0,
            cameraPos[0], cameraPos[1], cameraPos[2],
            cameraPos[0] + cameraFront[0], cameraPos[1] + cameraFront[1], cameraPos[2] + cameraFront[2],
            cameraUp[0], cameraUp[1], cameraUp[2]
        )
        shaderProgram.setMat4("view", view)

        CUBE_POSITIONS.forEachIndexed { index, position ->
            // 模型矩阵
            val model = generateMat4fv()
            // 定位
            Matrix.translateM(model, 0, position[0], position[1], position[2])
            // 旋转
            Matrix.rotateM(model, 0, 20f * index, 1f, 0.3f, 0.5f)
            shaderProgram.setMat4("model", model)

            GLES20.glDrawArrays(
                GLES20.GL_TRIANGLES,
                0,
                VERTICES.size / (FLOATS_OF_VERTEX + FLOATS_OF_TEXTURE_COORDINATE)
            )
        }
    }
}