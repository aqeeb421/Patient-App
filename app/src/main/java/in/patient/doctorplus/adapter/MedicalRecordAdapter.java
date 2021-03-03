package in.patient.doctorplus.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.patient.doctorplus.R;
import in.patient.doctorplus.activity.MedicalRecordActivity;
import in.patient.doctorplus.model.GetMedicalRecordModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MedicalRecordHolder> {
    ArrayList<GetMedicalRecordModel> getMedicalRecordModels;
    private Context context;
    final String[] okFileExtensions = new String[]{
            "jpg",
            "png",
            "gif",
            "jpeg",
            "pdf"
    };

    public MedicalRecordAdapter(ArrayList<GetMedicalRecordModel> getMedicalRecordModels, Context context) {
        this.getMedicalRecordModels = getMedicalRecordModels;
        this.context = context;
    }


    @NonNull
    @Override
    public MedicalRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_record_row, parent, false);
        return new MedicalRecordAdapter.MedicalRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordHolder holder, int position) {

        GetMedicalRecordModel medicalRecord = getMedicalRecordModels.get(position);

        holder.recordType.setText(medicalRecord.getRecordType());
        holder.recordDescription.setText(medicalRecord.getDescription());


        holder.recordFile.setOnClickListener(v -> {

            try {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.show_image_pop, null);

                TextView filenameTextView = dialogView.findViewById(R.id.showImg_filename);
                Button button = dialogView.findViewById(R.id.showImg_btn_sub_dialog);
                ImageView dialog_image_close = dialogView.findViewById(R.id.dialog_sub_image_close);
                ImageView selectedFile = dialogView.findViewById(R.id.selectedImgView);

                builder.setView(dialogView);
                builder.setCancelable(false);
                final androidx.appcompat.app.AlertDialog show = builder.show();

                File uploadFile = new File(medicalRecord.getRecordFiles().toString());
                filenameTextView.setText(uploadFile.getName());
                filenameTextView.setVisibility(View.VISIBLE);

                Picasso.get().load(medicalRecord.getRecordFiles()).fit().into(selectedFile);


                button.setOnClickListener(v1 -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(medicalRecord.getRecordFiles().toString()));
                    context.startActivity(browserIntent);
                });


                dialog_image_close.setOnClickListener(v12 -> show.dismiss());


            } catch (NullPointerException e) {
                Toast.makeText(context, "No File Found", Toast.LENGTH_SHORT).show();
            }

        });
        holder.deleteFile.setOnClickListener(v -> {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
            alertbox.setMessage("Do you want delete this file.");
            alertbox.setTitle("Warning");
            alertbox.setIcon(R.drawable.ic_warning);
            alertbox.setCancelable(true);

            alertbox.setPositiveButton("OK",
                    (arg0, arg1) -> {
                        DeleteDocFile(medicalRecord.getId());
                        arg0.dismiss();
                    });
            alertbox.setNegativeButton("Cancel", (arg0, arg2) -> {
                arg0.dismiss();
            });
            alertbox.show();
        });


    }

    private void DeleteDocFile(Integer id) {
        //progressDialog.show();

        Log.d(TAG, "DeleteDocFile: " + CommonSetting.getToken());
        Call<Void> call = RetrofitClient
                .getInstance()
                .getApi()
                .DeleteMedicalRecordDetails("Token " + CommonSetting.getToken(), id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MedicalRecordActivity.class));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return getMedicalRecordModels.size();
    }

    public class MedicalRecordHolder extends RecyclerView.ViewHolder {

        TextView recordType, recordDescription, recordCreatedBy, recordDate;
        Button recordFile, deleteFile;

        public MedicalRecordHolder(@NonNull View itemView) {
            super(itemView);

            recordType = itemView.findViewById(R.id.recordType);
            recordDescription = itemView.findViewById(R.id.recordDescription);
            //recordCreatedBy = itemView.findViewById(R.id.recordCreatedBy);
            //recordDate = itemView.findViewById(R.id.recordDate);
            recordFile = itemView.findViewById(R.id.recordFile);
            deleteFile = itemView.findViewById(R.id.deleteFile);
        }
    }
}
