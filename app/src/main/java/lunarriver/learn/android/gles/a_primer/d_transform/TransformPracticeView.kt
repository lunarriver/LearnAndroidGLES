package lunarriver.learn.android.gles.a_primer.d_transform

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.d_transform.practice.RotateFirstActivity
import lunarriver.learn.android.gles.a_primer.d_transform.practice.TwoBoxesActivity

class TransformPracticeView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_transform_practice, this)

        findViewById<View>(R.id.rotateFirstTv).setOnClickListener {
            context.startActivity(Intent(context, RotateFirstActivity::class.java))
        }

        findViewById<View>(R.id.twoBoxesTv).setOnClickListener {
            context.startActivity(Intent(context, TwoBoxesActivity::class.java))
        }
    }
}