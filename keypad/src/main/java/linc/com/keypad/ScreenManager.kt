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

    fun getWidthByPercent(percent: Int) = (width * percent) / 100

    fun getHeightByPercent(percent: Int) = (height * percent) / 100

    fun getWidth() = width

    fun getHeight() = height

}