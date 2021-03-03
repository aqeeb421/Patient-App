package in.patient.doctorplus.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import in.patient.doctorplus.R;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.PatientModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.patient.doctorplus.activity.LogInActivity.retrivedToken;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3;
    private static final int PERMISSIONS_CODE = 200;
    private AlertDialog progressDialog;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        //Retrieve token wherever necessary
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("LogIn Details", Context.MODE_PRIVATE);
        retrivedToken = preferences.getString("token", "null");
        CommonSetting.setToken(retrivedToken);

        if (retrivedToken.equals("null")) {
            //Retrieve token wherever necessary
            preferences = getApplicationContext().getSharedPreferences("SignUp Token", Context.MODE_PRIVATE);
            retrivedToken = preferences.getString("token", "null");//second parameter default value.
            CommonSetting.setToken(retrivedToken);
        }


        UserValues("Token " + retrivedToken);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                getUserDetails();

            }
        }, SPLASH_DISPLAY_LENGTH * 1000);
    }

    private void UserValues(String token) {
        Call<GetLoggedInPatientModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetLoggedInPatient(token);

        patientCall.enqueue(new Callback<GetLoggedInPatientModel>() {
            @Override
            public void onResponse(Call<GetLoggedInPatientModel> call, Response<GetLoggedInPatientModel> response) {
                if (response.isSuccessful()) {
                    CommonSetting.setEmail(response.body().getUser().getEmail());
                    CommonSetting.setPhoneNumber(response.body().getPhNo());
                }
            }

            @Override
            public void onFailure(Call<GetLoggedInPatientModel> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetails() {
        //   Log.d(retrivedToken, "getUserDetails: " + retrivedToken);
        //   System.out.print(retrivedToken);

        Call<PatientModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetLoginDetails("Token " + retrivedToken);


        Log.d("TAG", "getUserDetails: " + retrivedToken);

        call.enqueue(new Callback<PatientModel>() {
            @Override
            public void onResponse(Call<PatientModel> call, Response<PatientModel> response) {
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: " + response.message());
                    int id = response.body().getId();
                    launchHomeScreen();
                    Log.d("TAG", "onResponse: " + response.body());
                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<PatientModel> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}