package lunarriver.learn.android.gles.a_primer.a_hello_triangle

import android.app.ActivityManager
import android.opengl.GLES20
import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_STATIC_DRAW
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import lunarriver.learn.android.gles.R
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HelloTriangleActivity : ComponentActivity() {
    companion object {
        const val VERTEX_SHADER_SOURCE = """
            attribute vec4 aPos;
            void main() {
                gl_Position = aPos;
            }
        """

        const val FRAGMENT_SHADER_SOURCE = """
            precision mediump float;
            void main() {
                gl_FragColor = vec4(1.0, 0.5, 0.2, 1.0);
            }
        """

        // 三角形顶点
        val VERTICES = floatArrayOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0f, 0.5f, 0f
        )

        // 顶点使用的float数目
        const val FLOATS_OF_VERTEX = 3

        // float占据的字节数
        const val BYTES_OF_FLOAT = 4
    }

    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_triangle)

        if (!supportGLES20()) {
            Toast.makeText(this, "不支持GLES20", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        glSurfaceView = findViewById(R.id.glSurfaceView)
        glSurfaceView.setEGLContextClientVersion(2)

        glSurfaceView.setRenderer(object : GLSurfaceView.Renderer {
            private var program: Int = -1

            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                program = initShaderProgram()
                initVertexBuffer()
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
                GLES20.glClear(GL_COLOR_BUFFER_BIT)

                GLES20.glUseProgram(program)

                val aPosLocation = GLES20.glGetAttribLocation(program, "aPos")
                GLES20.glEnableVertexAttribArray(aPosLocation)
                GLES20.glVertexAttribPointer(
                    aPosLocation, FLOATS_OF_VERTEX, GL_FLOAT, false,
                    FLOATS_OF_VERTEX * BYTES_OF_FLOAT, 0
                )

                GLES20.glDrawArrays(GL_TRIANGLES, 0, VERTICES.size / FLOATS_OF_VERTEX)
            }
        })
    }

    private fun initShaderProgram(): Int {
        val vertexShader = GLES20.glCreateShader(GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, VERTEX_SHADER_SOURCE)
        GLES20.glCompileShader(vertexShader)

        val vertexShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(vertexShader, GL_COMPILE_STATUS, vertexShaderCompileStatus)
        if (vertexShaderCompileStatus.get(0) == 0) {
            Log.d(
                "initShaderProgram",
                "vertexShaderCompileLog: " + GLES20.glGetShaderInfoLog(vertexShader)
            )
        }

        val fragmentShader = GLES20.glCreateShader(GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragmentShader, FRAGMENT_SHADER_SOURCE)
        GLES20.glCompileShader(fragmentShader)

        val fragmentShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, fragmentShaderCompileStatus)
        if (fragmentShaderCompileStatus.get(0) == 0) {
            Log.d(
                "initShaderProgram",
                "fragmentShaderCompileLog: " + GLES20.glGetShaderInfoLog(fragmentShader)
            )
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

    private fun initVertexBuffer() {
        val vbo = IntBuffer.allocate(1)
        GLES20.glGenBuffers(1, vbo)

        GLES20.glBindBuffer(GL_ARRAY_BUFFER, vbo[0])

        val vertexData = FloatBuffer.wrap(VERTICES)
        GLES20.glBufferData(
            GL_ARRAY_BUFFER,
            VERTICES.size * BYTES_OF_FLOAT,
            vertexData,
            GL_STATIC_DRAW
        )
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