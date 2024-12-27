package com.pwhs.quickmem.presentation.app.paywall

import androidx.compose.runtime.Composable
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.restorePurchasesWith
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialog
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialogOptions
import com.revenuecat.purchases.ui.revenuecatui.PaywallListener
import timber.log.Timber

@Composable
fun Paywall(
    isPaywallVisible: Boolean,
    onCustomerInfoChanged: (CustomerInfo) -> Unit,
    onPaywallDismissed: () -> Unit,
    userId: String = "",
) {
    if (isPaywallVisible) {
        PaywallDialog(
            paywallDialogOptions = PaywallDialogOptions.Builder()
                .setListener(
                    object : PaywallListener {

                        override fun onPurchaseError(error: PurchasesError) {
                            super.onPurchaseError(error)
                            Timber.tag("PaywallListener").e("purchase error: ${error.message}")
                            onPaywallDismissed()
                        }

                        override fun onPurchaseCancelled() {
                            super.onPurchaseCancelled()
                            Timber.tag("PaywallListener").d("Purchased Cancelled")
                            onPaywallDismissed()
                        }

                        override fun onPurchaseStarted(rcPackage: Package) {
                            super.onPurchaseStarted(rcPackage)
                            Timber.tag("PaywallListener")
                                .d("Purchased Started - package: ${rcPackage.identifier}")
                        }

                        override fun onPurchaseCompleted(
                            customerInfo: CustomerInfo,
                            storeTransaction: StoreTransaction,
                        ) {
                            super.onPurchaseCompleted(customerInfo, storeTransaction)
                            Timber.tag("PaywallListener")
                                .d("Purchased Completed - customerInfo: $customerInfo, storeTransaction: $storeTransaction")
                            onCustomerInfoChanged(customerInfo)
                            onPaywallDismissed()
                        }

                        override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                            super.onRestoreCompleted(customerInfo)
                            Timber.tag("PaywallListener")
                                .d("Restore Completed - customerInfo: $customerInfo")
                            onPaywallDismissed()
                        }

                        override fun onRestoreError(error: PurchasesError) {
                            super.onRestoreError(error)
                            Timber.tag("PaywallListener").e("restore error: ${error.message}")
                            onPaywallDismissed()
                        }

                        override fun onRestoreStarted() {
                            super.onRestoreStarted()
                            Timber.tag("PaywallListener").d("Restore Started")
                            Purchases.sharedInstance.restorePurchasesWith { customerInfoRestore ->
                                if (customerInfoRestore.originalAppUserId == userId) {
                                    onCustomerInfoChanged(customerInfoRestore)
                                } else {
                                    onPaywallDismissed()
                                }
                            }
                        }
                    }
                )
                .build()
        )
    }
}