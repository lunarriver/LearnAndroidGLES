package lunarriver.learn.android.gles.utils

import android.content.Context
import android.opengl.Matrix
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Camera(context: Context, private val strictFps: Boolean = false) {
    companion object {
        const val ANGLE_SHIFT_SENSITIVITY = 0.05f
        const val MOVE_SENSITIVITY = 0.01f
    }

    private var fov = 45f
    private var aspect = 1f

    private var cameraPos = floatArrayOf(0f, 0f, 3f)
    private var cameraFront = floatArrayOf(0f, 0f, -1f)
    private var cameraUp = floatArrayOf(0f, 1f, 0f)

    private var yaw = -90f
    private var pitch = 0f

    private var deltaTime = 0L
    private var lastFrame = 0L

    init {
        lastFrame = System.currentTimeMillis()

        aspect = getScreenAspectRatio(context)
    }

    fun setupWithController(controller: CameraController) {
        controller.setOnActionListener(object: CameraController.OnActionListener {
            override fun onAngleShift(xOffset: Float, yOffset: Float) {
                yaw += xOffset * ANGLE_SHIFT_SENSITIVITY
                pitch += yOffset * ANGLE_SHIFT_SENSITIVITY

                if (pitch > 89.0f) {
                    pitch = 89.0f
                }
                if (pitch < -89.0f) {
                    pitch = -89.0f
                }

                cameraFront = vec3normalize(
                    floatArrayOf(
                        (cos(yaw * PI / 180) * cos(pitch * PI / 180)).toFloat(),
                        sin(pitch * PI / 180).toFloat(),
                        (sin(yaw * PI / 180) * cos(pitch * PI / 180)).toFloat()
                    )
                )
            }

            override fun onMove(action: MoveAction) {
                when (action) {
                    MoveAction.FORWARD -> {
                        cameraPos = vec3Plus(cameraPos, vec3MultiplyNum(cameraFront, MOVE_SENSITIVITY * deltaTime))
                    }
                    MoveAction.BACKWARD -> {
                        cameraPos = vec3Plus(cameraPos, vec3MultiplyNum(cameraFront, -MOVE_SENSITIVITY * deltaTime))
                    }
                    MoveAction.LEFT -> {
                        cameraPos = vec3Plus(
                            cameraPos,
                            vec3MultiplyNum(
                                vec3normalize(vec3cross(cameraFront, cameraUp)),
                                -MOVE_SENSITIVITY * deltaTime
                            )
                        )
                    }
                    MoveAction.RIGHT -> {
                        cameraPos = vec3Plus(
                            cameraPos,
                            vec3MultiplyNum(
                                vec3normalize(vec3cross(cameraFront, cameraUp)),
                                MOVE_SENSITIVITY * deltaTime
                            )
                        )
                    }
                }

                if (strictFps) {
                    // 严格的Fps相机，限定在xz平面
                    cameraPos[1] = 0f
                }
            }

            override fun onFovChange(value: Float) {
                fov = value
            }
        })
    }

    fun onDrawFrame() {
        val currentTime = System.currentTimeMillis()
        deltaTime = currentTime - lastFrame
        lastFrame = currentTime
    }

    fun getProjectionMatrix(): FloatArray {
        val projection = generateMat4fv()
        Matrix.perspectiveM(projection, 0, fov, aspect, 0.1f, 100f)
        return projection
    }

    fun getViewMatrix(): FloatArray {
        val view = generateMat4fv()
        Matrix.setLookAtM(view, 0,
            cameraPos[0], cameraPos[1], cameraPos[2],
            cameraPos[0] + cameraFront[0], cameraPos[1] + cameraFront[1], cameraPos[2] + cameraFront[2],
            cameraUp[0], cameraUp[1], cameraUp[2]
        )

        return view
    }
}

enum class MoveAction {
    LEFT,
    RIGHT,
    FORWARD,
    BACKWARD
}