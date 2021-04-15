package com.example.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.model.PaymentIntent;

import java.util.HashMap;

public class OfertFragmentPay extends Fragment implements View.OnClickListener{

    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    //private static final String BACKEND_URL = "https://stripe-payment-backend-what.herokuapp.com/";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;

    View mView;
    Button btnAccept, btnCancell, btnFinish;
    LinearLayout buttons1st, buttons2nd, loading_payment;

    TextView title, location, price;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pay, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();

        btnAccept = mView.findViewById(R.id.btn_accept);
        btnCancell = mView.findViewById(R.id.btn_cancell);
        btnFinish = mView.findViewById(R.id.btn_finish);

        buttons1st = mView.findViewById(R.id.buttonCreditHome);
        buttons2nd = mView.findViewById(R.id.buttonCreditFinish);
        loading_payment = mView.findViewById(R.id.loading_payment);

        title = mView.findViewById(R.id.ofert_title);
        location = mView.findViewById(R.id.ofert_location);
        price = mView.findViewById(R.id.pay_amount);

        title.setText(getArguments().getString("title"));
        location.setText(getArguments().getString("event")+", "+getArguments().getString("localitzacio"));
        price.setText(getArguments().getString("price")+" €");

        btnAccept.setOnClickListener(this);
        btnCancell.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        stripe = new Stripe(
                getActivity().getApplicationContext(),
                Objects.requireNonNull("pk_test_51IfnlWF0WdHSkK4FOuh1tLZoMQFU4XudmUt568HwdP8WiilSkbF2jH8rRodE7whryYt16PD06wZSujQdMp7Bd1LP00BDqekftm")
        );
        startCheckout();

        return mView;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancell:
                getFragmentManager().beginTransaction().replace(R.id.fragment_main, new OfertFragment()).commit();
                break;
            case R.id.btn_finish:
                updateUserOfert();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main, new OfertFragment()).commit();
                break;
        }
    }


    private void updateUserOfert() {

        final DocumentReference ofertDocument = mFirestore.collection("users").document(userID).collection("PaidOferts").document(getArguments().getString("document"));

        String TitleOfert = getArguments().getString("title");
        String EventOfert = getArguments().getString("event");
        String LocalizationOfert = getArguments().getString("localitzacio");

        Map<String, Object> data = new HashMap<>();

        data.put("info", TitleOfert+", "+EventOfert+", "+LocalizationOfert);
        data.put("acquired", true);

        ofertDocument.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess : ofert Added with Id "+ getArguments().getString("document"));
            }
        });
    }


    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        String stringamount = getArguments().getString("price").replace(",", "")+".00";
        //Pagament mínim desde Stripe
        if(stringamount.equals("000.00")){
            stringamount = "050.00";
        }

        double amount = Double.parseDouble(stringamount);
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "eur");
        itemMap.put("id", getArguments().getString("title")+" - " +getArguments().getString("event")+", "+getArguments().getString("localitzacio"));
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);
        String json = new Gson().toJson(payMap);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance

        btnAccept.setOnClickListener((View view) -> {
            loading_payment.setVisibility(View.VISIBLE);

            CardInputWidget cardInputWidget = new CardInputWidget(getContext());
            cardInputWidget.setCardNumber("4242424242424242");
            cardInputWidget.setCvcCode("123");
            cardInputWidget.setExpiryDate(12,25);
            cardInputWidget.setPostalCodeEnabled(false);

            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }

    private class PayCallback implements Callback {
        @NonNull
        private final WeakReference<OfertFragmentPay> activityRef;
        PayCallback(@NonNull OfertFragmentPay activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final OfertFragmentPay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("ERROR-SOCKET", e.toString());
                }
            });

        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final OfertFragmentPay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Error: " + response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR-CONNECTION", response.toString());
                    }
                });
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<OfertFragmentPay> activityRef;
        PaymentResultCallback(@NonNull OfertFragmentPay activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final OfertFragmentPay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                buttons1st.setVisibility(View.GONE);
                buttons2nd.setVisibility(View.VISIBLE);
                loading_payment.setVisibility(View.GONE);

                getFragmentManager().beginTransaction().replace(R.id.fragment_ofertpay, new PayCardFragment_Complete()).commit();

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                loading_payment.setVisibility(View.GONE);

                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final OfertFragmentPay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
            Log.d("ERROR-PAYMENT_RESULT", e.toString());
        }
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}
