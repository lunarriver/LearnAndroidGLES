package lunarriver.learn.android.gles.a_primer.a_hello_triangle

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.a_hello_triangle.practices.TwoProgramsActivity
import lunarriver.learn.android.gles.a_primer.a_hello_triangle.practices.TwoTrianglesActivity
import lunarriver.learn.android.gles.a_primer.a_hello_triangle.practices.TwoVAOsActivity

class HelloTrianglePracticeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_hello_triangle_practice, this)

        findViewById<View>(R.id.twoTrianglesTv).setOnClickListener {
            context.startActivity(Intent(context, TwoTrianglesActivity::class.java))
        }

        findViewById<View>(R.id.twoVAOsTv).setOnClickListener {
            context.startActivity(Intent(context, TwoVAOsActivity::class.java))
        }

        findViewById<View>(R.id.twoProgramsTv).setOnClickListener {
            context.startActivity(Intent(context, TwoProgramsActivity::class.java))
        }
    }
}