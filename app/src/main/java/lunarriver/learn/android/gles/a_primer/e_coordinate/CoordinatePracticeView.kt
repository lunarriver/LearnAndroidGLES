package lunarriver.learn.android.gles.a_primer.e_coordinate

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.e_coordinate.practice.FovAspectActivity
import lunarriver.learn.android.gles.a_primer.e_coordinate.practice.RotateBoxesActivity
import lunarriver.learn.android.gles.a_primer.e_coordinate.practice.ViewMatrixActivity

class CoordinatePracticeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_coordinate_practice, this)

        findViewById<View>(R.id.zBufferTv).setOnClickListener {
            context.startActivity(Intent(context, ZBufferActivity::class.java))
        }

        findViewById<View>(R.id.moreCubeTv).setOnClickListener {
            context.startActivity(Intent(context, MoreCubeActivity::class.java))
        }

        findViewById<View>(R.id.fovAspectTv).setOnClickListener {
            context.startActivity(Intent(context, FovAspectActivity::class.java))
        }

        findViewById<View>(R.id.viewMatrixTv).setOnClickListener {
            context.startActivity(Intent(context, ViewMatrixActivity::class.java))
        }

        findViewById<View>(R.id.rotateBoxesTv).setOnClickListener {
            context.startActivity(Intent(context, RotateBoxesActivity::class.java))
        }
    }
}