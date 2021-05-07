package linc.com.keypadexample

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import linc.com.keypad.CustomKey
import linc.com.keypad.KeypadView
import linc.com.keypad.KeypadConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<KeypadView>(R.id.keypad).apply {
            applyKeypadConfig {
                setKeypadHeightPercent(50)
//                setKeypadWidthPercent(50)
                setKeyMargin(20)
                setKeyTextSize(24f)
                setKeyTextStyle(Typeface.BOLD)
                setKeyContentBackground(R.drawable.border)
//                setKeypadColorRes(android.R.color.holo_purple)
                setKeyContentColorRes(android.R.color.white)
                setCustomKey(CustomKey.getInstance("+", CustomKey.Key.LEFT).apply {
//                    setKeyContentColorInt(Color.GREEN)
                    setKeyTextStyle(Typeface.ITALIC)

                })
                setCustomKey(CustomKey.getInstance(R.drawable.ic_coronavirus, CustomKey.Key.RIGHT).apply {
                    setKeyContentColorInt(Color.CYAN)
                    setKeyPadding(25)
                })
                setKeyPadding(50, 50, 25, 25)
                setKeySize(WRAP_CONTENT, WRAP_CONTENT)
            }

            addKeypadClickListener(KeypadView.OnKeyClickListener {
                println("KEY = $it")
            })
            addKeypadClickListener(KeypadView.OnCustomKeyClickListener {
                println("KEY = ${it.key} ${it.value}, ${it.contentType}")
            })
        }
    }
}