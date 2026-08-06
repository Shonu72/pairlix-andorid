package com.pairlix.dating.view.M6

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.ReusedComponents.NotificationCard
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.GetNotificationResponse
import com.pairlix.dating.viewModel.M7ViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.pairlix.dating.R

@Composable
fun NotificationScreen(navController: NavController,m7ViewModel: M7ViewModel) {

    val context = LocalContext.current
    val getNotification by m7ViewModel.getNotification.collectAsState()
    
    val notificationList= remember{ mutableStateListOf<GetNotificationResponse.Data>() }


LaunchedEffect(Unit) {
    m7ViewModel.hitGetNotification(access_token =SharedPreference.get(context).accessToken)

     }

    LaunchedEffect(getNotification) {
        getNotification.let {

            when (it) {

                is EmpResource.Loading -> {
                     CustomLoader.showLoader(context as MainActivity)
                }

                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    notificationList.clear()
                    notificationList.addAll(it.value.data?.filterNotNull() ?: emptyList())
                    m7ViewModel.resetGetTicket()

                }


                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }
                    m7ViewModel.resetGetTicket()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }


        }

    }


    

    

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)

        ) {
            TopBackBtnHeading(navController = navController, text = stringResource(R.string.notifications))
            verticalSpace(30)

            if (notificationList.isEmpty()) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(R.string.no_notifications),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {

                    items(notificationList) {
                        NotificationCard(it)


                        verticalSpace(20)
                    }

                }

            }
          


        }


    }


}