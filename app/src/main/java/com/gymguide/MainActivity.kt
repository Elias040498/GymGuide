
package com.gymguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gymguide.navigation.AppNavigation
import com.gymguide.ui.theme.GymGuideTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GymGuideTheme {
                AppNavigation()
            }
        }
    }
}
