package in.patient.doctorplus.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.DoctorsListAdapter;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.model.DoctorModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BaseActivity;
import in.patient.doctorplus.utils.BottomSheetDateDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends BaseActivity implements BottomSheetDateDialog.BottomSheetListener2 {

    private static final String TAG = "NewConsultActivity";
    private RecyclerView rvDoctorsList;
    private String selectedDay;
    private int selectedSpeciality;
    //private ImageView doctorsList_image;
    private AlertDialog progressDialog;
    LinearLayout lnr_empty;
    TextView text_phoneNumber;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_consult;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_new_consult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomSheetDateDialog bottomSheetDateDialog = new BottomSheetDateDialog();
        bottomSheetDateDialog.show(getSupportFragmentManager(), "bottomSheetDateDialog");

        init();

        SharedPreferences sharedPreferences = getSharedPreferences("selected_speciality_item", MODE_PRIVATE);
        selectedSpeciality = sharedPreferences.getInt("selectedSpeciality", 0);
    }

    private void init() {
        rvDoctorsList = findViewById(R.id.doctorsList_recyclerView);
        // doctorsList_image = findViewById(R.id.doctorsList_image);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);
        lnr_empty = findViewById(R.id.lnr_empty);
        text_phoneNumber = findViewById(R.id.text_phoneNumber);

    }

    @Override
    public void onButtonClicked2(String text) {
        selectedDay = text.substring(5).toLowerCase();
        Log.d(TAG, "onButtonClicked2: " + selectedDay);
        SharedPreferences sharedPreferences = getSharedPreferences("selected_speciality_item", MODE_PRIVATE);
        selectedSpeciality = sharedPreferences.getInt("selectedSpeciality", 0);


        GetDoctorsList(selectedSpeciality, selectedDay);

        SharedPreferences preferences = getSharedPreferences("selectedDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedDay", selectedDay);
        editor.apply();

    }

    private void GetDoctorsList(int selectedSpeciality, String selectedDay) {
        progressDialog.show();

        Call<List<DoctorListModel>> listCall = RetrofitClient.getInstance()
                .getApi()
                .GetDoctorsList(selectedSpeciality, selectedDay);


        listCall.enqueue(new Callback<List<DoctorListModel>>() {
            @Override
            public void onResponse(Call<List<DoctorListModel>> call, Response<List<DoctorListModel>> response) {
                if (response.code() == 200) {
                    ArrayList<DoctorListModel> doctorsListModels = (ArrayList<DoctorListModel>) response.body();
                    Log.d(TAG, "onResponse: " + doctorsListModels);

                    if (doctorsListModels.size() != 0) {
                        setRecyclerView(doctorsListModels);
                        rvDoctorsList.setVisibility(View.VISIBLE);

                    } else {
                        // doctorsList_image.setVisibility(View.VISIBLE);
                        lnr_empty.setVisibility(View.VISIBLE);
                        rvDoctorsList.setVisibility(View.GONE);
                        text_phoneNumber.setOnClickListener(v -> {
                            String phone = "08045549495";
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent);
                        });
                    }

                }


                 /* if (rvDoctorsList.getVisibility() == View.GONE){
                      // doctorsList_image.setVisibility(View.VISIBLE);
                      lnr_empty.setVisibility(View.VISIBLE);

                      text_phoneNumber.setOnClickListener(v -> {
                          String phone = "08045549495";
                          Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                          startActivity(intent);
                      });
                  }else
                  {
                      Toast.makeText(NewConsultActivity.this, "error", Toast.LENGTH_SHORT).show();
                  }
*/
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DoctorListModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setRecyclerView(ArrayList<DoctorListModel> doctorsListModels) {
        rvDoctorsList.setLayoutManager(new LinearLayoutManager(NewAppointmentActivity.this));
        DoctorsListAdapter doctorsListAdapter = new DoctorsListAdapter(doctorsListModels, getApplicationContext());
        rvDoctorsList.setAdapter(doctorsListAdapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}