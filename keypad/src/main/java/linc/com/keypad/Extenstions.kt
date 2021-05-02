package linc.com.keypad

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.TextView


fun TextView.contentInt() = text.toString().toInt()

fun Bitmap.decodeResource(res: Resources, resId: Int, dstWidth: Int, dstHeight: Int): Bitmap? {
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)
    options.inJustDecodeBounds = false
//    options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight)
    options = BitmapFactory.Options()
    //May use null here as well. The funciton may interpret the pre-used options variable in ways hard to tell.
    val unscaledBitmap = BitmapFactory.decodeResource(res, resId, options)
    if (unscaledBitmap == null) {
        Log.e("ERR", "Failed to decode resource - " + resId + " " + res.toString())
        return null
    }
    return unscaledBitmap
}