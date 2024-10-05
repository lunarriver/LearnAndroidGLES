package lunarriver.learn.android.gles.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import lunarriver.learn.android.gles.R

class CameraController @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): FrameLayout(context, attrs) {
    private lateinit var onActionListener: OnActionListener

    private var lastX = -1f
    private var lastY = -1f

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_camera_controller, this)

        findViewById<View>(R.id.wTv).setOnClickListener {
            onActionListener.onMove(MoveAction.FORWARD)
        }
        findViewById<View>(R.id.sTv).setOnClickListener {
            onActionListener.onMove(MoveAction.BACKWARD)
        }
        findViewById<View>(R.id.dTv).setOnClickListener {
            onActionListener.onMove(MoveAction.RIGHT)
        }
        findViewById<View>(R.id.aTv).setOnClickListener {
            onActionListener.onMove(MoveAction.LEFT)
        }

        val fovSeekBar: SeekBar = findViewById(R.id.fovSeekBar)
        fovSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onActionListener.onFovChange(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun setOnActionListener(l: OnActionListener) {
        onActionListener = l
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }
            ACTION_MOVE -> {
                val xOffset = (event.x - lastX)
                val yOffset = (event.y - lastY)
                lastX = event.x
                lastY = event.y

                onActionListener.onAngleShift(-xOffset, yOffset)
            }
        }

        return true
    }

    interface OnActionListener {
        fun onAngleShift(xOffset: Float, yOffset: Float)

        fun onMove(action: MoveAction)

        fun onFovChange(value: Float)
    }
}