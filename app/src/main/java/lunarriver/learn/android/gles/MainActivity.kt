package lunarriver.learn.android.gles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import lunarriver.learn.android.gles.a_primer.a_hello_triangle.HelloTriangleActivity
import lunarriver.learn.android.gles.a_primer.b_shader.ShadersInterpolationActivity
import lunarriver.learn.android.gles.a_primer.c_texture.TextureActivity
import lunarriver.learn.android.gles.a_primer.d_transform.TransformActivity
import lunarriver.learn.android.gles.a_primer.e_coordinate.CoordinateActivity
import lunarriver.learn.android.gles.a_primer.f_camera.CameraActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.helloTriangleTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, HelloTriangleActivity::class.java))
        }

        findViewById<View>(R.id.shadersInterpolationTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, ShadersInterpolationActivity::class.java))
        }

        findViewById<View>(R.id.textureTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, TextureActivity::class.java))
        }

        findViewById<View>(R.id.transformTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, TransformActivity::class.java))
        }

        findViewById<View>(R.id.coordinateTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, CoordinateActivity::class.java))
        }

        findViewById<View>(R.id.cameraTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, CameraActivity::class.java))
        }

        startActivity(Intent(this@MainActivity, CameraActivity::class.java))
    }
}