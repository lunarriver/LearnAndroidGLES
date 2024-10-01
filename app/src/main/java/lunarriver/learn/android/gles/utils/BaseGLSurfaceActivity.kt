package lunarriver.learn.android.gles.utils

import android.app.ActivityManager
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import lunarriver.learn.android.gles.R
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class BaseGLSurfaceActivity: ComponentActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    abstract fun layoutResource(): Int

    abstract fun onSurfaceCreated()

    abstract  fun onDrawFrame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())

        if (!supportGLES20()) {
            Toast.makeText(this, "不支持GLES20", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        glSurfaceView = findViewById(R.id.glSurfaceView)
        glSurfaceView.setEGLContextClientVersion(2)

        glSurfaceView.setRenderer(object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                onSurfaceCreated()
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                onDrawFrame()
            }
        })
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