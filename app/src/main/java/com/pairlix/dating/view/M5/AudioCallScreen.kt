package com.pairlix.dating.view.M5


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.viewModel.M5ViewModel

@Composable
fun AudioCallScreen(navController: NavController,m5ViewModel: M5ViewModel) {

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()) {
        val maxWidth=this.maxWidth


        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black).statusBarsPadding()
            .padding(horizontal = 16.dp)) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
            ) {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .clickable { navController.popBackStack()},
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = "back_ic"
                )

                Column(Modifier.align(alignment = Alignment.Center)) {

                    Text(
                        modifier= Modifier.fillMaxWidth(),
                        text = "Dhairya,23",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                    verticalSpace(5)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "2:35",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                }



            }




        }
    }

}