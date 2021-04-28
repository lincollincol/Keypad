package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorRes

internal data class KeypadStyle(
        var contentColor: Int = Color.BLACK,
        var textSize: Float = 45f,
        var textStyle: Int = Typeface.NORMAL,
        var textFont: Typeface = Typeface.DEFAULT,
        var keypadColor: Int = Color.WHITE,
        var keyRipple: Boolean = true,

        var height: Int = WRAP_CONTENT

        // TODO: 28.04.21 keyDrawableBackground
) {
    fun getSectionHeight() = when (height) {
        WRAP_CONTENT -> height
        else -> height / 4
    }
}
