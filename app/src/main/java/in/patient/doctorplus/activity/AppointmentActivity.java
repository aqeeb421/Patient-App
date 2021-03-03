package in.patient.doctorplus.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.AppointmentsAdapter;
import in.patient.doctorplus.model.GetAppointmentModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BaseActivity;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends BaseActivity {

    private static final String TAG = "AppointmentActivity";
    private RecyclerView recyclerView;
    private ImageView noData;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        GetAppointmentDetails();
    }

    private void init() {
        recyclerView = findViewById(R.id.appointment_recyclerView);
        noData = findViewById(R.id.noData);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);
    }

    private void GetAppointmentDetails() {
        progressDialog.show();
        String retrivedToken = CommonSetting.getToken();

        Call<List<GetAppointmentModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetAppointmentDetails("Token " + retrivedToken);

        call.enqueue(new Callback<List<GetAppointmentModel>>() {
            @Override
            public void onResponse(Call<List<GetAppointmentModel>> call, Response<List<GetAppointmentModel>> response) {
                if (response.code() == 200 && Objects.requireNonNull(response.body()).size() > 0) {
                    try {
                        int id = response.body().get(0).getId();
                        Log.d("TAG", "onResponse: " + id);
                        ArrayList<GetAppointmentModel> getAppointmentModel = (ArrayList<GetAppointmentModel>) response.body();
                        setRecyclerView(getAppointmentModel);
                    } catch (IndexOutOfBoundsException error) {
                        // Output expected IndexOutOfBoundsExceptions.
                        noData.setVisibility(View.VISIBLE);
                        Log.d("TAG", "onResponse: " + error);
                    }

                } else {
                    Toast.makeText(AppointmentActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    noData.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<GetAppointmentModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setRecyclerView(ArrayList<GetAppointmentModel> getAppointmentModel) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getAppointmentModel, this);
        recyclerView.setAdapter(appointmentsAdapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_appointment;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_appointment;
    }
}