package com.balex.fbnewsclient.presentation.main


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.ui.theme.FbNewsClientTheme
import com.facebook.AccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : ComponentActivity() {

    val TAG = "MA_TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FbNewsClientTheme {

                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(this)
                )
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

                viewModel.userFacebookProfile.observe(this) {
                    Log.d(TAG, it.toString())
                    if (it.id.isNotBlank()) {
                        lifecycleScope.launch {
                            viewModel.getUserPosts()
                        }
                    }


                }

                viewModel.userFacebookPosts.observe(this) {
                    Log.d(TAG, it.toString())
                }


                when (authState.value) {
                    is AuthState.Authorized -> {
                        //Log.d("MainActivity", AccessToken.getCurrentAccessToken()?.permissions.toString())
                        MainScreen()
                    }
                    is AuthState.NotAuthorized -> {
                        LoginScreen {
                            viewModel.processSuccessLoginResult(it)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }




//    private fun getHash() {
//        try {
//            val info = packageManager.getPackageInfo(
//                "com.balex.fbnewsclient",
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: NameNotFoundException) {
//        } catch (e: NoSuchAlgorithmException) {
//        }
//    }

}