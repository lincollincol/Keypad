package linc.com.keypad

import android.util.TypedValue
import android.view.View
import android.widget.TextView

fun TextView.contentInt() = text.toString().toInt()
fun View.enableBorderlessRipple() {
    val typed = TypedValue()
    context.theme.resolveAttribute(
            R.attr.selectableItemBackgroundBorderless,
            typed,
            true
    )
    setBackgroundResource(typed.resourceId)
}