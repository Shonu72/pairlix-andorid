package com.pairlix.dating.view.M5

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.requests.ProfileViewActionRequest
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.view.M4.GridItem
import com.pairlix.dating.view.M4.getUserActivityObserver
import com.pairlix.dating.viewModel.AuthViewModel
import com.pairlix.dating.viewModel.M4ViewModel
import com.pairlix.dating.viewModel.M5ViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import com.pairlix.dating.ReusedComponents.PlanPopUp
import com.pairlix.dating.helper.EmpResource
@Composable
fun BlockedProfilesScreen(navController : NavController,viewModelM5: M5ViewModel,viewModelM4: M4ViewModel,authViewModel: AuthViewModel) {

val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current


    val getPreviewProfile by authViewModel.getPreviewProfile.observeAsState()
    var planType by remember { mutableStateOf(0) }
    var showPlanPopUp by remember { mutableStateOf(false) }


    LaunchedEffect(getPreviewProfile) {
        getPreviewProfile.let {
            if (it is EmpResource.Success){
                planType=it.value.data?.activePlanType?:0
            }
        }}







        LaunchedEffect(viewModelM4.selectedChipIndex.value) {
        viewModelM4.hitUserActivity(
            access_token = SharedPreference.get(context).accessToken,
            actionType ="blocked")
            }



    getUserActivityObserver(
        viewModel = viewModelM4,
        context = context as MainActivity,
        lifecycleOwner = lifecycleOwner,
        navController = navController,
        onSuccess = { it ->
            val list = it ?: emptyList()
            it.let {
                viewModelM4.getUserActivityList.clear()
                viewModelM4.getUserActivityList.addAll(list)
            }
        }
    )

    val userList = viewModelM4.getUserActivityList


    BackHandler {
        // ✅ Task only on back press

        SingletonObject.isComeFromBlockedProfile = false
        viewModelM4.showBottomActions = viewModelM4.selectedChipIndex.value

        // Back navigation
        navController.popBackStack()
    }

   /* DisposableEffect(Unit) {

        onDispose {
            viewModelM4.showBottomActions = viewModelM4.selectedChipIndex.value
        }
    }
*/


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val max = this.maxHeight
        val maxWidth = this.maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            TopBackBtnHeading(navController, text = stringResource(R.string.blocked_profiles))

            verticalSpace(18)

            LazyColumn() {


                if (userList.isEmpty()) {
                    item {

                        if (viewModelM4.selectedChipIndex.value == 3) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                text = stringResource(R.string.profiles_you_skip_will_appear_here_for_reference) ,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )

                        }
                        if (viewModelM4.selectedChipIndex.value == 0) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.no_blocked_profiles),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )

                        }
                        if (viewModelM4.selectedChipIndex.value == 1) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                painter = painterResource(R.drawable.nodata_image),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.start_exploring_profiles_and_send_your_first_like) ,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24 .sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )

                        }
                        if (viewModelM4.selectedChipIndex.value == 2) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                painter = painterResource(R.drawable.shaikh_match_img),
                                contentDescription = "img"
                            )
                            verticalSpace(20)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text =  stringResource(R.string.keep_swiping_the_right_connection_takes_time),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_bold))
                            )

                        }


                    }

                } else {
                    items(userList.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // First / Second item
                            rowItems.forEach { item ->
                                GridItem(
                                    modifier = Modifier.weight(1f),
                                    navController = navController as NavHostController,
                                    data = item!!,
                                    viewModel = viewModelM4,
                                    authViewModel = authViewModel,
                                    icon = false, planType = planType,showPlanPopUp = { showPlanPopUp = it }
                                )
                            }

                            // ✅ IMPORTANT FIX:
                            // Agar sirf 1 item hai, to dusra empty space add karo
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                }

                item {
                    verticalSpace(100)
                }
            }





        }
    }

    if(showPlanPopUp){
        PlanPopUp(onDismiss ={ showPlanPopUp = false}, navController)
    }


}