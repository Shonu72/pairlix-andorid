package com.pairlix.dating.view.M7

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomInputField
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CommonResource
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.formatDate
import com.pairlix.dating.network.AddTicketRequest
import com.pairlix.dating.response.GetTicketResponse
import com.pairlix.dating.view.M4.CountryMultiSelectBottomSheet
import com.pairlix.dating.view.M4.DrinkBottomSheet
import com.pairlix.dating.view.M4.DrinkSmokeSheet
import com.pairlix.dating.view.M4.FilterEditType
import com.pairlix.dating.view.M4.LanguageBottomSheetFilter
import com.pairlix.dating.view.M4.ProfessionBottomSheet
import com.pairlix.dating.view.M4.UserTypeBottomSheet
import com.pairlix.dating.view.profileDetails.MaritalStatusBottomSheet
import com.pairlix.dating.view.profileDetails.SectBottomSheet
import com.pairlix.dating.viewModel.M7ViewModel
import kotlin.reflect.typeOf
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pairlix.dating.ReusedComponents.AttachmentUploadSection
import com.pairlix.dating.ReusedComponents.showDatePicker
import com.pairlix.dating.helper.convertDateForApi
import com.pairlix.dating.view.newAccountRegistrationScreen.uploadImageObserverStep1
import com.pairlix.dating.viewModel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.graphics.ColorFilter
import com.pairlix.dating.ReusedComponents.convertDateForApii
import com.pairlix.dating.ReusedComponents.toEnglishDigits
import com.pairlix.dating.navigation.Screen
import ir.kaaveh.sdpcompose.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(navController: NavController, viewModel: AuthViewModel, m7ViewModel: M7ViewModel) {

    var searchText by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var fromDate by remember { mutableStateOf("") }
    var ticketType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachment by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var expandedTicketType by remember { mutableStateOf(false) }
    var expandedStatusFilter by remember { mutableStateOf(false) } // NEW: Status filter dropdown state
    var selectedStatus by remember { mutableIntStateOf(-1) } // NEW: -1 = All, 0 = Pending, 1 = Resolved
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val getTicket by m7ViewModel.getTicket.collectAsState()
    val addTicket by m7ViewModel.addTicket.collectAsState()
    val ticketData = remember { mutableStateListOf<GetTicketResponse.Data>() }
    val pendingIndices = remember { mutableStateListOf<Int>() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity ?: return
    var attachmentUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    val issueList = context.resources.getStringArray(R.array.ticket_issue_types).toList()

    // String resources
    val strTicket = stringResource(R.string.ticket)
    val strChooseDate = stringResource(R.string.choose_date)
    val strFrom = stringResource(R.string.from)
    val strTo = stringResource(R.string.to)
    val strSearch = stringResource(R.string.search)
    val strNoResultsFound = stringResource(R.string.no_results_found)
    val strNoTicketsFound = stringResource(R.string.no_tickets_found)
    val strAddTicket = stringResource(R.string.add_ticket)
    val strTitle = stringResource(R.string.title)
    val strEnterTitle = stringResource(R.string.enter_title)
    val strTicketType = stringResource(R.string.ticket_type)
    val strSelect = stringResource(R.string.select)
    val strDescription = stringResource(R.string.description)
    val strEnterDescription = stringResource(R.string.enter_description)
    val strAdd = stringResource(R.string.add)
    val strPleaseEnterTitle = stringResource(R.string.please_enter_title)
    val strPleaseEnterTicketType = stringResource(R.string.please_enter_ticket_type)
    val strPleaseEnterDescription = stringResource(R.string.please_enter_description)
    val strPleaseSelectFromDate = stringResource(R.string.please_select_from_date_first)
    val strAll = stringResource(R.string.all) // NEW
    val strPending = stringResource(R.string.pending) // NEW
    val strResolved = stringResource(R.string.resolved) // NEW
    val rejectedIndices = mutableListOf<Int>()
    LaunchedEffect(Unit) {
        m7ViewModel.hitGetTicket(
            SharedPreference.get(context).accessToken,
            search = "",
            from = "",
            to = "",
            status = null // NEW: Initial load shows all
        )
        uploadImageObserverStep1(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            onUploadSuccess = {
                attachmentUrls = viewModel.uploadedImageUrls.filter { it.isNotEmpty() }
            },
            onUploadStart = {},
            onAllUploadsDone = {},
            pendingIndices = pendingIndices,
            // ✅🔥 NEW BLOCK (VERY IMPORTANT)
            onUploadFailed = { /*failedIndices ->
                val updated = images.toMutableList()
                failedIndices.forEach { index ->
                    if (index < updated.size) {
                        updated[index] = null
                    }
                }
                images = updated*/
            },
            onRejectedIndices= { rejectedIndices }

        )
    }

    // NEW: Updated LaunchedEffect to include status filter
    LaunchedEffect(searchText, toDate, selectedStatus) {
        val fromApi = if (fromDate.isNotEmpty()) convertDateForApii(fromDate) else ""
        val toApi = if (toDate.isNotEmpty()) convertDateForApii(toDate) else ""
        val statusParam = if (selectedStatus == -1) null else selectedStatus

        m7ViewModel.hitGetTicket(
            SharedPreference.get(context).accessToken,
            search = searchText,
            from = fromApi,
            to = toApi,
            status = statusParam
        )
    }

    LaunchedEffect(getTicket) {
        getTicket.let {
            when (it) {
                is EmpResource.Loading -> {}
                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    ticketData.clear()
                    ticketData.addAll(it.value.data?.filterNotNull() ?: emptyList())
                    m7ViewModel.resetGetTicket()
                }
                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    it.throwable.let { err -> ErrorUtil.handlerGeneralError(context, err) }
                    m7ViewModel.resetGetTicket()
                }
                EmpResource.Idle -> { CustomLoader.hideLoader() }
            }
        }
    }

    LaunchedEffect(addTicket) {
        addTicket.let {
            when (it) {
                is EmpResource.Loading -> {}
                is EmpResource.Success -> {
                    CustomLoader.hideLoader()
                    context.showToast(it.value.message ?: "")
                    showBottomSheet = false
                    viewModel.uploadedImageUrls.fill("")
                    pendingIndices.clear()
                    attachmentUrls = emptyList()
                    title = ""
                    ticketType = ""
                    description = ""
                    m7ViewModel.hitGetTicket(
                        SharedPreference.get(context).accessToken,
                        search = "",
                        from = "",
                        to = "",
                        status = null
                    )
                    m7ViewModel.resetAddTicket()
                }
                is EmpResource.Failure -> {
                    CustomLoader.hideLoader()
                    val err = (addTicket as EmpResource.Failure).throwable
                    ErrorUtil.handlerGeneralError(context, err)
                    m7ViewModel.resetAddTicket()
                }
                EmpResource.Idle -> {}
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        val maxHeight = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {

            TopBackBtnHeading(navController, strTicket)
            verticalSpace(20)

            Text(
                text = strChooseDate,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            verticalSpace(10)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ✅ FROM DATE - Works perfectly
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            showDatePicker(context) { date ->
                                fromDate = date
                                toDate = "" // Clear toDate when fromDate changes
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (fromDate.isEmpty()) strFrom else fromDate,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                    Image(
                        painter = painterResource(R.drawable.calendar_icon),
                        contentDescription = "calendar",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                // ✅ TO DATE - FIXED with English digits conversion
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (fromDate.isEmpty()) {
                                context.showToast(strPleaseSelectFromDate)
                                return@clickable
                            }

                            // ✅ CRITICAL FIX: Convert fromDate to English digits BEFORE parsing
                            val englishFromDate = fromDate.toEnglishDigits()
                            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                            val calendar = Calendar.getInstance(Locale.ENGLISH)

                            try {
                                calendar.time = format.parse(englishFromDate)!!
                                // ✅ Pass minDate as Long
                                showDatePicker(
                                    context = context,
                                    minDate = calendar.timeInMillis
                                ) { date ->
                                    toDate = date
                                }
                            } catch (e: Exception) {
                                // ✅ Better error handling
                                context.showToast("Invalid from date. Please select again.")
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (toDate.isEmpty()) strTo else toDate,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                    Image(
                        painter = painterResource(R.drawable.calendar_icon),
                        contentDescription = "calendar",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            verticalSpace(20)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = {
                            Text(
                                text = strSearch,
                                color = Color(0xFF7C7C7C),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF7C7C7C)
                            )
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .height(38.dp)
                                        .width(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                )
                                horizontalSpace(8)
                                Icon(
                                    painter = painterResource(R.drawable.three_line_filter_ic),
                                    contentDescription = "filter",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            expandedStatusFilter = !expandedStatusFilter
                                        },
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                            cursorColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.sdp)
                    )

                    // NEW: Status Filter Dropdown
                    DropdownMenu(
                        expanded = expandedStatusFilter,
                        onDismissRequest = { expandedStatusFilter = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        // All
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strAll,
                                        color = if (selectedStatus == -1) Color(0xFF8B5DF6) else MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                    )
                                    if (selectedStatus == -1) {
                                        Icon(
                                            painter = painterResource(R.drawable.tick_icon),
                                            contentDescription = null,
                                            tint = Color(0xFF8B5DF6),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedStatus = -1
                                expandedStatusFilter = false
                            }
                        )

                        // Pending
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strPending,
                                        color = if (selectedStatus == 0) Color(0xFF8B5DF6) else MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                    )
                                    if (selectedStatus == 0) {
                                        Icon(
                                            painter = painterResource(R.drawable.tick_icon),
                                            contentDescription = null,
                                            tint = Color(0xFF8B5DF6),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedStatus = 0
                                expandedStatusFilter = false
                            }
                        )

                        // Resolved
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strResolved,
                                        color = if (selectedStatus == 1) Color(0xFF8B5DF6) else MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                                    )
                                    if (selectedStatus == 1) {
                                        Icon(
                                            painter = painterResource(R.drawable.tick_icon),
                                            contentDescription = null,
                                            tint = Color(0xFF8B5DF6),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedStatus = 1
                                expandedStatusFilter = false
                            }
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.plus_ic),
                    contentDescription = "plus",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { showBottomSheet = true }
                )
            }

            verticalSpace(30)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (ticketData.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = if (searchText.isNotEmpty()) strNoResultsFound else strNoTicketsFound,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(ticketData) {
                            TicketCard(data = it, onclick = {
                                m7ViewModel.setData(it)
                                navController.navigate(Screen.TicketDetailsScreen.route)
                            })
                            verticalSpace(20)
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                dragHandle = null,
                onDismissRequest = {
                    showBottomSheet = false
                    viewModel.uploadedImageUrls.fill("")
                    pendingIndices.clear()
                    attachmentUrls = emptyList()
                    title = ""
                    ticketType = ""
                    description = ""
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF14590988))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strAddTicket,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(R.drawable.cross_pruple_ic),
                            contentDescription = "close",
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showBottomSheet = false
                                    viewModel.uploadedImageUrls.fill("")
                                    pendingIndices.clear()
                                    attachmentUrls = emptyList()
                                    title = ""
                                    ticketType = ""
                                    description = ""
                                }
                        )
                    }

                    verticalSpace(20)

                    Column(modifier = Modifier.padding(16.dp)) {

                        CustomInputField(
                            heading = strTitle,
                            value = title,
                            onValueChange = { title = noInitialSpace(it) },
                            placeholder = strEnterTitle,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            maxLines = 1,
                            singleLine = true,
                        )

                        verticalSpace(20)

                        Text(
                            text = strTicketType,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                        verticalSpace(8)

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { expandedTicketType = true }
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (ticketType.isEmpty()) strSelect else ticketType,
                                    color = if (ticketType.isEmpty()) Color(0xFF7C7C7C) else MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "dropdown",
                                    tint = Color(0xFF7C7C7C)
                                )
                            }

                            DropdownMenu(
                                expanded = expandedTicketType,
                                onDismissRequest = { expandedTicketType = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                issueList.forEach { issue ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = issue,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(Font(R.font.axiforma_regular))
                                            )
                                        },
                                        onClick = {
                                            ticketType = issue
                                            expandedTicketType = false
                                        }
                                    )
                                }
                            }
                        }

                        verticalSpace(20)

                        CustomInputField(
                            heading = strDescription,
                            value = description,
                            onValueChange = { description = noInitialSpace(it) },
                            placeholder = strEnterDescription,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            minLines = 5,
                            maxLines = 6,
                            singleLine = false,
                        )

                        verticalSpace(20)

                        AttachmentUploadSection(
                            viewModel = viewModel,
                            pendingIndices = pendingIndices,
                            activity = activity,
                            onUrlsUpdated = { urls -> attachmentUrls = urls }
                        )

                        AppButton(
                            modifier = Modifier,
                            text = strAdd,
                            onClick = {

                                when {
                                    title.isBlank() -> context.showToast(strPleaseEnterTitle)
                                    ticketType.isBlank() -> context.showToast(strPleaseEnterTicketType)
                                    description.isBlank() -> context.showToast(strPleaseEnterDescription)

                                    // ✅ WAIT FOR UPLOAD
                                    pendingIndices.isNotEmpty() -> {
                                        context.showToast("Please wait, image uploading...")
                                    }

                                    // ✅ CHECK IMAGE URL
                                    attachmentUrls.isEmpty() -> {
                                        context.showToast("Please upload image first")
                                    }

                                    else -> {
                                        m7ViewModel.hitAddTicket(
                                            SharedPreference.get(context).accessToken,
                                            request = AddTicketRequest(
                                                attachImage = attachmentUrls,
                                                description = description,
                                                ticketType = ticketType,
                                                titleName = title
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketCard(data: GetTicketResponse.Data, onclick: () -> Unit) {
    val strPending = stringResource(R.string.pending)
    val strResolved = stringResource(R.string.resolved)
    val strNA = stringResource(R.string.n_a)
    val strTicketId = stringResource(R.string.ticket_id)
    val strTitlePrefix = stringResource(R.string.title_prefix)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$strTitlePrefix${data.titleName ?: ""}",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
                Text(
                    modifier = Modifier,
                    text = when (data.status) {
                        0 -> strPending
                        1 -> strResolved
                        else -> strNA
                    },
                    color = if (data?.status==0) Color(0xFF153EC5) else Color.Green,

                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
            }

            verticalSpace(10)

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$strTicketId${data.ticketId ?: ""}",
                    color = Color(0xFF6D6D6D),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier,
                        text = formatDate(data.createdAt ?: ""),
                        color = Color(0xFF6D6D6D),
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_medium))
                    )
                    horizontalSpace(5)
                    Image(
                        painter = painterResource(R.drawable.calendar_date),
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}