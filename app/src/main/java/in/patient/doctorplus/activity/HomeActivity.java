package in.patient.doctorplus.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.SpecialistAdapter;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.SpecialitiesModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BaseActivity;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity.this";
    private RecyclerView recyclerView;
    private SpecialistAdapter specialistAdapter;
    private CircleImageView circleImageView;
    private TextView userName, popularCount_textView;
    private Button ConsultNow_Btn;
    private AlertDialog progressDialog;
    private String retrivedToken;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }


    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrivedToken = CommonSetting.getToken();

        Log.d(retrivedToken, "getUserDetails: " + retrivedToken);
        System.out.print(retrivedToken);

        init();

        GetSpecialist();

        UserValues();

    }

    private void UserValues() {
        Call<GetLoggedInPatientModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetLoggedInPatient("Token " + retrivedToken);

        patientCall.enqueue(new Callback<GetLoggedInPatientModel>() {
            @Override
            public void onResponse(Call<GetLoggedInPatientModel> call, Response<GetLoggedInPatientModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");

                    userName.setText(response.body().getFullName());
                    Picasso.get().load(response.body().getProfilePic()).placeholder(R.drawable.user).into(circleImageView);


                    SharedPreferences sharedPreferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fullName", response.body().getFullName());
                    editor.putString("fullPhoneNumber", response.body().getPhNo());
                    editor.putString("email", response.body().getUser().getEmail());
                    editor.putString("password", response.body().getUser().getPassword());
                    editor.putString("gender", response.body().getGender());
                    editor.putInt("id", response.body().getId());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<GetLoggedInPatientModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    progressDialog.dismiss();

                    Log.d(TAG, "onResponse: " + response.body());


                    List<SpecialitiesModel> specialitiesModel = new ArrayList<>();
                    specialitiesModel = response.body();

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            HomeActivity.this, LinearLayoutManager.HORIZONTAL, false
                    );
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    popularCount_textView.setText(specialitiesModel.size() + " Popular");

                    //initialize adapter
                    specialistAdapter = new SpecialistAdapter((ArrayList<SpecialitiesModel>) specialitiesModel, HomeActivity.this, true);
                    recyclerView.setAdapter(specialistAdapter);


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
        recyclerView = findViewById(R.id.specialist_rv);
        circleImageView = (CircleImageView) findViewById(R.id.home_profile_image);
        ConsultNow_Btn = findViewById(R.id.ConsultNow_Btn);
        userName = findViewById(R.id.userName);
        popularCount_textView = findViewById(R.id.popularCount_textView);

        circleImageView.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            finish();
            Toast.makeText(HomeActivity.this, "bvhjb", Toast.LENGTH_SHORT).show();
        });
        ConsultNow_Btn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, StartConsultActivity.class));
            finish();
        });

        progressDialog = new SpotsDialog(this, R.style.CustomDialog);
    }
}