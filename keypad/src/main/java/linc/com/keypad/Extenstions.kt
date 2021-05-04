package linc.com.keypad

import android.util.TypedValue
import android.view.View
import android.widget.TextView

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