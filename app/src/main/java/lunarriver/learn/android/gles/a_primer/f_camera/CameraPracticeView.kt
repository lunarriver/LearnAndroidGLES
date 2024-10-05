package lunarriver.learn.android.gles.a_primer.f_camera

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.f_camera.practice.CameraFpsActivity

class CameraPracticeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_camera_practice, this)

        findViewById<View>(R.id.cameraMoveTv).setOnClickListener {
            context.startActivity(Intent(context, CameraMoveActivity::class.java))
        }

        findViewById<View>(R.id.cameraControllerTv).setOnClickListener {
            context.startActivity(Intent(context, CameraControllerActivity::class.java))
        }

        findViewById<View>(R.id.cameraClassTv).setOnClickListener {
            context.startActivity(Intent(context, CameraClassActivity::class.java))
        }

        findViewById<View>(R.id.cameraFpsTv).setOnClickListener {
            context.startActivity(Intent(context, CameraFpsActivity::class.java))
        }
    }
}