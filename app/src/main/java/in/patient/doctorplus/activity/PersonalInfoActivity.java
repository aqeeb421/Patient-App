package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.PatientIDModel;
import in.patient.doctorplus.model.PatientModel;
import in.patient.doctorplus.model.PatientRegModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.BottomSheetBloodDialog;
import in.patient.doctorplus.utils.BottomSheetMaterialDialog;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetBloodDialog.BottomSheetListener,
        BottomSheetMaterialDialog.BottomSheetListener1 {

    private static final String TAG = "PersonalInfoActivity";
    private Button male, female, other, btn_continue, btn_continue1;
    private Button addButton1, addButton2, addButton3, addButton4;
    private String gender = "";
    private int age;
    private TextView tvSkip;
    private ImageView img_backArrow, img_backArrow_black;
    private RelativeLayout layout_personal_info_first;
    private LinearLayout layout_personal_info_second;
    private NumberPicker agePicker;
    private EditText edt_BloodGroup, edt_materialStatus, edt_height1, edt_weight;
    private BottomSheetBloodDialog bottomSheet;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String STRING_DEFAULT = "N/A";
    private String pet_id;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        init();
    }

    private void init() {
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);
        btn_continue = findViewById(R.id.btn_continue);
        btn_continue1 = findViewById(R.id.btn_done);
        img_backArrow = findViewById(R.id.back_arrow);
        img_backArrow_black = findViewById(R.id.back_arrow_black);
        tvSkip = findViewById(R.id.tv_skip);
        layout_personal_info_first = findViewById(R.id.layout_personal_info_first);
        layout_personal_info_second = findViewById(R.id.layout_personal_info_second);
        agePicker = findViewById(R.id.numberPicker);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);


        //edit text
        edt_BloodGroup = findViewById(R.id.edt_BloodGroup);
        edt_materialStatus = findViewById(R.id.edt_materialStatus);
        edt_height1 = findViewById(R.id.edt_height1);
        edt_weight = findViewById(R.id.edt_weight);

        //add Buttons
        addButton1 = findViewById(R.id.add_button1);
        addButton2 = findViewById(R.id.add_button2);


        //button init
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        other.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_continue1.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
        img_backArrow_black.setOnClickListener(this);
        img_backArrow.setOnClickListener(this);

        //add Buttons init
        addButton1.setOnClickListener(this);
        addButton2.setOnClickListener(this);

        agePicker.setMinValue(0);
        agePicker.setMaxValue(100);


    }

    @Override
    public void onButtonClicked(String text) {
        edt_BloodGroup.setText(text);
    }

    @Override
    public void onButtonClicked1(String text) {
        edt_materialStatus.setText(text);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.male:
                male.setTextColor(Color.WHITE);
                female.setTextColor(Color.BLACK);
                other.setTextColor(Color.BLACK);
                gender = male.getText().toString();
                break;
            case R.id.female:
                male.setTextColor(Color.BLACK);
                female.setTextColor(Color.WHITE);
                other.setTextColor(Color.BLACK);
                gender = female.getText().toString();
                break;
            case R.id.other:
                male.setTextColor(Color.BLACK);
                female.setTextColor(Color.BLACK);
                other.setTextColor(Color.WHITE);
                gender = other.getText().toString();
                break;

            case R.id.btn_continue:
                layout_personal_info_first.setVisibility(View.GONE);
                layout_personal_info_second.setVisibility(View.VISIBLE);
                Log.d("TAG", "PersonalInfo: " + gender + "  " + agePicker.getValue());

                age = agePicker.getValue();


                sharedPreferences = getSharedPreferences("Personal Details", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("gender", gender);
                editor.putInt("age", age);
                editor.apply();

                break;

            case R.id.btn_done:
                editor.putString("bloodGroup", edt_BloodGroup.getText().toString());
                editor.putString("materialStatus", edt_materialStatus.getText().toString());
                editor.putString("height", edt_height1.getText().toString());
                editor.putString("weight", edt_weight.getText().toString());
                editor.apply();

                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                break;

            case R.id.tv_skip:
                CommonSetting.setSkipping(true);
                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                finish();
                break;

            case R.id.back_arrow:
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
                break;

            case R.id.back_arrow_black:
                layout_personal_info_first.setVisibility(View.VISIBLE);
                layout_personal_info_second.setVisibility(View.GONE);
                break;

            case R.id.add_button1:
                BottomSheetBloodDialog bottomSheet = new BottomSheetBloodDialog();
                bottomSheet.show(getSupportFragmentManager(), "bottomBloodSheet");
                break;
            case R.id.add_button2:
                BottomSheetMaterialDialog bottomSheetMaterialDialog = new BottomSheetMaterialDialog();
                bottomSheetMaterialDialog.show(getSupportFragmentManager(), "bottomMaterialSheet");
                break;

        }
    }


}