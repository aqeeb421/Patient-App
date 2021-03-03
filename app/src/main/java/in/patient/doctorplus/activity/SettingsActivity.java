package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.PatientUpdateInfoModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import in.patient.doctorplus.utils.PathUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private static final int PICK_PHOTO_FOR_AVATAR = 1234;
    private ImageView settings_back;
    private TextView saveChanges;
    private String retrivedToken;
    private CircleImageView edit_profileImage, profile_image;
    final String[] okFileExtensions = new String[]{
            "jpg",
            "png",
            "gif",
            "jpeg"
    };
    private File thePath;

    private TextInputEditText edit_FullName, edit_Email, edit_PhoneNumber, edit_age, edit_height, edit_weight, edit_materialStatus;
    private TextInputEditText edit_Gender, edit_BloodGroup;

    private String fullName, email, fullPhoneNumber, gender, age, height1, weight, bloodGroup, materialStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        retrivedToken = CommonSetting.getToken();

        Log.d(retrivedToken, "getUserDetails: " + retrivedToken);
        System.out.print(retrivedToken);

        init();

        setValues();
    }

    private void setValues() {
        Call<GetLoggedInPatientModel> patientCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetLoggedInPatient("Token " + retrivedToken);

        patientCall.enqueue(new Callback<GetLoggedInPatientModel>() {
            @Override
            public void onResponse(Call<GetLoggedInPatientModel> call, Response<GetLoggedInPatientModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");

                    fullName = response.body().getFullName();
                    email = response.body().getUser().getEmail();
                    fullPhoneNumber = response.body().getUser().getUsername();
                    if (gender != null)
                        gender = response.body().getGender();
                    else
                        gender = "Not Specified";

                    age = String.valueOf(response.body().getAge());
                    height1 = String.valueOf(response.body().getHeight());
                    weight = String.valueOf(response.body().getWeight());
                    materialStatus = response.body().getMaritalStatus();
                    bloodGroup = response.body().getBloodGroup();

                    if (response.body().getProfilePic() == null) {
                        Picasso.get().load(R.drawable.user).fit().into(profile_image);
                    } else {
                        Picasso.get().load(response.body().getProfilePic()).fit().into(profile_image);

                    }

                    edit_FullName.setText(fullName);
                    edit_Email.setText(email);
                    edit_PhoneNumber.setText(fullPhoneNumber);
                    edit_Gender.setText(gender);
                    edit_age.setText(age);
                    edit_height.setText(height1);
                    edit_weight.setText(weight);
                    edit_materialStatus.setText(materialStatus);
                    edit_BloodGroup.setText(bloodGroup);
                }
            }

            @Override
            public void onFailure(Call<GetLoggedInPatientModel> call, Throwable t) {

                Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        Log.d("TAG", "setValues: ");

    }

    private void init() {
        settings_back = findViewById(R.id.settings_back);
        saveChanges = findViewById(R.id.saveChanges);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        // Get a reference to the AutoCompleteTextView in the layout
        edit_BloodGroup = findViewById(R.id.edit_BloodGroup);
        edit_Gender = findViewById(R.id.edit_Gender);

        // Get the string array
        String[] bloodGroups = getResources().getStringArray(R.array.blood_group);
        String[] genders = getResources().getStringArray(R.array.gender);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> bloodGroupAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bloodGroups);
        ArrayAdapter<String> gendersAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genders);
      /*  edit_BloodGroup.setThreshold(1);
        edit_Gender.setThreshold(1);
        edit_BloodGroup.setAdapter(bloodGroupAdapter);
        edit_Gender.setAdapter(gendersAdapter);
*/
        edit_FullName = findViewById(R.id.edit_FullName);
        edit_Email = findViewById(R.id.edit_Email);
        edit_PhoneNumber = findViewById(R.id.edit_PhoneNumber);
        edit_age = findViewById(R.id.edit_age);
        edit_height = findViewById(R.id.edit_height);
        edit_weight = findViewById(R.id.edit_weight);
        edit_materialStatus = findViewById(R.id.edit_materialStatus);
        edit_profileImage = findViewById(R.id.edit_profileImage);
        profile_image = findViewById(R.id.profile_image);

        edit_profileImage.setOnClickListener(v -> {
            pickImage();
        });

        settings_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        });

        saveChanges.setOnClickListener(v -> {

            SendPatientUpdateInfo();
        });
    }

    private AlertDialog progressDialog;

    private void SendPatientUpdateInfo() {
        progressDialog.show();
        fullPhoneNumber = edit_PhoneNumber.getText().toString();
        email = edit_Email.getText().toString();
        fullName = edit_FullName.getText().toString();
        age = edit_age.getText().toString();
        height1 = edit_height.getText().toString();
        weight = edit_weight.getText().toString();
        materialStatus = edit_materialStatus.getText().toString();
        bloodGroup = edit_BloodGroup.getText().toString();

        // add another part within the multipart request
        RequestBody fullName1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), fullName);
        RequestBody fullPhoneNumber1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), fullPhoneNumber);
        RequestBody email1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody gender1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), gender);
        RequestBody age1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), age);
        RequestBody height11 =
                RequestBody.create(MediaType.parse("multipart/form-data"), height1);
        RequestBody weight1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody materialStatus1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), materialStatus);
        RequestBody bloodGroup1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), bloodGroup);
        RequestBody loggedInUser =
                RequestBody.create(MediaType.parse("multipart/form-data"), "1");

        File imgFile = new File(String.valueOf(thePath));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);


        Call<PatientUpdateInfoModel> observable = RetrofitClient
                .getInstance()
                .getApi()
                .SendPatientUpdateInfo("Token " + retrivedToken, body, fullName1, fullPhoneNumber1, fullPhoneNumber1, email1, bloodGroup1, age1, weight1, gender1, height11);
        observable.enqueue(new Callback<PatientUpdateInfoModel>() {
            @Override
            public void onResponse(Call<PatientUpdateInfoModel> call, Response<PatientUpdateInfoModel> response) {
                progressDialog.hide();
                if (response.isSuccessful()) {

                    CommonSetting.setToken(response.body().getToken());

                    Toast.makeText(SettingsActivity.this, "Profile Changes Saved.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PatientUpdateInfoModel> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
    }


    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            Uri path = Uri.parse(PathUtils.getMediaFilePathForN(uri, getApplicationContext()));
            thePath = new File(path.getPath());
            String url = path.toString();

            try {
                File compressedImageFile = new Compressor(this).compressToFile(new File(path.getPath()));
                Log.e(TAG, "Commpressed image file size : " + compressedImageFile.length());
                enterImageDocuments(Uri.fromFile(compressedImageFile), compressedImageFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

    }

    private void enterImageDocuments(Uri fromFile, File compressedImageFile) {
        String imageString = fromFile.toString();
        for (String extesion : okFileExtensions) {
            if (imageString.toLowerCase().endsWith(extesion)) {
                Picasso.get().load(imageString).fit().placeholder(R.drawable.user).into(profile_image);
            }
        }
    }
}