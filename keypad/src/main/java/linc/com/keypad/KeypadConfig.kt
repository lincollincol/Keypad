package linc.com.keypad

import android.graphics.Color
import android.graphics.ColorSpace
import android.graphics.Typeface
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class KeypadConfig private constructor(
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var imageSize: Int = 24,
        internal var keypadColor: ConfigColorWrapper = ConfigColorWrapper(Color.WHITE),
        internal var keyRipple: Boolean = true,
        private var height: Int = WRAP_CONTENT,
        // TODO: 29.04.21 handle width
        private var width: Int = MATCH_PARENT,

        // TODO: 28.04.21 keyDrawableBackground
) {

    fun setKeyContentColorInt(@ColorInt colorInt: Int) {
        contentColor.apply {
            color = colorInt
            colorSource = ConfigColorWrapper.ColorSource.INT
        }
    }

    fun setKeyContentColorRes(@ColorRes colorRes: Int) {
        contentColor.apply {
            color = colorRes
            colorSource = ConfigColorWrapper.ColorSource.RES
        }
    }

    fun setKeyTextSize(sizeSp: Float) {
        textSize = sizeSp
    }

    fun setKeyTextStyle(typefaceStyle: Int) {
        textStyle = typefaceStyle
    }

    fun setKeyTextFont(font: Typeface) {
        textFont = font
    }

    fun setKeyImageSize(sizeDp: Int) {
        imageSize = sizeDp
    }

    fun enableKeyRipple(enable: Boolean) {
        keyRipple = enable
    }

    fun setKeypadColorInt(@ColorInt colorInt: Int) {
        keypadColor.apply {
            color = colorInt
            colorSource = ConfigColorWrapper.ColorSource.INT
        }
    }

    fun setKeypadColorRes(@ColorRes colorRes: Int) {
        keypadColor.apply {
            color = colorRes
            colorSource = ConfigColorWrapper.ColorSource.RES
        }
    }

    fun setKeypadHeightPercent(percentage: Int) {
        height = ScreenManager.getHeightByPercent(percentage)
    }

    internal fun getSectionHeight() = when (height) {
        WRAP_CONTENT -> height
        else -> height / 4
    }

    companion object {
        fun getInstance() = KeypadConfig()
    }

    internal class ConfigColorWrapper(
            var color: Int,
            var colorSource: ColorSource = ColorSource.INT,
    ) {
        enum class ColorSource { RES, INT }
    }
}
