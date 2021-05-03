package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

class KeypadConfig private constructor(
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        // TODO: 03.05.21 padding
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var enableKeyRipple: Boolean = true,
        private var height: Int = WRAP_CONTENT,
        // TODO: 29.04.21 handle width
        private var width: Int = MATCH_PARENT,

        // TODO: 28.04.21 keyDrawableBackground
) {

    private var customKeys = mutableListOf(
            CustomKey.getInstance("*", CustomKey.Key.LEFT),
            CustomKey.getInstance("#", CustomKey.Key.RIGHT)
    )

    fun hideCustomKey(key: CustomKey.Key, hide: Boolean) {
        getCustomKey(key)?.hide = hide
    }

    fun setCustomKey(customKey: CustomKey) {
//        customKeys.apply {
//            removeAll { it.key == customKey.key }
//            add(customKey)
//        }
        getCustomKey(customKey.key)?.updateKey(customKey)
    }

    fun setKeyContentColorInt(@ColorInt colorInt: Int) {
        val color = ConfigColorWrapper(colorInt, ConfigColorWrapper.ColorSource.INT)
        contentColor = color
        customKeys.forEach { it.contentColor.setGlobal(color) }
    }

    fun setKeyContentColorRes(@ColorRes colorRes: Int) {
        val color = ConfigColorWrapper(colorRes, ConfigColorWrapper.ColorSource.RES)
        contentColor = color
        customKeys.forEach { it.contentColor.setGlobal(color) }
    }

    fun setKeyTextSize(sizeSp: Float) {
        textSize = sizeSp
        customKeys.forEach { it.textSize.setGlobal(sizeSp) }
    }

    fun setKeyTextStyle(typefaceStyle: Int) {
        textStyle = typefaceStyle
        customKeys.forEach { it.textStyle.setGlobal(typefaceStyle) }
    }

    fun setKeyTextFont(font: Typeface) {
        textFont = font
        customKeys.forEach { it.textFont.setGlobal(font) }
    }

    fun enableKeyRipple(enable: Boolean) {
        enableKeyRipple = enable
        customKeys.forEach { it.enableKeyRipple.setGlobal(enable) }
    }

    fun setKeypadHeightPercent(percentage: Int) {
        height = ScreenManager.getHeightByPercent(when(percentage) {
            in 0..100 -> percentage
            else -> 100
        })
    }

    internal fun getSectionHeight() = when (height) {
        WRAP_CONTENT -> height
        else -> height / 4
    }

    internal fun getCustomKey(key: CustomKey.Key) = customKeys.find { it.key == key }

    companion object {
        fun getInstance() = KeypadConfig()
    }
}
