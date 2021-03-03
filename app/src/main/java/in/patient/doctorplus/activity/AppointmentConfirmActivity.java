package in.patient.doctorplus.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.AppointmentModel;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentConfirmActivity extends AppCompatActivity {

    private static final String TAG = "Appointment";
    private TextView appointConfirm_doctorName, appointConfirm_about, appointConfirm_time, appointConfirm_date, appointConfirm_consultAbout;
    private Button appointConfirm_button_confirm;
    private ImageView appointConfirm_img;
    private int SelectedDoctorId;
    private String strMsg;
    private AlertDialog progressDialog;
    private String selectedDay;
    private int selectedSpeciality;
    String fullPhoneNumber, fullName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirm);

        Intent intent = getIntent();
        strMsg = intent.getStringExtra("consultationMsg");
        SelectedDoctorId = intent.getIntExtra("id", 0);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("User Details", Context.MODE_PRIVATE);
        fullPhoneNumber = preferences.getString("fullPhoneNumber", "STRING_DEFAULT");
        fullName = preferences.getString("fullName", "STRING_DEFAULT");


        init();

        SharedPreferences sharedPreferences = getSharedPreferences("selected_speciality_item", MODE_PRIVATE);
        selectedSpeciality = sharedPreferences.getInt("selectedSpeciality", 0);

        sharedPreferences = getSharedPreferences("selectedDay", MODE_PRIVATE);
        selectedDay = sharedPreferences.getString("selectedDay", "null");

        GetDoctorsList(selectedSpeciality, selectedDay, SelectedDoctorId);
    }


    private void init() {
        appointConfirm_doctorName = findViewById(R.id.appointConfirm_doctorName);
        appointConfirm_about = findViewById(R.id.appointConfirm_about);
        appointConfirm_date = findViewById(R.id.appointConfirm_date);
        appointConfirm_time = findViewById(R.id.appointConfirm_time);
        appointConfirm_consultAbout = findViewById(R.id.appointConfirm_consultAbout);
        appointConfirm_button_confirm = findViewById(R.id.appointConfirm_button_confirm);
        appointConfirm_img = findViewById(R.id.appointConfirm_img);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        appointConfirm_button_confirm.setOnClickListener(v -> {

            SendPatientMsg();
            SendDoctorMsg();

            startActivity(new Intent(AppointmentConfirmActivity.this, AppointmentActivity.class));
            finish();
        });


    }

    private void SendPatientMsg() {

        String text = "Your Appointment with " + appointConfirm_doctorName.getText() + " has been fixed on " + appointConfirm_date.getText() + " at " + appointConfirm_time.getText() + " am to keep track of your appointments visit https://doctor-plus.in/patients/appointments";

        Call<String> sendOtp = RetrofitClient
                .getInstance()
                .getApi()
                .getSMS(
                        "a224db72-cafb-4cce-93ab-3d7f950c92e2",
                        text,
                        "0",
                        "BANDSS",
                        fullPhoneNumber
                );

        sendOtp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void SendDoctorMsg() {

        String text = "Your Appointment with " + fullName + " has been fixed on " + appointConfirm_date.getText() + " at " + appointConfirm_time.getText() + " to keep track of your appointments visit https://doctor-plus.in/patients/appointments";


        Call<String> sendOtp = RetrofitClient
                .getInstance()
                .getApi()
                .getSMS(
                        "a224db72-cafb-4cce-93ab-3d7f950c92e2",
                        text,
                        "0",
                        "BANDSS",
                        fullPhoneNumber
                );

        sendOtp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void GetDoctorsList(int selectedSpeciality, String selectedDay, int ID) {
        progressDialog.show();
        Call<List<DoctorListModel>> listCall = RetrofitClient.getInstance()
                .getApi()
                .GetDoctorsList(selectedSpeciality, selectedDay);


        listCall.enqueue(new Callback<List<DoctorListModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<DoctorListModel>> call, Response<List<DoctorListModel>> response) {
                if (response.code() == 200) {
                    List<DoctorListModel> doctorsListModels = response.body();
                    assert response.body() != null;
                    appointConfirm_doctorName.setText("Dr. " + response.body().get(ID).getDoctor().getFullName());
                    appointConfirm_about.setText(response.body().get(ID).getDoctor().getSpecialistType());
                    String avl_time = response.body().get(ID).getFromTime();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat outputTime = new SimpleDateFormat("hh:mm a");
                    Date d1 = null;
                    try {
                        d1 = sdf1.parse(avl_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String apt_time = outputTime.format(d1);
                    appointConfirm_date.setText(selectedDay);
                    appointConfirm_time.setText(apt_time);
                    appointConfirm_consultAbout.setText(strMsg);
                    Picasso.get().load(doctorsListModels.get(ID).getDoctor().getProfilePic()).fit().into(appointConfirm_img);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DoctorListModel>> call, Throwable t) {

                progressDialog.dismiss();
            }
        });
    }


}