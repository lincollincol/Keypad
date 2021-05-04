package linc.com.keypadexample

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.keypad.CustomKey
import linc.com.keypad.KeypadView
import linc.com.keypad.KeypadConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<KeypadView>(R.id.keypad).apply {

            applyKeypadConfig(KeypadConfig.getInstance().apply {
                setKeypadHeightPercent(50)
//                setKeypadWidthPercent(50)
//                setKeyTextSize(24f)
                setKeyTextStyle(Typeface.BOLD)
//                setKeypadColorRes(android.R.color.holo_purple)
                setKeyContentColorRes(android.R.color.white)
                setCustomKey(CustomKey.getInstance("+", CustomKey.Key.LEFT).apply {
//                    setKeyContentColorInt(Color.GREEN)
                    setKeyTextStyle(Typeface.ITALIC)

                })
                setCustomKey(CustomKey.getInstance(R.drawable.ic_coronavirus, CustomKey.Key.RIGHT).apply {
                    setContentPadding(50)
                    setKeyContentColorInt(Color.CYAN)
                })
//                hideCustomKey(CustomKey.Key.LEFT, false)
            })

            addKeypadClickListener(KeypadView.OnKeyClickListener {
                println("KEY = $it")
            })
            addKeypadClickListener(KeypadView.OnCustomKeyClickListener {
                println("KEY = ${it.key} ${it.value}, ${it.contentType}")
            })
        }
    }
}