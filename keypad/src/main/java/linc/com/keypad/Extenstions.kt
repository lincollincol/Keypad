package linc.com.keypad

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import linc.com.keypad.Constants.BOTTOM
import linc.com.keypad.Constants.END
import linc.com.keypad.Constants.START
import linc.com.keypad.Constants.TOP

internal fun TextView.contentInt() = text.toString().toInt()

internal fun View.enableBorderlessRipple() {
    val typed = TypedValue()
    context.theme.resolveAttribute(
            R.attr.selectableItemBackgroundBorderless,
            typed,
            true
    )
    setBackgroundResource(typed.resourceId)
}

internal fun ConstraintLayout.LayoutParams.setMargins(margins: IntArray) {
    setMargins(margins[START], margins[TOP], margins[END], margins[BOTTOM])
}

internal fun View.setPadding(padding: IntArray) {
    setPadding(padding[START], padding[TOP], padding[END], padding[BOTTOM])
}

internal fun copyDimenValues(dimen: IntArray, vararg sideValue: Int) {
    dimen[TOP] = sideValue[TOP]
    dimen[BOTTOM] = sideValue[BOTTOM]
    dimen[START] = sideValue[START]
    dimen[END] = sideValue[END]
}
