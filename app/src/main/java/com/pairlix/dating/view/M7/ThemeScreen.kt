
package com.pairlix.dating.view.M7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace

@Composable
fun ThemeScreen(navController: NavController) {


    BoxWithConstraints(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {

        val maxHeight= this.maxHeight

        Column (modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding().padding(top = 30.dp).padding(horizontal = 16.dp)){


            TopBackBtnHeading(navController,"")

            verticalSpace(30)


        }


    }

}