package com.example.spender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.viewmodel.AuthViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpenderMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(activity = this)
        }
    }
}

@Composable
fun AppNavigation(
    activity: ComponentActivity
) {
    SpenderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                dependenciesContainerBuilder = { // this: DependenciesContainerBuilder<*>
                    // 👇 To tie ActivityViewModel to the activity, making it available to all destinations
                    dependency(hiltViewModel<AuthViewModel>(activity))
                }
            )
        }
    }
}
