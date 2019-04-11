@file:Suppress("DEPRECATION")

package com.idappstudio.innabajka

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.idappstudio.innabajka.pay.GooglePay
import kotlinx.android.synthetic.main.activity_navigation.*
import org.json.JSONException
import org.json.JSONObject
import com.fujiyuu75.sequent.Animation
import com.fujiyuu75.sequent.Sequent
import com.idappstudio.innabajka.menu.MenuActivity


/**
 * Checkout implementation for the app
 */
class NavigationActivity : AppCompatActivity() {

    /**
     * A client for interacting with the Google Pay API.
     *
     * @see [PaymentsClient](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient)
     */
    private lateinit var paymentsClient: PaymentsClient

    /**
     * Arbitrarily-picked constant integer you define to track a request for payment data activity.
     *
     * @value #LOAD_PAYMENT_DATA_REQUEST_CODE
     */
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    private lateinit var btnTip: Button
    private lateinit var btnClose2: ImageButton

    private lateinit var text: TextView
    private lateinit var image: ImageView
    private lateinit var loading: ProgressBar

    private lateinit var amountTip: EditText

    private lateinit var alertDialog: AlertDialog

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see Activity.onCreate
     */
    @SuppressLint("InflateParams", "PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        Sequent
            .origin(navigationLayout)
            .anim(this, Animation.BOUNCE_IN)
            .start()

        val dialogBuilder = AlertDialog.Builder(this,
            R.style.Base_Theme_MaterialComponents_Dialog
        )

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_tip, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorTranspery)))
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        btnClose.setOnClickListener {
            finish()
        }

        btnMenu.setOnClickListener {

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()

        }

        button.setOnClickListener {

            alertDialog.show()

            if(alertDialog.isShowing){

                btnClose2 = alertDialog.findViewById(R.id.btnClose2)
                btnTip = alertDialog.findViewById(R.id.btn_tip)
                amountTip = alertDialog.findViewById(R.id.tip_amount)
                image = alertDialog.findViewById(R.id.imageView2)
                text = alertDialog.findViewById(R.id.textView)
                loading = alertDialog.findViewById(R.id.loading)

                possiblyShowGooglePayButton()

                btnClose2.setOnClickListener {

                    if(alertDialog.isShowing){

                        alertDialog.dismiss()

                    }

                }

                btnTip.setOnClickListener {

                    if(amountTip.text.isNullOrBlank()){

                        amountTip.error = "Wpisz wartość napiwku"

                    } else {

                        if(amountTip.text.toString().toDouble() >= 1.00){

                            btnClose2.visibility = View.GONE
                            btnTip.visibility = View.GONE
                            amountTip.visibility = View.GONE
                            image.visibility = View.GONE
                            text.visibility = View.GONE
                            loading.visibility = View.VISIBLE

                            requestPayment()

                        } else {

                            amountTip.error = "Wpisz wartość napiwku większą niż 0.99"

                        }

                    }

                }

            }

        }

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = GooglePay.createPaymentsClient(this)

    }

    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient.html.isReadyToPay
    ) */
    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = GooglePay.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {

        if (available) {

            btnTip.visibility = View.VISIBLE

        } else {

            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG).show()

        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        btnTip.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.

        val paymentDataRequestJson = GooglePay.getPaymentDataRequest(amountTip.text.toString())
        if (paymentDataRequestJson == null) {

            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    Activity.RESULT_CANCELED -> {
                        // Nothing to do here normally - the user simply cancelled without selecting a
                        // payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            Log.d("Pay | Error", it.statusCode.toString())
                        }
                    }
                }
                // Re-enables the Google Pay payment button.
                btnTip.isClickable = true
            }
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {

        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData

                    .getJSONObject("tokenizationData")
                    .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token") == "examplePaymentMethodToken") {

                AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Gateway name set to \"example\" - please modify " +
                            "Constants.java and replace it with your own gateway.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()

                if(alertDialog.isShowing){

                    alertDialog.dismiss()

                }

            }

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $e")
        }

    }

}
