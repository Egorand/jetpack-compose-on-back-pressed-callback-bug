package dev.egorand.onbackpressedcallbackbug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.egorand.onbackpressedcallbackbug.ui.theme.OnBackPressedCallbackBugTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      OnBackPressedCallbackBugTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun BackPressHandler(onBackPressed: () -> Unit) {
  val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
  val backPressedCallback = remember {
    object : OnBackPressedCallback(enabled = true) {
      override fun handleOnBackPressed() {
        onBackPressed()
      }
    }
  }
  DisposableEffect(dispatcher) {
    dispatcher.addCallback(backPressedCallback)
    onDispose {
      backPressedCallback.remove()
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  OnBackPressedCallbackBugTheme {
    Greeting("Android")
  }
}