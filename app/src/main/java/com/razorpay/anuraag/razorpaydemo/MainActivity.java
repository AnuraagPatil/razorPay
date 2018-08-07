package com.razorpay.anuraag.razorpaydemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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

        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(getString(R.string.intent_key_contact), getInputContact.getText().toString());
        intent.putExtra(getString(R.string.intent_key_email), getInputEmail.getText().toString());
        intent.putExtra(getString(R.string.intent_key_amount), getInputAmount.getText().toString());
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateContact() {
        String getContact = getInputContact.getText().toString().trim();
        if (getContact.isEmpty() || !isValidContact(getContact)) {
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

    private boolean isValidContact(String getContact) {
        return !TextUtils.isEmpty(getContact) && getContact.length() == 10;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
