package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.SpecialistAdapter;
import in.patient.doctorplus.model.SpecialitiesModel;
import in.patient.doctorplus.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartConsultActivity extends AppCompatActivity {

    private static final String TAG = "StartConsultActivity";
    private ImageView backArrow;
    private RecyclerView recyclerView;
    private Button btnStartConsult;
    private AlertDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_consult);


        init();

        backArrow.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HomeActivity.class)));

        GetSpecialist();
    }

    private void GetSpecialist() {
        progressDialog.show();

        Call<List<SpecialitiesModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetSpecialist();

        call.enqueue(new Callback<List<SpecialitiesModel>>() {
            @Override
            public void onResponse(Call<List<SpecialitiesModel>> call, Response<List<SpecialitiesModel>> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "onResponse: " + response.body());


                    List<SpecialitiesModel> specialitiesModel = new ArrayList<>();
                    specialitiesModel = response.body();

                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    //initialize adapter
                    SpecialistAdapter specialistAdapter = new SpecialistAdapter((ArrayList<SpecialitiesModel>) specialitiesModel, StartConsultActivity.this, false);
                    recyclerView.setAdapter(specialistAdapter);
                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<SpecialitiesModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });


    }

    private void init() {
        backArrow = findViewById(R.id.back_arrow_black);
        recyclerView = findViewById(R.id.recycler_speciality);

        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        findViewById(R.id.btn_start_consulting).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewAppointmentActivity.class);
            startActivity(intent);
            finish();
        });
    }
}