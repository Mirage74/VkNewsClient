package com.balex.vknewsclient


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.balex.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var t = 5
        setContent {
            VkNewsClientTheme {
                val someState = remember {
                    mutableStateOf(true)
                }
                //t++
                Log.d("MainActivity", "Recomposition: ${someState.value}")
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    when (it) {
                        is VKAuthenticationResult.Success -> {
                            Log.d("MainActivity", "Success auth")
                        }
                        is VKAuthenticationResult.Failed -> {
                            Log.d("MainActivity", "Failed auth")
                        }
                    }
                }
                LaunchedEffect(key1 = t ) {
                    Log.d("MainActivity", "LaunchedEffect")
                    delay(100)
                }
                SideEffect {
                    Log.d("MainActivity", "SideEffect")
                }
                Button(onClick = { someState.value = !someState.value }) {
                    Text(text = "Change state")
                }
            }
        }
    }
}