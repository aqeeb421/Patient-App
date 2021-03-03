package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import java.util.Random;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.PatientIDModel;
import in.patient.doctorplus.model.PatientModel;
import in.patient.doctorplus.model.PatientRegModel;
import in.patient.doctorplus.model.VerifyOTPModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerifictionActivity";
    private Button verify_otp, btn_final_verified;
    private LinearLayout lnr_otp_verify_page, lnr_verified_page;
    private ImageView img_verified;
    private TextView tv_verified, tv_verified_sub, txt_ph;
    private static final String STRING_DEFAULT = "N/A";
    private static final int INT_DEFAULT = -1;
    private SharedPreferences sharedPreferences;
    private String pet_id;
    String fullPhoneNumber, fullName, email, password;
    PinView otpText;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        init();

        GeneratePatientID();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        img_verified.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_top);
        tv_verified.startAnimation(animation1);
        tv_verified_sub.startAnimation(animation1);
        btn_final_verified.startAnimation(animation1);

    }

    private void GeneratePatientID() {
        progressDialog.show();

        Call<PatientIDModel> patientIDModelCall = RetrofitClient
                .getInstance()
                .getApi()
                .GeneratePatientID();

        patientIDModelCall.enqueue(new Callback<PatientIDModel>() {
            @Override
            public void onResponse(Call<PatientIDModel> call, Response<PatientIDModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    Log.d("TAG", "onResponse: " + response.body().getPatId());
                    pet_id = response.body().getPatId();
                } else {
                    Toast.makeText(getApplicationContext(), "Error with api...", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PatientIDModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        verify_otp = findViewById(R.id.btn_otp_verify);
        lnr_otp_verify_page = findViewById(R.id.lnr_otp_verify_page);
        lnr_verified_page = findViewById(R.id.lnr_verified_page);
        btn_final_verified = findViewById(R.id.btn_final_verified);
        img_verified = findViewById(R.id.img_verified);
        tv_verified = findViewById(R.id.tv_verified);
        tv_verified_sub = findViewById(R.id.tv_verified_sub);
        txt_ph = findViewById(R.id.txt_ph);
        otpText = findViewById(R.id.otpText);

        sharedPreferences = getSharedPreferences("SignUp Details", Context.MODE_PRIVATE);
        fullName = sharedPreferences.getString("fullName", STRING_DEFAULT);
        fullPhoneNumber = sharedPreferences.getString("fullPhoneNumber", STRING_DEFAULT);
        email = sharedPreferences.getString("email", STRING_DEFAULT);
        password = sharedPreferences.getString("password", STRING_DEFAULT);
        txt_ph.setText(fullPhoneNumber);

        progressDialog = new SpotsDialog(this, R.style.CustomDialog);


        verify_otp.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + otpText.getText().toString().trim());
            String otp = otpText.getText().toString().trim();
            VerifyOTP(fullPhoneNumber, otp);

        });

        btn_final_verified.setOnClickListener(v -> {
            Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


    private void VerifyOTP(String fullPhoneNumber, String otp) {
        progressDialog.show();
        Call<VerifyOTPModel> verifyOTP = RetrofitClient
                .getInstance()
                .getApi()
                .VerifyOTP(fullPhoneNumber, otp);

        verifyOTP.enqueue(new Callback<VerifyOTPModel>() {
            @Override
            public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                if (response.body().getResponseCode().equals("8000")) {
                    Toast.makeText(VerificationActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                    if (CommonSetting.getSkipping()) {
                        Skipped();
                    } else {
                        NotSkipped();
                    }
                } else if (response.body().getResponseCode().equals("8003")) {
                    Toast.makeText(VerificationActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponseCode().equals("8006")) {
                    Toast.makeText(VerificationActivity.this, "OTP Verification failed please", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpText.getWindowToken(), 0);
                if (CommonSetting.getSkipping()) {
                    Skipped();
                } else {
                    NotSkipped();
                }

            }

            @Override
            public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void NotSkipped() {
        sharedPreferences = getSharedPreferences("Personal Details", Context.MODE_PRIVATE);
        String gender = sharedPreferences.getString("gender", STRING_DEFAULT);
        String bloodGroup = sharedPreferences.getString("bloodGroup", STRING_DEFAULT);
        String materialStatus = sharedPreferences.getString("materialStatus", STRING_DEFAULT);
        int age = sharedPreferences.getInt("age", 0);
        double height = Double.parseDouble(sharedPreferences.getString("height", STRING_DEFAULT));
        double weight = Double.parseDouble(sharedPreferences.getString("weight", STRING_DEFAULT));

        float height1 = 0;
        int weight1 = 0;

        if (TextUtils.isEmpty(email)) {
            email = "default";
        }
        if (TextUtils.isEmpty(materialStatus)) {
            materialStatus = "default";
        }
        if (TextUtils.isEmpty(bloodGroup)) {
            bloodGroup = "default";
        }
        if (TextUtils.isEmpty(gender)) {
            gender = "default";
        }

        Log.d(TAG, "onClick: " + height1 + weight1);

        progressDialog.show();

        Call<PatientRegModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .SendRegisterUser(fullPhoneNumber, password, email, pet_id, fullName, gender, age, height, weight, materialStatus, bloodGroup, fullPhoneNumber);

        patientCall.enqueue(new Callback<PatientRegModel>() {
            @Override
            public void onResponse(Call<PatientRegModel> call, Response<PatientRegModel> response) {
                if (response.code() == 200) {
                    PatientModel patient = response.body().getPatient();
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: " + patient.getUsername());

                    lnr_otp_verify_page.setVisibility(View.GONE);
                    lnr_verified_page.setVisibility(View.VISIBLE);

                    String token = response.body().getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences("SignUp Token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.putString("from", "signup");
                    editor.apply();

                    CommonSetting.setToken(token);
                    UserValues("Token " + token);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(VerificationActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PatientRegModel> call, Throwable t) {
                Toast.makeText(VerificationActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

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
                Toast.makeText(VerificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Skipped() {
        sharedPreferences = getSharedPreferences("SignUp Details", Context.MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", STRING_DEFAULT);
        String fullPhoneNumber = sharedPreferences.getString("fullPhoneNumber", STRING_DEFAULT);
        String email = sharedPreferences.getString("email", STRING_DEFAULT);
        String password = sharedPreferences.getString("password", STRING_DEFAULT);

        if (TextUtils.isEmpty(email)) {
            email = "default";
        }

        String materialStatus = "default";
        String bloodGroup = "default";
        String gender = "default";
        int age = 0;
        double height = 0.0;
        double weight = 0.0;


        progressDialog.show();

        Call<PatientRegModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .SendRegisterUser(fullPhoneNumber, password, email, pet_id, fullName, gender, age, height, weight, materialStatus, bloodGroup, fullPhoneNumber);

        patientCall.enqueue(new Callback<PatientRegModel>() {
            @Override
            public void onResponse(Call<PatientRegModel> call, Response<PatientRegModel> response) {
                if (response.code() == 200) {

                    PatientModel patient = response.body().getPatient();

                    progressDialog.dismiss();
                    lnr_otp_verify_page.setVisibility(View.GONE);
                    lnr_verified_page.setVisibility(View.VISIBLE);

                    String token = response.body().getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences("SignUp Token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.putString("from", "signUp");
                    editor.apply();

                    CommonSetting.setToken(token);

                } else{
                    progressDialog.dismiss();
                    Toast.makeText(VerificationActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PatientRegModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(VerificationActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}