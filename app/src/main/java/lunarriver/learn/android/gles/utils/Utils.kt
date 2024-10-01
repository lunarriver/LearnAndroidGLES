package lunarriver.learn.android.gles.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
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