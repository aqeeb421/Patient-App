package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import in.patient.doctorplus.R;
import in.patient.doctorplus.adapter.MedicalRecordAdapter;
import in.patient.doctorplus.model.GetMedicalRecordModel;
import in.patient.doctorplus.model.PostMedicalRecord;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import in.patient.doctorplus.utils.PathUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecordActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_FOR_AVATAR = 1001;
    private static final String TAG = "MedicalRecordActivity";
    private RecyclerView mediRecord_recyclerView;
    private ImageView noData, selectedFile, img_back;
    private AlertDialog progressDialog;
    private Button add_medical_button;
    private String retrivedToken;
    private File thePath;
    private TextView recordType, recordDiscription, filenameTextView;
    String recordTypeValue, recordDiscriptionValue;
    final String[] okFileExtensions = new String[]{
            "jpg",
            "png",
            "gif",
            "jpeg",
            "pdf"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);
        retrivedToken = CommonSetting.getToken();
        init();
        GetMedicalRecordDetails();
    }

    private void GetMedicalRecordDetails() {
        progressDialog.show();
        Call<List<GetMedicalRecordModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMedicalRecordDetails("Token " + retrivedToken);

        call.enqueue(new Callback<List<GetMedicalRecordModel>>() {
            @Override
            public void onResponse(Call<List<GetMedicalRecordModel>> call, Response<List<GetMedicalRecordModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    ArrayList<GetMedicalRecordModel> getMedicalRecordModels = (ArrayList<GetMedicalRecordModel>) response.body();
                    setRecyclerView(getMedicalRecordModels);
                    noData.setVisibility(View.GONE);
                } else {
                    noData.setVisibility(View.VISIBLE);
                    mediRecord_recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<GetMedicalRecordModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setRecyclerView(ArrayList<GetMedicalRecordModel> getMedicalRecordModels) {
        mediRecord_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MedicalRecordAdapter medicalRecordAdapter = new MedicalRecordAdapter(getMedicalRecordModels, this);
        mediRecord_recyclerView.setAdapter(medicalRecordAdapter);
    }

    private void init() {
        mediRecord_recyclerView = findViewById(R.id.mediRecord_recyclerView);
        noData = findViewById(R.id.noData);
        img_back = findViewById(R.id.img_back);
        add_medical_button = findViewById(R.id.add_medical_button);
        progressDialog = new SpotsDialog(this, R.style.CustomDialog);

        noData.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        });
        img_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        });

        add_medical_button.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getRootView().getContext());
            View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.add_medical_custom_pop, null);
            recordType = dialogView.findViewById(R.id.edt_recordType);
            recordDiscription = dialogView.findViewById(R.id.edt_recordDescription);
            filenameTextView = dialogView.findViewById(R.id.filename);
            Button button = dialogView.findViewById(R.id.btn_sub_dialog);
            Button chooseButton = dialogView.findViewById(R.id.btn_select_dialog);
            ImageView dialog_image_close = dialogView.findViewById(R.id.dialog_sub_image_close);
            selectedFile = dialogView.findViewById(R.id.selectedFile);


            builder.setView(dialogView);
            builder.setCancelable(false);
            final androidx.appcompat.app.AlertDialog show = builder.show();

            chooseButton.setOnClickListener(v1 -> {
                pickFile();
            });

            button.setOnClickListener(v1 -> {

                recordTypeValue = recordType.getText().toString();
                recordDiscriptionValue = recordDiscription.getText().toString();
                if (recordTypeValue.isEmpty()) {
                    recordType.setError("Enter Record Type");
                    return;
                }
               /*if (recordDiscriptionValue.isEmpty()) {
                    recordDiscription.setError("Enter Record Type");
                    return;
                }*/
                UploadMedicalRecord(recordTypeValue, recordDiscriptionValue);
                show.dismiss();
            });


            dialog_image_close.setOnClickListener(v12 -> show.dismiss());

        });
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    private void UploadMedicalRecord(String recordTypeValue, String recordDiscriptionValue) {

        progressDialog.show();
        RequestBody description;
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("User Details", Context.MODE_PRIVATE);
        String created_byValue = preferences.getString("fullName", "User");

        // add another part within the multipart request
        RequestBody record_type =
                RequestBody.create(MediaType.parse("multipart/form-data"), recordTypeValue);

        if (!recordDiscriptionValue.equals("")) {
            description = RequestBody.create(MediaType.parse("multipart/form-data"), recordDiscriptionValue);
        } else {
            description = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        }

        assert created_byValue != null;
        RequestBody created_by =
                RequestBody.create(MediaType.parse("multipart/form-data"), created_byValue);

        File uploadFile = new File(String.valueOf(thePath));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("record_files", uploadFile.getName(), requestFile);

        Call<PostMedicalRecord> call = RetrofitClient
                .getInstance()
                .getApi()
                .UploadMedicalRecord("Token " + retrivedToken, record_type, description, created_by, body);

        call.enqueue(new Callback<PostMedicalRecord>() {
            @Override
            public void onResponse(Call<PostMedicalRecord> call, Response<PostMedicalRecord> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response);
                    finish();
                    startActivity(getIntent());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostMedicalRecord> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            Uri path = Uri.parse(PathUtils.getMediaFilePathForN(uri, getApplicationContext()));
            thePath = new File(Objects.requireNonNull(path.getPath()));
            String url = path.toString();

            try {
                File compressedImageFile = new Compressor(this).compressToFile(new File(path.getPath()));
                Log.e(TAG, "Commpressed image file size : " + compressedImageFile.length());
                enterImageDocuments(Uri.fromFile(compressedImageFile), compressedImageFile);
            } catch (Exception ex) {
                ex.printStackTrace();
                for (String extension : okFileExtensions) {
                    if (Objects.equals(extension, "pdf")) {
                        //do here
                        File uploadFile = new File(String.valueOf(thePath));
                        filenameTextView.setText(uploadFile.getName());
                        filenameTextView.setVisibility(View.VISIBLE);
                    }
                }
            }


        }

    }

    private void enterImageDocuments(Uri fromFile, File compressedImageFile) {
        String imageString = fromFile.toString();
        for (String extesion : okFileExtensions) {
            if (imageString.toLowerCase().endsWith(extesion)) {
                filenameTextView.setVisibility(View.GONE);
                selectedFile.setVisibility(View.VISIBLE);
                Picasso.get().load(imageString).fit().into(selectedFile);
            }
        }
    }
}