package linc.com.keypadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.keypad.Keypad

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Keypad>(R.id.keypad).apply {
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