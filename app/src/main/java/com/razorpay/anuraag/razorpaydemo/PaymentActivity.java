package com.razorpay.anuraag.razorpaydemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "logPaymentActivity";
    private Activity activity = this;
    private String getAmount;
    private String getEmail;
    private String getContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle bundle = getIntent().getExtras();
        getAmount = bundle.getString(String.valueOf(R.string.intent_key_amount));
        getContact = bundle.getString(String.valueOf(R.string.intent_key_contact));
        getEmail = bundle.getString(String.valueOf(R.string.intent_key_email));

        startPayment();
    }

    private void startPayment() {
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Testing mode");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount",getAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", getEmail);
            preFill.put("contact", getContact);

            options.put("prefill", preFill);

            co.open(this, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}
