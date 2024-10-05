package lunarriver.learn.android.gles.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.view.WindowManager
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import kotlin.math.sqrt

fun loadStringFromAsset(context: Context, source: String): String? {
    return try {
        val inputStream: InputStream = context.assets.open(source)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, StandardCharsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun loadBitmapFromAsset(context: Context, source: String): Bitmap? {
    return try {
        val inputStream: InputStream = context.assets.open(source)
        BitmapFactory.decodeStream(inputStream)
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun generateByteBuffersFromBitmap(bitmap: Bitmap, asRGBA: Boolean = false): ByteBuffer {
    val pixels = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    val byteBuffer = ByteBuffer.allocate(bitmap.width * bitmap.height * if (asRGBA) 4 else 3)
    for (pixel in pixels) {
        byteBuffer.put((pixel shr 16 and 0xFF).toByte()) // Red
        byteBuffer.put((pixel shr 8 and 0xFF).toByte()) // Green
        byteBuffer.put((pixel and 0xFF).toByte()) // Blue
        if (asRGBA) {
            byteBuffer.put((pixel shr 24 and 0xFF).toByte()) // Alpha
        }
    }

    byteBuffer.rewind()
    return byteBuffer
}

fun generateMat4fv(): FloatArray {
    return floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    )
}

fun getScreenAspectRatio(context: Context): Float {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val size = Point()
    wm.defaultDisplay?.getRealSize(size)

    val width = size.x
    val height = size.y

    // 防止除数为0
    return if (height == 0) 1f else width.toFloat() / height.toFloat()
}

fun vec3cross(v1: FloatArray, v2: FloatArray): FloatArray {
    return floatArrayOf(
        v1[1]*v2[2] - v1[2]*v2[1],
        v1[2]*v2[0] - v1[0]*v2[2],
        v1[0]*v2[1] - v1[1]*v2[0]
    )
}

fun vec3normalize(v: FloatArray): FloatArray {
    val length = sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2])
    return floatArrayOf(
        v[0] / length,
        v[1] / length,
        v[2] / length
    )
}

fun vec3MultiplyNum(v: FloatArray, num: Float): FloatArray {
    return floatArrayOf(
        v[0] * num,
        v[1] * num,
        v[2] * num
    )
}

fun vec3Plus(v1: FloatArray, v2: FloatArray): FloatArray {
    return floatArrayOf(
        v1[0] + v2[0],
        v1[1] + v2[1],
        v1[2] + v2[2]
    )
}
