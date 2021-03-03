package in.patient.doctorplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.patient.doctorplus.R;
import in.patient.doctorplus.model.GetMedicalRecordModel;
import in.patient.doctorplus.model.GetPatientPrescriptionModel;
import in.patient.doctorplus.utils.CommonSetting;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionHolder> {

    ArrayList<GetPatientPrescriptionModel> getPatientPrescriptionModels;
    private Context context;

    public PrescriptionAdapter(ArrayList<GetPatientPrescriptionModel> getPatientPrescriptionModels, Context context) {
        this.getPatientPrescriptionModels = getPatientPrescriptionModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PrescriptionAdapter.PrescriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_row, parent, false);
        return new PrescriptionAdapter.PrescriptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionAdapter.PrescriptionHolder holder, int position) {
        holder.Name.setText(getPatientPrescriptionModels.get(position).getDoctor().getFullName());
        holder.speciality.setText(getPatientPrescriptionModels.get(position).getDoctor().getSpecialistType());
        holder.doctorAbout.setText(getPatientPrescriptionModels.get(position).getDoctor().getAbout());
        holder.app_prognosis.setText(getPatientPrescriptionModels.get(position).getRecordType());
        holder.app_Medication.setText(getPatientPrescriptionModels.get(position).getDescription());

        String id = getPatientPrescriptionModels.get(position).getId().toString();

        Picasso.get().load(getPatientPrescriptionModels.get(position).getDoctor().getProfilePic()).fit().into(holder.imageView);
        holder.app_fileView.setOnClickListener(v -> {
            String url = "https://www.doctor-plus.in//patients/single_prescrption/?id="+id;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);

        });


    }

    @Override
    public int getItemCount() {
        return getPatientPrescriptionModels.size();
    }

    public class PrescriptionHolder extends RecyclerView.ViewHolder {

        TextView Name, speciality, doctorAbout, app_prognosis, app_Medication;
        RatingBar ratingBar;
        ImageView app_fileView;
        ImageView imageView;

        public PrescriptionHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.app_doctor_name);
            speciality = itemView.findViewById(R.id.app_doctor_speciality);
            doctorAbout = itemView.findViewById(R.id.app_doctor_about);
            app_prognosis = itemView.findViewById(R.id.app_prognosis);
            app_Medication = itemView.findViewById(R.id.app_Medication);
            ratingBar = itemView.findViewById(R.id.app_ratingBar);
            app_fileView = itemView.findViewById(R.id.app_fileView);
            imageView = itemView.findViewById(R.id.app_doctor_image);
        }
    }
}
