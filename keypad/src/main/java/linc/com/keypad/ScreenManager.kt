package linc.com.keypad

import android.content.res.Resources

internal object ScreenManager {

    private var width: Int = 0
    private var height: Int = 0

    fun init() {
        val display = Resources.getSystem().displayMetrics
        width = display.widthPixels
        height = display.heightPixels

        println(width)
        println(height)
    }

    fun getWidthByPercent(percent: Int) = when(percent) {
        in 0..100 -> (width * percent) / 100
        else -> width
    }

    fun getHeightByPercent(percent: Int) = when(percent) {
        in 0..100 -> (height * percent) / 100
        else -> height
    }

    fun getWidth() = width

    fun getHeight() = height

}