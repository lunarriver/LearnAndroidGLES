package lunarriver.learn.android.gles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import lunarriver.learn.android.gles.a_primer.a_hello_triangle.HelloTriangleActivity
import lunarriver.learn.android.gles.a_primer.b_shader.ShadersInterpolationActivity

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

        startActivity(Intent(this@MainActivity, ShadersInterpolationActivity::class.java))
    }
}