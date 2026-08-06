package com.pairlix.dating.view.home

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.R
import com.pairlix.dating.navigation.BottomNavItem
import com.pairlix.dating.navigation.BottomNavItem.Activity
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.utils.SocketManager
import com.pairlix.dating.utils.SocketState
import com.pairlix.dating.view.M4.ActivityScreen
import com.pairlix.dating.view.M5.ChatScreen
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import com.pairlix.dating.viewModel.M6ViewModel
import com.pairlix.dating.viewModel.SocketViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlin.system.exitProcess
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    socketViewModel: SocketViewModel,
    viewModel4: M4ViewModel,
    viewModelM5: M5ViewModel,
    viewModelM6: M6ViewModel
) {
    val socketState by socketViewModel.socketState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (viewModel4.selectedMainScreenIndex.value) {
                0 -> HomeScreen(navController, viewModel, viewModel4, viewModelM5, viewModelM6, socketViewModel = socketViewModel)
                1 -> ActivityScreen(navController, viewModel4, viewModel)
                2 -> {

                        ChatScreen(navController as NavHostController, viewModel4, viewModelM5, viewModel)

                }
            }
        }

        // 🔥 Bottom bar sits naturally below content — no overlap ever
        CustomBottomBar(
            selectedIndex = viewModel4.selectedMainScreenIndex.value,
            onItemSelected = { index ->
                if (index == 0) {
                    SingletonObject.isComeFromBlockedProfile = false
                    SingletonObject.isComeFromChat = false
                    SingletonObject.isFromProfileView = false
                }
                if (index == 1) {
                    viewModel4.showBottomActions = 0
                    viewModel4.selectedChipIndex.value = 0
                    SingletonObject.isComeFromBlockedProfile = false
                    SingletonObject.isComeFromChat = false
                    SingletonObject.isFromProfileView = false
                }
                if (index == 2) {
                    viewModelM5.selectedChipIndex.value = 0
                    viewModel4.showBottomActions = 0
                    SingletonObject.isComeFromChat = true
                    SingletonObject.isComeFromBlockedProfile = false
                    SingletonObject.isFromProfileView = false
                }
                if (index == 3) {
                    navController.navigate(Screen.ProfileScreen.route)
                    SingletonObject.isComeFromBlockedProfile = false
                    SingletonObject.isFromProfileView = false
                }
                else viewModel4.selectedMainScreenIndex.value = index
            }
        )
    }
}

@Composable
fun CustomBottomBar(
    modifier: Modifier= Modifier,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Activity,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            .background(Color.Black, RoundedCornerShape(60.dp))
          /*  .background(
                Color(0xFFF2ECF6),
                RoundedCornerShape(60.dp)
            )*/
            .padding(vertical = 6.sdp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEachIndexed { index, item ->

                val isSelected = index == selectedIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {onItemSelected(index)}
                        .padding(12.dp)
                ) {

                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color(0xFF6D6D6D),
                        modifier = Modifier.size(28.dp)
                    )

                  /*  Spacer(modifier = Modifier.height(10.sdp))

                    Text(
                        text = stringResource( item.title),
                        color = if (isSelected) Color(0xFF590988) else Color.Black,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )*/
                }
            }
        }
    }
}
