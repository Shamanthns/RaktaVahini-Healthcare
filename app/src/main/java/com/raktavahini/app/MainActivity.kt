package com.raktavahini.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.raktavahini.app.navigation.RaktaVahiniNavGraph
import com.raktavahini.app.ui.theme.RaktaVahiniTheme
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RaktaVahiniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RaktaVahiniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RaktaVahiniNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
