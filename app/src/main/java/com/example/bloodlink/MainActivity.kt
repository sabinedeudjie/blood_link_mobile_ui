package com.example.bloodlink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bloodlink.navigation.BloodLinkNavigation
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodLinkTheme
import com.example.bloodlink.ui.theme.White
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloodLinkTheme {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    SplashScreen(
                        onSplashComplete = {
                            showSplash = false
                        }
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        BloodLinkNavigation(
                            navController = navController,
                            startDestination = Screen.Home.route
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        // Note: If logo.png doesn't exist, this will cause a compilation error
        // Make sure logo.png is in res/drawable/
        /*Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Blood Link Logo",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )*/
    }
    
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        onSplashComplete()
    }
}