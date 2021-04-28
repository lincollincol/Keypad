package linc.com.keypadexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.keypad.Key
import linc.com.keypad.Keypad

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Keypad>(R.id.keypad).apply {
            setKeyCustomText(Key.RIGHT, "#")
            setKeyCustomText(Key.LEFT, "*")
            setKeypadHeightPercent(30)
            setKeypadColorInt(Color.RED)
            setKeyContentColorInt(Color.WHITE)
            hideCustomKey(Key.LEFT, true)
            setOnKeypadClickListener(object : Keypad.OnKeypadClickListener() {
                override fun onKeyClicked(value: Int) {
                    println(value)
                }

                override fun onLeftKeyClicked() {
                    super.onLeftKeyClicked()
                    println("LEFT")
                }

                override fun onRightKeyClicked() {
                    super.onRightKeyClicked()
                    println("RIGHT")
                }
            })
        }
    }
}