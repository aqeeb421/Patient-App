package in.patient.doctorplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.patient.doctorplus.R;
import in.patient.doctorplus.activity.AppointmentSummaryActivity;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.model.DoctorModel;
import in.patient.doctorplus.utils.CommonSetting;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.DoctorsListHolder> {
    ArrayList<DoctorListModel> doctorsListModels;
    private Context context;

    public DoctorsListAdapter(ArrayList<DoctorListModel> doctorsListModels, Context context) {
        this.doctorsListModels = doctorsListModels;
        this.context = context;
    }


    @NonNull
    @Override
    public DoctorsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_row, parent, false);
        return new DoctorsListHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DoctorsListHolder holder, int position) {

        String feeAmount = null;


        String comm = (doctorsListModels.get(position).getDoctor().getCommissionVal());
        if (doctorsListModels.get(position).getDoctor().getCommissionType().equalsIgnoreCase("percent")) {
            double percentage = Double.parseDouble(comm);
            String fee = (doctorsListModels.get(position).getDoctor().getConsultationFee());
            double amount = Double.parseDouble(fee);

            double res = (amount / 100.0f) * percentage;

            feeAmount = String.valueOf(res + amount);

        } else {
            String fee = (doctorsListModels.get(position).getDoctor().getConsultationFee());
            double percentage = Double.parseDouble(comm);
            double amount = Double.parseDouble(fee);

            double res = amount + percentage;

            feeAmount = String.valueOf(res);

        }

        String avl_time = doctorsListModels.get(position).getFromTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputTime = new SimpleDateFormat("hh:mm a");
        Date d1 = null;
        try {
            d1 = sdf1.parse(avl_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = outputTime.format(d1);


        holder.Name.setText(doctorsListModels.get(position).getDoctor().getFullName());
        holder.consultationFee.setText("Rs. " + feeAmount);
        holder.fromTime.setText(formattedTime);
        holder.ratingBar.setRating(doctorsListModels.get(position).getDoctor().getRating());
        Picasso.get().load(doctorsListModels.get(position).getDoctor().getProfilePic()).fit().into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.cd_row_doctor_progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        String finalFeeAmount = feeAmount;
        holder.bookAppointment.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_pop, null);
            TextView textView = dialogView.findViewById(R.id.tv_dialog);
            EditText editText = dialogView.findViewById(R.id.edt_dialog);
            Button button = dialogView.findViewById(R.id.btn_dialog);
            Button button_skip = dialogView.findViewById(R.id.btn_dialog_skip);
            ImageView dialog_image_close = dialogView.findViewById(R.id.dialog_image_close);
            CircleImageView imageView = dialogView.findViewById(R.id.dialog_image);

            Picasso.get().load(doctorsListModels.get(position).getDoctor().getProfilePic()).fit().into(imageView);

            // working method check once
            int docID = doctorsListModels.get(position).getId();

            button.setOnClickListener(v1 -> {
                String consultationMsg = editText.getText().toString();
                if (!TextUtils.isEmpty(consultationMsg)) {
                    CommonSetting.setConsultationAbout(consultationMsg);
                    Intent intent = new Intent(context, AppointmentSummaryActivity.class);
                    intent.putExtra("consultationMsg", consultationMsg);
                    intent.putExtra("finalFeeAmount", finalFeeAmount);
                    intent.putExtra("id", position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            });
            button_skip.setOnClickListener(v1 -> {
                CommonSetting.setConsultationAbout("Not Specified");
                Intent intent = new Intent(context, AppointmentSummaryActivity.class);
                intent.putExtra("finalFeeAmount", finalFeeAmount);
                intent.putExtra("consultationMsg", "Not Specified");
                intent.putExtra("id", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

            builder.setView(dialogView);
            builder.setCancelable(false);
            final AlertDialog show = builder.show();


            dialog_image_close.setOnClickListener(v12 -> show.dismiss());

        });

    }

    @Override
    public int getItemCount() {
        return doctorsListModels.size();
    }


    public class DoctorsListHolder extends RecyclerView.ViewHolder {

        TextView Name, consultationFee, fromTime;
        RatingBar ratingBar;
        Button bookAppointment;
        ImageView imageView;
        ProgressBar cd_row_doctor_progress;

        public DoctorsListHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.cd_row_doctor_name);
            consultationFee = itemView.findViewById(R.id.cd_row_fee_amount);
            fromTime = itemView.findViewById(R.id.cd_row_from_time);
            ratingBar = itemView.findViewById(R.id.cd_row_ratingBar);
            bookAppointment = itemView.findViewById(R.id.cd_row_btn);
            imageView = itemView.findViewById(R.id.cd_row_doctor_image);
            cd_row_doctor_progress = itemView.findViewById(R.id.cd_row_doctor_progress);

        }
    }
}
