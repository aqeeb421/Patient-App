package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import in.patient.doctorplus.R;
import in.patient.doctorplus.model.ConsultationModel;
import in.patient.doctorplus.model.InvoiceNumberModel;
import in.patient.doctorplus.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView feeBack_dismiss;
    private EditText feeBack_edt_dialog;
    private Button feeBack_button_submit;
    private RatingBar feedBack_ratingBar, feedBack_video_ratingBar, feedBack_consult_ratingBar;
    private String invoiceNo, feedBackMsg;
    private int rating1, rating2, rating3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //hide status bar https://developer.android.com/training/system-ui/status
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();


    }

    private void GetInvoiceNumber() {
        /*Do API  call here*/
        Call<InvoiceNumberModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetInvoiceNumber();

        call.enqueue(new Callback<InvoiceNumberModel>() {
            @Override
            public void onResponse(Call<InvoiceNumberModel> call, Response<InvoiceNumberModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    invoiceNo = response.body().getInvNum();
                    Log.d("TAG", "onResponse: " + invoiceNo);

                    SendConsultationDetails();
                }
            }

            @Override
            public void onFailure(Call<InvoiceNumberModel> call, Throwable t) {

            }
        });
    }

    private void SendConsultationDetails() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("LogIn Details", Context.MODE_PRIVATE);
        String retrivedToken = preferences.getString("token", null);//second parameter default value.

        Call<ConsultationModel> modelCall = RetrofitClient
                .getInstance()
                .getApi()
                .SendConsultationDetails("Token " + retrivedToken, invoiceNo, rating1, rating2, rating3, feedBackMsg, "2:03", 360.00, 1);

        modelCall.enqueue(new Callback<ConsultationModel>() {
            @Override
            public void onResponse(Call<ConsultationModel> call, Response<ConsultationModel> response) {

                if (response.code() == 201) {
                    Log.d("TAG", "onResponse: " + response.body().getId());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ConsultationModel> call, Throwable t) {
                Toast.makeText(FeedBackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        feeBack_dismiss = findViewById(R.id.feeBack_dismiss);
        feeBack_edt_dialog = findViewById(R.id.feeBack_edt_dialog);
        feeBack_button_submit = findViewById(R.id.feeBack_button_submit);
        feedBack_ratingBar = findViewById(R.id.feedBack_overall_ratingBar);
        feedBack_consult_ratingBar = findViewById(R.id.feedBack_consult_ratingBar);
        feedBack_video_ratingBar = findViewById(R.id.feedBack_video_ratingBar);

        feeBack_dismiss.setOnClickListener(this);
        feeBack_button_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feeBack_dismiss:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                break;

            case R.id.feeBack_button_submit:

                rating1 = (int) feedBack_video_ratingBar.getRating();
                rating2 = (int) feedBack_consult_ratingBar.getRating();
                rating3 = (int) feedBack_ratingBar.getRating();
                feedBackMsg = feeBack_edt_dialog.getText().toString();

                Log.d("Tag", rating1 + rating2 + rating3 + feedBackMsg);

                GetInvoiceNumber();

                break;
        }

    }
}