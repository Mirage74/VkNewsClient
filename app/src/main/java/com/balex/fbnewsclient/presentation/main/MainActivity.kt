package com.balex.fbnewsclient.presentation.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.presentation.NewsFeedApplication
import com.balex.fbnewsclient.presentation.ViewModelFactory
import com.balex.fbnewsclient.ui.theme.FbNewsClientTheme
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as NewsFeedApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            FbNewsClientTheme {

                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.collectAsState(AuthState.Initial)


                viewModel.checkToken(this)

                when (authState.value) {
                    is AuthState.Authorized -> {
                        //Log.d("MainActivity", AccessToken.getCurrentAccessToken()?.permissions.toString())
                        MainScreen(viewModelFactory)
                    }
                    is AuthState.NotAuthorized -> {
                        LoginScreen {
                            viewModel.processSuccessLoginResult()
                            //Log.d("Result", it.toString())
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