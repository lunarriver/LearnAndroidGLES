package lunarriver.learn.android.gles.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

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