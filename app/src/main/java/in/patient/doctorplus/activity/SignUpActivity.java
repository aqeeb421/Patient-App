package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity.this";
    private EditText editTextEmail, editTextPassword, editTextFullName, editTextPhoneNumber;
    //private CountryCodePicker countryCodePicker;
    private CheckBox cb_confirm_tc;
    private ImageView bck_from_regPage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init() {
        editTextEmail = findViewById(R.id.edt_reg_email);
        editTextPassword = findViewById(R.id.edt_reg_password);
        editTextFullName = findViewById(R.id.edt_fullName);
        editTextPhoneNumber = findViewById(R.id.edt_reg_phone);
        cb_confirm_tc = findViewById(R.id.cb_confirm_tc);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        findViewById(R.id.btn_sign_up_user).setOnClickListener(v -> {
            UserSignUp();
        });
        findViewById(R.id.tv_signIn).setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            finish();
        });
        findViewById(R.id.bck_from_regPage).setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        });
    }

    private void UserSignUp() {

        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (email.isEmpty()) {

        } else {
            // onClick of button perform this simplest code.
            if (email.matches(emailPattern)) {
                Log.d(TAG, "UserSignUp: valid email address");
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                editTextEmail.setError("Enter Valid Email Address");
                editTextEmail.requestFocus();
                return;
            }
        }


        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("First name is required");
            editTextPhoneNumber.requestFocus();
            return;
        }

        String fullPhoneNumber = /*countryCodePicker.getSelectedCountryCodeWithPlus() +*/ phoneNumber;

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 4) {
            editTextPassword.setError("Password should be atleast 4 char long");
            editTextPassword.requestFocus();
            return;
        }

        if (cb_confirm_tc.isChecked()) {

            SendOTP(fullPhoneNumber);

            Log.d(TAG, "UserSignUp: " + fullName + fullPhoneNumber + email + password);
            //Toast.makeText(this, fullName + fullPhoneNumber + email + password, Toast.LENGTH_SHORT).show();

            sharedPreferences = getSharedPreferences("SignUp Details", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("fullName", fullName);
            editor.putString("fullPhoneNumber", phoneNumber);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();

        } else {

            @SuppressLint("ResourceAsColor") Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Please check Terms and Condition box...", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.red));
            snackbar.show();
        }


    }

    private void SendOTP(String fullPhoneNumber) {
        progressDialog.show();
        String otpText = "is your verification code for Doctor Plus";
        Call<String> sendOtp = RetrofitClient
                .getInstance()
                .getApi()
                .getSMSOTP(
                        "a224db72-cafb-4cce-93ab-3d7f950c92e2",
                        "Welcome <OTP> " + otpText,
                        "0",
                        "BANDSS",
                        fullPhoneNumber,
                        "1",
                        4
                );

        sendOtp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}