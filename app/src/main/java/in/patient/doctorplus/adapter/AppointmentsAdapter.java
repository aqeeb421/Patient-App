package in.patient.doctorplus.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import in.patient.doctorplus.R;
import in.patient.doctorplus.activity.AppointmentActivity;
import in.patient.doctorplus.activity.AppointmentSummaryActivity;
import in.patient.doctorplus.activity.VideoCallActivity;
import in.patient.doctorplus.model.AppointmentModel;
import in.patient.doctorplus.model.CancelAppModel;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.model.GetAppointmentModel;
import in.patient.doctorplus.model.RescheduleAppModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentsHolder> {

    ArrayList<GetAppointmentModel> getAppointmentModels;
    private Context context;
    private String SelectedDay = "";
    private TextView tv_avail_doc;
    private int flag = 0;
    private String formattedTime = "";

    public AppointmentsAdapter(ArrayList<GetAppointmentModel> getAppointmentModel, AppointmentActivity appointmentActivity) {

        this.getAppointmentModels = getAppointmentModel;
        this.context = appointmentActivity;

    }


    @NonNull
    @Override
    public AppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointed_doctors_row, parent, false);
        return new AppointmentsHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull AppointmentsHolder holder, int position) {

        holder.Name.setText(getAppointmentModels.get(position).getDoctor().getFullName());
        holder.speciality.setText(getAppointmentModels.get(position).getDoctor().getSpecialistType());
        holder.doctorAbout.setText(getAppointmentModels.get(position).getDoctor().getAbout());
        holder.patientName.setText(getAppointmentModels.get(position).getPatientName());
        holder.consultationAbout.setText(CommonSetting.getConsultationAbout());
        holder.patientName.setVisibility(View.GONE);

        String date = getAppointmentModels.get(position).getAppointmentDate();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputDate.format(d);

        holder.scheduledOn.setText(formattedDate + "    |   " + getAppointmentModels.get(position).getAppointmentTime());
        holder.ratingBar.setRating(getAppointmentModels.get(position).getDoctor().getRating());
        Picasso.get().load(getAppointmentModels.get(position).getDoctor().getProfilePic()).fit().into(holder.imageView);

        getAppointmentModels.get(position).getDoctor().setIsActive(true);

        if (getAppointmentModels.get(position).getDoctor().getIsActive()) {
            holder.videoCall.setImageResource(R.drawable.videocall);
        }

        holder.videoCall.setOnClickListener(v -> {
            if (getAppointmentModels.get(position).getDoctor().getIsActive()) {

                Intent intent = new Intent(context, VideoCallActivity.class);
                intent.putExtra("aptID", getAppointmentModels.get(position).getId());
                intent.putExtra("docID", getAppointmentModels.get(position).getDoctor().getId());
                context.startActivity(intent);

            }
        });

       /* holder.videoCall.setOnClickListener(v -> {
            Toast.makeText(context, "video calling still in progress...", Toast.LENGTH_SHORT).show();
        });*/
        holder.app_edit.setOnClickListener(v -> {
            //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_app_edit_pop, null);
            TextView tv_dialog_docName = dialogView.findViewById(R.id.tv_dialog_docName);
            TextView tv_dialog_docSpec = dialogView.findViewById(R.id.tv_dialog_docSpec);
            TextView date_picker_action = dialogView.findViewById(R.id.date_picker_action);
            tv_avail_doc = dialogView.findViewById(R.id.tv_avail_doc);
            EditText edt_dialog_Reason = dialogView.findViewById(R.id.edt_dialog_Reason);
            Button btn_dialog_cancel = dialogView.findViewById(R.id.btn_dialog_cancel);
            Button btn_dialog_reschedule = dialogView.findViewById(R.id.btn_dialog_reschedule);
            Button check_avail = dialogView.findViewById(R.id.check_avail);
            Button btn_dialog_submit = dialogView.findViewById(R.id.btn_dialog_submit);
            ImageView dialog_image_close = dialogView.findViewById(R.id.dialog_image_close);
            CircleImageView imageView = dialogView.findViewById(R.id.dialog_image);
            LinearLayout lnr_doc_edt = dialogView.findViewById(R.id.lnr_doc_edt);


            Picasso.get().load(getAppointmentModels.get(position).getDoctor().getProfilePic()).fit().into(imageView);
            tv_dialog_docName.setText(getAppointmentModels.get(position).getDoctor().getFullName());
            tv_dialog_docSpec.setText(getAppointmentModels.get(position).getDoctor().getSpecialistType());

            // working method check once
            int docID = getAppointmentModels.get(position).getId();

            btn_dialog_reschedule.setOnClickListener(v1 -> {
                btn_dialog_cancel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                btn_dialog_cancel.setTextColor(Color.parseColor("#1D3963"));
                btn_dialog_reschedule.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1D3963")));
                btn_dialog_reschedule.setTextColor(Color.parseColor("#ffffff"));
                lnr_doc_edt.setVisibility(View.VISIBLE);
                edt_dialog_Reason.setVisibility(View.GONE);

                btn_dialog_submit.setText("Book Appointment");

            });

            btn_dialog_cancel.setOnClickListener(v1 -> {
                btn_dialog_cancel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1D3963")));
                btn_dialog_cancel.setTextColor(Color.parseColor("#ffffff"));
                btn_dialog_reschedule.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                btn_dialog_reschedule.setTextColor(Color.parseColor("#1D3963"));
                lnr_doc_edt.setVisibility(View.GONE);
                edt_dialog_Reason.setVisibility(View.VISIBLE);

                btn_dialog_submit.setText("Cancel Appointment");

            });

            btn_dialog_submit.setOnClickListener(v1 -> {
                if (btn_dialog_submit.getText().toString().equals("Cancel Appointment")) {
                    if (!edt_dialog_Reason.getText().toString().isEmpty()) {
                        CancelAppointment(edt_dialog_Reason.getText().toString(), "Cancelled", getAppointmentModels.get(position).getId());
                    } else {
                        Toast.makeText(context, "Please Enter Some Description", Toast.LENGTH_SHORT).show();
                    }
                } else if (btn_dialog_submit.getText().toString().equals("Book Appointment")) {
                    if (tv_avail_doc.getText().toString().equals("There is no doctor available at this time")) {
                        Toast.makeText(context, "please check availability", Toast.LENGTH_SHORT).show();
                    } else {
                        String id = String.valueOf(getAppointmentModels.get(position).getDoctor().getId());
                        RescheduleAppointment(getAppointmentModels.get(position).getId(), SelectedDay, formattedTime, getAppointmentModels.get(position).getDoctor().getConsultationFee(), id);
                    }
                }
            });


            //getting calendar instance
            Calendar myCalendar = Calendar.getInstance();
            int year = myCalendar.get(Calendar.YEAR);
            int month = myCalendar.get(Calendar.MONTH);
            int day = myCalendar.get(Calendar.DAY_OF_MONTH);
            date_picker_action.setOnClickListener(v1 -> {
                DatePickerDialog startDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy"; // your format
                        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.getDefault());

                        // datePicker.setMinDate(System.currentTimeMillis() - 1000);

                        date_picker_action.setText(sdf1.format(myCalendar.getTime()));

                        SelectedDay = date_picker_action.getText().toString();

                        Log.d(TAG, "onDateSet: " + SelectedDay);
                    }
                }, year, month, day);
                //Disable date before today
                startDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                startDatePicker.show();
            });


            check_avail.setOnClickListener(v1 -> {
                String token = "Token " + CommonSetting.getToken();
                Call<List<DoctorListModel>> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .GetSpecificDoctorAvailable(token, SelectedDay, docID);

                call.enqueue(new Callback<List<DoctorListModel>>() {
                    @Override
                    public void onResponse(Call<List<DoctorListModel>> call, Response<List<DoctorListModel>> response) {
                        if (response.code() == 200 && response.body().size() > 0) {

                            tv_avail_doc.setVisibility(View.VISIBLE);
                            String avl_time = response.body().get(0).getFromTime();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputTime = new SimpleDateFormat("hh:mm a");
                            Date d1 = null;
                            try {
                                d1 = sdf1.parse(avl_time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            formattedTime = outputTime.format(d1);
                            tv_avail_doc.setText("Doctor Available at " + formattedTime + " on " + SelectedDay);
                        } else {

                            Toast.makeText(context, "There is no data", Toast.LENGTH_SHORT).show();
                            tv_avail_doc.setVisibility(View.VISIBLE);
                            tv_avail_doc.setText("There is no doctor available at this time");
                            tv_avail_doc.setTextColor(Color.parseColor("#F32E2E"));
                            btn_dialog_submit.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DoctorListModel>> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });

                // GetSpecificDoctorAvailable(token, SelectedDay, getAppointmentModels.get(position).getId(), docID);

            });


            builder.setView(dialogView);
            builder.setCancelable(false);
            final AlertDialog show = builder.show();


            dialog_image_close.setOnClickListener(v12 -> show.dismiss());
        });
    }

    private void RescheduleAppointment(int ID, String selectedDay, String formattedTime, String consultationFee, String id) {
        RescheduleAppModel rescheduleAppModel = new RescheduleAppModel(selectedDay, formattedTime, consultationFee, id);

        Call<AppointmentModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .RescheduleAppointment("Token " + CommonSetting.getToken(), ID, rescheduleAppModel);

        call.enqueue(new Callback<AppointmentModel>() {
            @Override
            public void onResponse(Call<AppointmentModel> call, Response<AppointmentModel> response) {
                if (response.code() == 200) {
                    // Toast.makeText(context, "djfjafjdn", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, AppointmentActivity.class));
                }
            }

            @Override
            public void onFailure(Call<AppointmentModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void CancelAppointment(String description, String cancelled, Integer id) {

        CancelAppModel cancelAppModel = new CancelAppModel(description, cancelled);

        Call<AppointmentModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .CancelAppointment("Token " + CommonSetting.getToken(), id, cancelAppModel);

        call.enqueue(new Callback<AppointmentModel>() {
            @Override
            public void onResponse(Call<AppointmentModel> call, Response<AppointmentModel> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "DeleteDocFile: " + CommonSetting.getToken());
                    context.startActivity(new Intent(context, AppointmentActivity.class));
                }
            }

            @Override
            public void onFailure(Call<AppointmentModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    @Override
    public int getItemCount() {
        return getAppointmentModels.size();
    }

    public static class AppointmentsHolder extends RecyclerView.ViewHolder {

        TextView Name, speciality, doctorAbout, patientName, consultationAbout, scheduledOn;
        RatingBar ratingBar;
        ImageView videoCall;
        ImageView imageView, app_edit;

        public AppointmentsHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.app_doctor_name);
            speciality = itemView.findViewById(R.id.app_doctor_speciality);
            doctorAbout = itemView.findViewById(R.id.app_doctor_about);
            patientName = itemView.findViewById(R.id.app_patient_name);
            consultationAbout = itemView.findViewById(R.id.app_consultation_about);
            scheduledOn = itemView.findViewById(R.id.app_scheduled_on);
            ratingBar = itemView.findViewById(R.id.app_ratingBar);
            videoCall = itemView.findViewById(R.id.app_videoCall);
            imageView = itemView.findViewById(R.id.app_doctor_image);
            app_edit = itemView.findViewById(R.id.app_edit);
        }
    }
}
