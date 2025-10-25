package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme



class MainActivity : ComponentActivity(), Orientation.Listener {

    private lateinit var mOrientation: Orientation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    buton()
                }
            }
        }

        mOrientation = Orientation(this)
    }

    override fun onStart() {
        super.onStart()
        mOrientation.startListening(this)
    }

    override fun onStop() {
        super.onStop()
        mOrientation.stopListening()
    }

    override fun onOrientationChanged(pitch: Float, roll: Float) {
        //Log.d("", "pitch: $pitch  |  roll: $roll")
        val rollA = (roll * 100).toInt()
        //var roll2Decimal = rollA / 100.0
        Log.d("PhoneOutput", "<leftStick>$rollA")
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun buton(modifier: Modifier = Modifier)
{
    Button(

        onClick = { Log.d("PhoneInput", "10") },
        modifier = Modifier
            .offset(100.dp, 300.dp)
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //Log.d("PhoneInput","50")
                        Log.d("PhoneOutput", "<throttle>255")
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        //Log.d("PhoneInput", "0")
                        Log.d("PhoneOutput", "<throttle>0")
                    }
                }
                true
            }

    ) {
        Text("Filled")
    }

    Button(

        onClick = { Log.d("PhoneOutput", "<gearup>111") },
        modifier = Modifier
            .offset(100.dp, 400.dp)

    ) {
        Text("gear")
    }
}
