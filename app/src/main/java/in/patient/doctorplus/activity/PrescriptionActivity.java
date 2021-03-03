package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.MedicalRecordAdapter;
import in.patient.doctorplus.adapter.PrescriptionAdapter;
import in.patient.doctorplus.model.GetMedicalRecordModel;
import in.patient.doctorplus.model.GetPatientPrescriptionModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends AppCompatActivity {

    ImageView img_back, noData;
    RecyclerView recyclerView;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);


        img_back = findViewById(R.id.img_back);
        noData = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.prescription_recyclerView);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        GetMedicalRecordDetails(CommonSetting.getToken());
        img_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        });
    }

    private void GetMedicalRecordDetails(String token) {
        progressDialog.show();
        Call<List<GetPatientPrescriptionModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetPatientPrescription("Token " + token);

        call.enqueue(new Callback<List<GetPatientPrescriptionModel>>() {
            @Override
            public void onResponse(Call<List<GetPatientPrescriptionModel>> call, Response<List<GetPatientPrescriptionModel>> response) {
                progressDialog.dismiss();
                if (response.code() == 200 && response.body().size() > 0) {
                    ArrayList<GetPatientPrescriptionModel> getPatientPrescriptionModels = (ArrayList<GetPatientPrescriptionModel>) response.body();
                    setRecyclerView(getPatientPrescriptionModels);
                    noData.setVisibility(View.GONE);
                } else {
                    noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<GetPatientPrescriptionModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setRecyclerView(ArrayList<GetPatientPrescriptionModel> getPatientPrescriptionModels) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter(getPatientPrescriptionModels, this);
        recyclerView.setAdapter(prescriptionAdapter);
    }
}