package in.patient.doctorplus.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.AppointmentsAdapter;
import in.patient.doctorplus.adapter.WalletTransactionAdapter;
import in.patient.doctorplus.model.Transaction;
import in.patient.doctorplus.model.WalletDetailsModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BaseActivity;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends BaseActivity {

    private static final String TAG = "MyWalletActivity";
    private String retrivedToken;
    private AlertDialog progressDialog;
    private TextView availAmount;
    private RecyclerView transactionRecyclerView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_my_wallet;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        availAmount = findViewById(R.id.availAmount);
        transactionRecyclerView = findViewById(R.id.transactionRecyclerView);
        retrivedToken = CommonSetting.getToken();

        Log.d(retrivedToken, "getUserDetails: " + retrivedToken);
        System.out.print(retrivedToken);

        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        GetWalletDetail("Token " + retrivedToken);

    }

    private ArrayList<Transaction> transactions = new ArrayList<>();

    private void GetWalletDetail(String retriveToke) {
        progressDialog.show();
        Call<List<WalletDetailsModel>> listCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetWalletDetails(retriveToke);

        listCall.enqueue(new Callback<List<WalletDetailsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<WalletDetailsModel>> call, Response<List<WalletDetailsModel>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().size() > 0) {
                        Toast.makeText(MyWalletActivity.this, "jbdfjhdfjhdj", Toast.LENGTH_SHORT).show();
                        availAmount.setText("₹ " + response.body().get(0).getWallet().getBalance().toString());
                        transactions = (ArrayList<Transaction>) response.body().get(0).getTransaction();
                        Log.d(TAG, "onResponse: " + transactions);
                        setRecyclerView(transactions);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<WalletDetailsModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });

    }

    private void setRecyclerView(ArrayList<Transaction> transactions) {
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        WalletTransactionAdapter walletTransactionAdapter = new WalletTransactionAdapter(transactions, this);
        transactionRecyclerView.setAdapter(walletTransactionAdapter);
    }


}