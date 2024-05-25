package com.balex.fbnewsclient.presentation.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.presentation.getApplicationComponent
import com.balex.fbnewsclient.ui.theme.FbNewsClientTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)


            viewModel.checkToken(this)
            FbNewsClientTheme {


                when (authState.value) {
                    is AuthState.Authorized -> {
                        //Log.d("MainActivity", AccessToken.getCurrentAccessToken()?.permissions.toString())
                        MainScreen()
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



}