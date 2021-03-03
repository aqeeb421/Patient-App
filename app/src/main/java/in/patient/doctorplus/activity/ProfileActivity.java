package in.patient.doctorplus.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.PasswordChange;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BaseActivity;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private TextView editProfile, tvProfileName, tvMedicalRecording, tvProfileEmail;
    private TextView profilePrescription, profileHelpCenter, profileTC, profilePrivacy, logout, editPassword;
    private SharedPreferences pref;
    private CircleImageView profile_image1;
    private String retrivedToken;
    private android.app.AlertDialog progressDialog;
    private String TAG = "ProfileActivity";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrivedToken = CommonSetting.getToken();
        Log.d(TAG, "onCreate: " + retrivedToken);


        init();

        UserValues();

        tvMedicalRecording.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), MedicalRecordActivity.class));

        });
    }

    private void UserValues() {
        Call<GetLoggedInPatientModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetLoggedInPatient("Token " + retrivedToken);
        progressDialog.show();
        patientCall.enqueue(new Callback<GetLoggedInPatientModel>() {
            @Override
            public void onResponse(Call<GetLoggedInPatientModel> call, Response<GetLoggedInPatientModel> response) {
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: " + response);

                    tvProfileName.setText(response.body().getFullName());
                    tvProfileEmail.setText(response.body().getUser().getEmail());

                    Picasso.get().load(response.body().getProfilePic()).placeholder(R.drawable.user).error(R.drawable.user).into(profile_image1);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetLoggedInPatientModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void init() {
        editProfile = findViewById(R.id.editProfile);
        profile_image1 = findViewById(R.id.profile_image1);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvMedicalRecording = findViewById(R.id.profileMedicalRecording);
        profilePrescription = findViewById(R.id.my_prescription);
        logout = findViewById(R.id.logout);
        profilePrivacy = findViewById(R.id.profilePrivacy);
        profileTC = findViewById(R.id.profileTC);
        // profileHelpCenter = findViewById(R.id.profileHelpCenter);
        editPassword = findViewById(R.id.editPassword);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        editPassword.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_password_pop, null);
            EditText edt_old_password = dialogView.findViewById(R.id.edt_old_password);
            EditText edt_new_password = dialogView.findViewById(R.id.edt_new_password);
            EditText edt_confirm_password = dialogView.findViewById(R.id.edt_confirm_password);
            Button button = dialogView.findViewById(R.id.btn_dialog);
            ImageView dialog_image_close = dialogView.findViewById(R.id.dialog_image_close);


            button.setOnClickListener(v1 -> {
                String old_password = edt_old_password.getText().toString();
                String new_password = edt_new_password.getText().toString();
                String confirm_password = edt_confirm_password.getText().toString();
                if (!TextUtils.isEmpty(old_password)) {
                    if (!TextUtils.isEmpty(new_password)) {
                        if (!TextUtils.isEmpty(confirm_password)) {
                            if (new_password.equals(confirm_password)) {
                                SendChangePassword(old_password, new_password, confirm_password);
                            } else
                                edt_new_password.setError("Password mismatched");
                        } else
                            edt_confirm_password.setError("Enter Confirm Password");
                    } else
                        edt_new_password.setError("Enter New Password");
                } else
                    edt_old_password.setError("Enter Old Password");

            });
            builder.setView(dialogView);
            builder.setCancelable(false);
            final AlertDialog show = builder.show();


            dialog_image_close.setOnClickListener(v12 -> show.dismiss());
        });

        tvMedicalRecording.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MedicalRecordActivity.class));
        });
        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        });

        profilePrescription.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PrescriptionActivity.class));
        });

        logout.setOnClickListener(v -> {

            android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(v.getRootView().getContext());
            alertbox.setMessage("Do you want logout?");
            alertbox.setTitle("Warning");
            alertbox.setIcon(R.drawable.ic_warning);
            alertbox.setCancelable(true);

            alertbox.setPositiveButton("OK",
                    (arg0, arg1) -> {
                        progressDialog.show();
                        Call<Void> patientCall = RetrofitClient
                                .getInstance()
                                .getApi()
                                .PatientLogout("Token " + retrivedToken);

                        patientCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "user logged out...", Toast.LENGTH_SHORT).show();
                                CommonSetting.setToken("");

                                sharedPreferences = getSharedPreferences("SignUp Token", Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.apply();

                                sharedPreferences = getSharedPreferences("LogIn Details", Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.apply();

                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
                        arg0.dismiss();
                    });
            alertbox.setNegativeButton("Cancel", (arg0, arg2) -> {
                arg0.dismiss();
            });
            alertbox.show();


        });
        profilePrivacy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.doctor-plus.in/privacypolicy"));
            startActivity(browserIntent);
        });
        profileTC.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.doctor-plus.in/termsandcondition"));
            startActivity(browserIntent);
        });

    }


    private void SendChangePassword(String old_password, String new_password, String confirm_password) {
        progressDialog.show();

        String retrivedToken = CommonSetting.getToken();//second parameter default value.

        Call<PasswordChange> appointmentModelCall = RetrofitClient.getInstance()
                .getApi()
                .SendChangePassword("Token " + retrivedToken, old_password, new_password, confirm_password);

        appointmentModelCall.enqueue(new Callback<PasswordChange>() {
            @Override
            public void onResponse(Call<PasswordChange> call, Response<PasswordChange> response) {

                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: " + response.body().getDone());
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    Toast.makeText(ProfileActivity.this, "Password Updated Successfully...", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<PasswordChange> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}