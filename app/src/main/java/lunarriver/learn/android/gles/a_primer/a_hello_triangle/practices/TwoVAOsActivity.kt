package lunarriver.learn.android.gles.a_primer.a_hello_triangle.practices

import android.app.ActivityManager
import android.opengl.GLES20
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

class TwoVAOsActivity : ComponentActivity() {
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
        val VERTICES1 = floatArrayOf(
            -1f, -0.5f, 0f,
            0f, -0.5f, 0f,
            -0.5f, 0.5f, 0f
        )

        val VERTICES2 = floatArrayOf(
            0f, -0.5f, 0f,
            1f, -0.5f, 0f,
            0.5f, 0.5f, 0f
        )

        // 顶点使用的float数目
        const val FLOATS_OF_VERTEX = 3

        // float占据的字节数
        const val BYTES_OF_FLOAT = 4
    }

    private lateinit var glSurfaceView: GLSurfaceView

    private var vbo1 = -1
    private var vbo2 = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_only_glsurfaceview)

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
                vbo1 = initVertexBuffer(VERTICES1)
                vbo2 = initVertexBuffer(VERTICES2)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1f)
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                GLES20.glUseProgram(program)

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo1)
                GLES20.glBufferData(
                    GLES20.GL_ARRAY_BUFFER,
                    VERTICES1.size * BYTES_OF_FLOAT,
                    FloatBuffer.wrap(VERTICES1),
                    GLES20.GL_STATIC_DRAW
                )
                val aPosLocation = GLES20.glGetAttribLocation(program, "aPos")
                GLES20.glEnableVertexAttribArray(aPosLocation)
                GLES20.glVertexAttribPointer(
                    aPosLocation, FLOATS_OF_VERTEX, GLES20.GL_FLOAT, false,
                    FLOATS_OF_VERTEX * BYTES_OF_FLOAT, 0
                )
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTICES1.size / FLOATS_OF_VERTEX)

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo2)
                GLES20.glBufferData(
                    GLES20.GL_ARRAY_BUFFER,
                    VERTICES2.size * BYTES_OF_FLOAT,
                    FloatBuffer.wrap(VERTICES2),
                    GLES20.GL_STATIC_DRAW
                )
                // 必须重新配置
                val aPosLocationAgain = GLES20.glGetAttribLocation(program, "aPos")
                GLES20.glEnableVertexAttribArray(aPosLocationAgain)
                GLES20.glVertexAttribPointer(
                    aPosLocationAgain, FLOATS_OF_VERTEX, GLES20.GL_FLOAT, false,
                    FLOATS_OF_VERTEX * BYTES_OF_FLOAT, 0
                )
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTICES2.size / FLOATS_OF_VERTEX)
            }
        })
    }

    private fun initShaderProgram(): Int {
        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, VERTEX_SHADER_SOURCE)
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
        GLES20.glShaderSource(fragmentShader, FRAGMENT_SHADER_SOURCE)
        GLES20.glCompileShader(fragmentShader)

        val fragmentShaderCompileStatus = IntBuffer.allocate(1)
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, fragmentShaderCompileStatus)
        if (fragmentShaderCompileStatus.get(0) == 0) {
            Log.d(
                "initShaderProgram",
                "fragmentShaderCompileLog: " + GLES20.glGetShaderInfoLog(fragmentShader)
            )
        }

        val program = GLES20.glCreateProgram()
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

        return program
    }

    private fun initVertexBuffer(vertices: FloatArray): Int {
        val vbo = IntBuffer.allocate(1)
        GLES20.glGenBuffers(1, vbo)
        return vbo[0]
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