package com.bismark.jetpackcomposeplayground.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bismark.jetpackcomposeplayground.ui.theme.JPGTheme

@Composable
fun JPGApp(windowSizeClass: WindowSizeClass){
    JPGTheme {
        val navController = rememberNavController()

    }
}
