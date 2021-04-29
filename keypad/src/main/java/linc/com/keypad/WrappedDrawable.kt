package linc.com.keypad

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build


internal class WrappedDrawable(
        val drawable: Drawable,
) : Drawable() {

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        //update bounds to get correctly
        super.setBounds(left, top, right, bottom)
        drawable.setBounds(left, top, right, bottom)
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
    }

    @Deprecated("", ReplaceWith("drawable.opacity"))
    override fun getOpacity(): Int {
        return drawable.opacity
    }

    override fun draw(canvas: Canvas) {
        drawable.draw(canvas)
    }

    override fun getIntrinsicWidth(): Int {
        return drawable.bounds.width()
    }

    override fun getIntrinsicHeight(): Int {
        return drawable.bounds.height()
    }
}