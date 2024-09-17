package lunarriver.learn.android.gles

import android.app.ActivityManager
import android.opengl.GLES20
import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glGenBuffers
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HelloTriangleActivity: ComponentActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    private val vertexShaderSource = """
        attribute vec4 vPosition;
        void main() {
            gl_Position = vPosition;
        }
    """.trimIndent()

    private val fragmentShaderSource = """
        precision mediump float;
        void main() {
            gl_FragColor = vec4(1.0, 0.5, 0.2, 1.0);
        }
    """.trimIndent()

    private var program: Int = -1

    private lateinit var vertexBuffer: FloatBuffer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_triangle)

        Log.d("HelloTriangleActivity", "supportGLES20: ${supportGLES20()}")

        glSurfaceView = findViewById(R.id.glSurfaceView)
        glSurfaceView.setEGLContextClientVersion(2)

        glSurfaceView.setRenderer(object: GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                initVertexBuffer()
                program = initShaderProgram()
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
                GLES20.glClear(GL_COLOR_BUFFER_BIT)

                GLES20.glUseProgram(program)
                val vPosition = GLES20.glGetAttribLocation(program, "vPosition")
                GLES20.glEnableVertexAttribArray(vPosition)
                GLES20.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 3 * 4, vertexBuffer)

                GLES20.glDrawArrays(GL_TRIANGLES, 0, 3)
            }
        })
    }

    private fun initVertexBuffer() {
        val vertices = arrayOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0f, 0.5f, 0f
        )
        val verticesArray = FloatArray(vertices.size) {
            vertices[it]
        }

        val buffer = ByteBuffer.allocateDirect(9 * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(verticesArray)
        vertexBuffer.position(0)
    }

    private fun initShaderProgram(): Int {
        val vertexShader = GLES20.glCreateShader(GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, vertexShaderSource)
        GLES20.glCompileShader(vertexShader)

        val vertexShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(vertexShader, GL_COMPILE_STATUS, vertexShaderCompileStatus)
        if (vertexShaderCompileStatus.get(0) == 0) {
            Log.d("initShaderProgram", "vertexShaderCompileLog: " + GLES20.glGetShaderInfoLog(vertexShader))
        }

        val fragmentShader = GLES20.glCreateShader(GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource)
        GLES20.glCompileShader(fragmentShader)

        val fragmentShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, fragmentShaderCompileStatus)
        if (fragmentShaderCompileStatus.get(0) == 0) {
            Log.d("initShaderProgram", "fragmentShaderCompileLog: " + GLES20.glGetShaderInfoLog(fragmentShader))
        }

        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        glLinkProgram(program)

        val programLinkStatus = IntBuffer.allocate(1)
        GLES20.glGetProgramiv(program, GL_LINK_STATUS, programLinkStatus)
        if (programLinkStatus.get(0) == 0) {
            Log.d("initShaderProgram", "programLinkLog: " + GLES20.glGetProgramInfoLog(program))
        }

        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragmentShader)

        return program
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    private fun supportGLES20(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        return am.deviceConfigurationInfo.reqGlEsVersion >= 0x2000
    }
}