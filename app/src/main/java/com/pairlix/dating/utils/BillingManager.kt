package com.pairlix.dating.utils

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(          // ✅ only context, no lambdas
    private val context: Context
) {
    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { purchase -> handlePurchase(purchase) }
        }
    }

    private val _purchaseSuccessFlow = MutableSharedFlow<Purchase>(extraBufferCapacity = 1)
    val purchaseSuccessFlow: SharedFlow<Purchase> = _purchaseSuccessFlow.asSharedFlow()

    private val _products = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productsFlow: StateFlow<List<ProductDetails>> = _products.asStateFlow()

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchaseListener)
        .enablePendingPurchases()
        .build()

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                startConnection()
            }
        })
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    _purchaseSuccessFlow.tryEmit(purchase)  // ✅ only flow, no lambda
                }
            }
        }
    }

    fun queryProducts() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("gold_plan")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("platinum_plan")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, productDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                _products.value = productDetailsList
            }
        }
    }

    fun getOffersForProduct(product: ProductDetails): List<ProductDetails.SubscriptionOfferDetails> {
        return product.subscriptionOfferDetails ?: emptyList()
    }

    fun launchPurchase(activity: Activity, product: ProductDetails, offerToken: String) {
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .setOfferToken(offerToken)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun restorePurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { _, purchases ->
            purchases.forEach { handlePurchase(it) }
        }
    }
}