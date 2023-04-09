package com.example.spender.ui.navigation.screens.profileScreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.spender.data.firebase.viewModels.AuthViewModel
import com.example.spender.ui.navigation.ProfileNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.navigation.screens.destinations.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ProfileNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    var signOut by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                authViewModel.signOut()
                signOut = true
            }
        ) {
            Text("Profile screen")
        }
    }
    if (signOut) {
        val activity = (LocalContext.current as Activity)
        val intent: Intent = activity.intent
        activity.finish()
        startActivity(activity, intent, intent.extras)
    }
}
