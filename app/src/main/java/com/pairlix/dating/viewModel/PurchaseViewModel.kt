package com.pairlix.dating.viewModel

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pairlix.dating.MyApplication
import com.pairlix.dating.R
import com.pairlix.dating.data.repository.AuthRepository
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.InternetConnection
import com.pairlix.dating.requests.PurchasedPlanRequest
import com.pairlix.dating.response.PurchasedPlanResponse
import com.pairlix.dating.utils.BillingManager
import com.pairlix.dating.utils.LocationHelper
import com.pairlix.dating.utils.SingletonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val billingManager: BillingManager,
    private val locationHelper: LocationHelper,
    private val authRepository: AuthRepository

    ) : ViewModel() {
    var isNetworkAvailable = MutableLiveData(true)


    var purchaseState by mutableStateOf("")
        private set

    var products by mutableStateOf<List<ProductDetails>>(emptyList())
        private set

    var selectedOfferTokens by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    init {
        // ✅ Collects once BillingManager emits — no timing issue
        viewModelScope.launch {
            billingManager.productsFlow.collect { loaded ->
                products = loaded
                selectedOfferTokens = loaded.associate { product ->
                    val firstToken = product.subscriptionOfferDetails
                        ?.firstOrNull()?.offerToken ?: ""
                    product.productId to firstToken
                }
            }
        }
        viewModelScope.launch {
            billingManager.purchaseSuccessFlow.collect { purchase ->
                onPurchaseSuccess(purchase)
            }
        }
    }



    fun getOffersForProduct(product: ProductDetails) =
        billingManager.getOffersForProduct(product)

    fun buy(activity: Activity, product: ProductDetails, token: String) {
        if (token.isBlank()) {
            purchaseState = "Please select a plan"
            return
        }
        billingManager.launchPurchase(activity, product, token)
    }

    fun restore() {
        billingManager.restorePurchases()
    }

    private fun onPurchaseSuccess(purchase: Purchase) {
        viewModelScope.launch {
            val product = products.find {
                purchase.products.contains(it.productId)
            } ?: return@launch

            val selectedToken = selectedOfferTokens[product.productId] ?: ""
            val offerDetails = product.subscriptionOfferDetails
                ?.find { it.offerToken == selectedToken }

            // ── Build dates ──
            val purchaseDate = SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault()
            ).format(Date(purchase.purchaseTime))

            val duration = offerDetails?.basePlanId ?: "monthly"

            val expireDate = calculateExpireDate(purchaseDate, duration)

            // ── Build request ──
            val request = PurchasedPlanRequest(
                countryName = locationHelper.getCountry(),  // or pass from UI
                duration = duration,
                expiredOn = expireDate,
                planType = if (SingletonObject.isComeFromPlatinumPlan)3 else 2,       // "gold" / "platinum"
                price = offerDetails?.pricingPhases
                    ?.pricingPhaseList
                    ?.lastOrNull()
                    ?.formattedPrice ?: "",
                purchasedOn = purchaseDate,
                paymentStatus = 0
            )
            hitPurchasedPlans(SingletonObject.accessToken,request)


            // ── Hit API ──

        }
    }

    private fun calculateExpireDate(purchaseDate: String, duration: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = sdf.parse(purchaseDate) ?: Date()
            when {
                duration.contains("year", ignoreCase = true) ->
                    add(Calendar.YEAR, 1)
                duration.contains("month", ignoreCase = true) ->
                    add(Calendar.MONTH, 1)
                duration.contains("week", ignoreCase = true) ->
                    add(Calendar.WEEK_OF_YEAR, 1)
                else -> add(Calendar.MONTH, 1)
            }
        }
        return sdf.format(calendar.time)
    }

    fun selectOffer(productId: String, offerToken: String) {
        selectedOfferTokens = selectedOfferTokens.toMutableMap().apply {
            put(productId, offerToken)
        }
    }
    fun checkInternetConnection(): Boolean {
        return if (InternetConnection.checkConnection(MyApplication.appContext)) {
            true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                isNetworkAvailable.value = false
            }
            false
        }
    }
    private val _purchasePlan = MutableLiveData<EmpResource<PurchasedPlanResponse>>()
    val purchasePlan: LiveData<EmpResource<PurchasedPlanResponse>>
        get() = _purchasePlan


    fun hitPurchasedPlans(access_token: String, request: PurchasedPlanRequest) {
        if (checkInternetConnection()) viewModelScope.launch {
            _purchasePlan.value = EmpResource.Loading
            _purchasePlan.value = authRepository.purchasePlan(access_token, request)
        }
        else {
            Toast.makeText(
                MyApplication.appContext,
                MyApplication.appContext.getString(R.string.no_network_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}