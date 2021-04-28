package linc.com.keypad

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.core.view.setMargins
import androidx.core.view.setPadding

class Keypad @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var clickListener: OnKeypadClickListener? = null
    private var leftKey: CustomKey<*>? = null
    private var rightKey: CustomKey<*>? = null

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setBackgroundResource(R.color.design_default_color_primary)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initKeypad()
    }

    fun setOnKeypadClickListener(clickListener: OnKeypadClickListener) {
        this.clickListener = clickListener
    }

    fun setCustomTextKey(key: Key, value: String) {
        when(key) {
            Key.LEFT -> leftKey = CustomKey(value)
            Key.RIGHT -> rightKey = CustomKey(value)
        }
    }

    fun setCustomImageKey(key: Key, @IntegerRes resource: Int) {
        when(key) {
            Key.LEFT -> leftKey = CustomKey(resource, CustomKey.IMAGE)
            Key.RIGHT -> rightKey = CustomKey(resource, CustomKey.IMAGE)
        }
    }

    fun hideCustomKey(key: Key, hide: Boolean) {
        when(key) {
            Key.LEFT -> leftKey?.hide = hide
            Key.RIGHT -> rightKey?.hide = hide
        }
    }

    private fun initKeypad() {
        var key = 1
        var section: LinearLayout

        repeat(KEYS_PER_SECTION) { row ->
            section = getSection()
            repeat(KEYS_PER_SECTION) { col ->
                // TODO: 28.04.21 getTextView according to config function
                section.addView(TextView(context).apply {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
                    layoutParams = LayoutParams(0, WRAP_CONTENT).apply {
//                        setMargins(25)
//                        setPadding(50)
                        weight = 33f
                    }
                    gravity = Gravity.CENTER
                    text = key.toString()
                    setBackgroundResource(R.color.design_default_color_error)
                    setOnClickListener { clickListener?.onKeyClicked(text.toString().toInt()) }
                })
                key++
            }
            addView(section)
        }

        // TODO: 28.04.21 getTextView according to config function
        // Fill last section
        section = getSection()
        repeat(KEYS_PER_SECTION) { key ->
            when {
                key == LEFT_KEY -> {
                    if(leftKey != null && leftKey?.hide!!.not()) {
                        section.addView(when(leftKey?.type) {
                            CustomKey.IMAGE -> {
                                ImageView(context).apply {
                                    setImageResource(leftKey?.value as Int)
                                    setOnClickListener { clickListener?.onLeftKeyClicked() }
                                }
                            }
                            else -> TextView(context).apply {
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
                                layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                                    setMargins(25)
                                    setPadding(50)
                                }
                                text = leftKey?.value.toString()
                                setBackgroundResource(R.color.design_default_color_error)
                                setOnClickListener { clickListener?.onKeyClicked(key) }
                            }
                        })
                    }
                }
                key == RIGHT_KEY -> {
                    if(rightKey != null && rightKey?.hide!!.not()) {
                        section.addView(when(rightKey?.type) {
                            CustomKey.IMAGE -> {
                                ImageView(context).apply {
                                    setImageResource(rightKey?.value as Int)
                                    setOnClickListener { clickListener?.onRightKeyClicked() }
                                }
                            }
                            else -> TextView(context).apply {
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
                                layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                                    setMargins(25)
                                    setPadding(50)
                                }
                                text = rightKey?.value.toString()
                                setBackgroundResource(R.color.design_default_color_error)
                                setOnClickListener { clickListener?.onKeyClicked(key) }
                            }
                        })
                    }
                }
                key == ZERO_KEY -> TextView(context).apply {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        setMargins(25)
                        setPadding(50)
                    }
                    text = "0"
                    setBackgroundResource(R.color.design_default_color_error)
                    setOnClickListener { clickListener?.onKeyClicked(key) }
                }
            }
        }
    }

    private fun getSection() = LinearLayout(context).apply {
        this.orientation = orientation
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setBackgroundResource(R.color.design_default_color_secondary_variant)

    }

    abstract class OnKeypadClickListener {
        abstract fun onKeyClicked(value: Int)

        open fun onLeftKeyClicked() {
            /** Not implemented*/
        }

        open fun onRightKeyClicked() {
            /** Not implemented*/
        }
    }

    companion object {
        private const val LEFT_KEY = 0
        private const val ZERO_KEY = 1
        private const val RIGHT_KEY = 2
        private const val LAST_KEYPAD_SECTION = 3
        private const val KEYS_PER_SECTION = 3
    }
}