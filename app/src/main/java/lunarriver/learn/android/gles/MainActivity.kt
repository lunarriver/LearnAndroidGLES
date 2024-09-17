package lunarriver.learn.android.gles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.helloTriangleTv).setOnClickListener {
            startActivity(Intent(this@MainActivity, HelloTriangleActivity::class.java))
        }

        startActivity(Intent(this@MainActivity, HelloTriangleActivity::class.java))
    }
}