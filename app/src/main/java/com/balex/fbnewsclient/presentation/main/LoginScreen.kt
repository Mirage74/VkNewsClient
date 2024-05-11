package com.balex.fbnewsclient.presentation.main

import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.balex.fbnewsclient.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


@Composable
fun LoginScreen(
    onSuccessLogin: (LoginResult) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.outline_tag_faces_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = {
                    if (context is ActivityResultRegistryOwner) {
                        val callbackManager = CallbackManager.Factory.create()
                        val loginManager = LoginManager.getInstance()
                        loginManager.registerCallback(
                            callbackManager,
                            object : FacebookCallback<LoginResult> {
                                override fun onCancel() {
                                    Toast.makeText(context, "Login canceled!", Toast.LENGTH_LONG).show()
                                }

                                override fun onError(error: FacebookException) {
                                    Log.e("Login", error.message ?: "Unknown error")
                                    Toast.makeText(context, "Login failed with errors!", Toast.LENGTH_LONG).show()
                                }

                                override fun onSuccess(result: LoginResult) {
                                    onSuccessLogin(result)
                                }
                            })
                        LoginManager.getInstance().logIn(context, callbackManager, listOf("public_profile", "user_friends"))
                    } else {
                        Toast.makeText(
                            context,
                            "This login should only happens with an AndroidX activity.",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                }) {
                Text(text = stringResource(R.string.button_login))
            }
        }
    }
}

