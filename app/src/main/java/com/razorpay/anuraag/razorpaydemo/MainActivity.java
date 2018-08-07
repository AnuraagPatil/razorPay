package com.razorpay.anuraag.razorpaydemo;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    @BindView(R.id.input_layout_contact)
    TextInputLayout getInputLayoutContact;
    @BindView(R.id.input_layout_email)
    TextInputLayout getInputLayoutEmail;
    @BindView(R.id.input_layout_amount)
    TextInputLayout getInputLayoutAmount;

    @BindView(R.id.input_contact)
    EditText getInputContact;
    @BindView(R.id.input_email)
    EditText getInputEmail;
    @BindView(R.id.input_amount)
    EditText getInputAmount;

    @BindView(R.id.toolbar)
    Toolbar getToolbar;
    @BindView(R.id.btn_pay)
    Button getPayButton;

    private static final String TAG = "logPaymentActivity";
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //for faster checkout
        Checkout.preload(getApplicationContext());

        getToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(getToolbar);

        getInputContact.addTextChangedListener(new MyTextWatcher(getInputContact));
        getInputEmail.addTextChangedListener(new MyTextWatcher(getInputEmail));
        getInputAmount.addTextChangedListener(new MyTextWatcher(getInputAmount));

        getPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        if (!validateContact()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validateAmount()) {
            return;
        }

        startPayment();
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateContact() {
        if (getInputContact.getText().toString().trim().isEmpty()) {
            getInputLayoutContact.setError(getString(R.string.err_msg_contact));
            requestFocus(getInputContact);
            return false;
        } else {
            getInputLayoutContact.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = getInputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            getInputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(getInputEmail);
            return false;
        } else {
            getInputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAmount() {
        if (getInputAmount.getText().toString().trim().isEmpty()) {
            getInputLayoutAmount.setError(getString(R.string.err_msg_amount));
            requestFocus(getInputAmount);
            return false;
        } else {
            getInputLayoutAmount.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void startPayment() {
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Testing mode");
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "Anuraag0551@gmail.com");
            preFill.put("contact", "8329133027");

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
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_contact:
                    validateContact();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_amount:
                    validateAmount();
                    break;
            }
        }
    }
}
