package lunarriver.learn.android.gles.a_primer.c_texture

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import lunarriver.learn.android.gles.R
import lunarriver.learn.android.gles.a_primer.c_texture.practice.FourFaceActivity
import lunarriver.learn.android.gles.a_primer.c_texture.practice.MixTextureActivity
import lunarriver.learn.android.gles.a_primer.c_texture.practice.OtherSideActivity
import lunarriver.learn.android.gles.a_primer.c_texture.practice.PartTextureActivity

class TexturePracticeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_texture_practice, this)

        findViewById<View>(R.id.awesomeFaceTv).setOnClickListener {
            context.startActivity(Intent(context, TextureActivity2::class.java))
        }

        findViewById<View>(R.id.otherSideTv).setOnClickListener {
            context.startActivity(Intent(context, OtherSideActivity::class.java))
        }

        findViewById<View>(R.id.fourFaceTv).setOnClickListener {
            context.startActivity(Intent(context, FourFaceActivity::class.java))
        }

        findViewById<View>(R.id.partTextureTv).setOnClickListener {
            context.startActivity(Intent(context, PartTextureActivity::class.java))
        }

        findViewById<View>(R.id.mixTv).setOnClickListener {
            context.startActivity(Intent(context, MixTextureActivity::class.java))
        }
    }
}