package lunarriver.learn.android.gles.a_primer.b_shader

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.b_shader.practice.OffsetTriangleActivity
import lunarriver.learn.android.gles.a_primer.b_shader.practice.OutColorActivity
import lunarriver.learn.android.gles.a_primer.b_shader.practice.ReverseTriangleActivity

class ShadersInterpolationPracticeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_shaders_interpolation_practice, this)

        findViewById<View>(R.id.shaderClassTv).setOnClickListener {
            context.startActivity(Intent(context, ShadersInterpolationActivity2::class.java))
        }

        findViewById<View>(R.id.reverseTriangleTv).setOnClickListener {
            context.startActivity(Intent(context, ReverseTriangleActivity::class.java))
        }

        findViewById<View>(R.id.offsetTriangleTv).setOnClickListener {
            context.startActivity(Intent(context, OffsetTriangleActivity::class.java))
        }

        findViewById<View>(R.id.outColorTv).setOnClickListener {
            context.startActivity(Intent(context, OutColorActivity::class.java))
        }
    }
}