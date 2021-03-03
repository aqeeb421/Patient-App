package in.patient.doctorplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.patient.doctorplus.R;
import in.patient.doctorplus.activity.NewAppointmentActivity;
import in.patient.doctorplus.model.SpecialitiesModel;

public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.ViewHolder> {

    ArrayList<SpecialitiesModel> specialitiesModelArrayList;
    Context context;
    private int lastCheckedPosition = -1;
    private Boolean bool;


    public SpecialistAdapter(ArrayList<SpecialitiesModel> specialitiesModelArrayList, Context context, Boolean bool) {
        this.specialitiesModelArrayList = specialitiesModelArrayList;
        this.context = context;
        this.bool = bool;
    }

    @NonNull
    @Override
    public SpecialistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speciality_row_item, parent, false);
        return new ViewHolder(view);
    }

    private RadioButton lastCheckedRB = null;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(specialitiesModelArrayList.get(position).getSpecialType());
        holder.radioButton.setChecked(position == lastCheckedPosition);

        holder.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb;
            }
        });

        String spl = specialitiesModelArrayList.get(position).getSpecialType().toLowerCase();

        switch (spl) {
            case "general physician":
                Picasso.get().load(R.drawable.services).fit().centerCrop().into(holder.imageView);
                break;
            case "dietitian":
                Picasso.get().load(R.drawable.dietitian).fit().centerCrop().into(holder.imageView);
                break;
            case "dermatologist":
                Picasso.get().load(R.drawable.dermat).fit().centerCrop().into(holder.imageView);
                break;
            case "pediatrician":
                Picasso.get().load(R.drawable.pediatritian).fit().centerCrop().into(holder.imageView);
                break;
            case "gynecologist":
                Picasso.get().load(R.drawable.gynae).fit().centerCrop().into(holder.imageView);
                break;
            case "ayurveda":
                Picasso.get().load(R.drawable.ayurveda).fit().centerCrop().into(holder.imageView);
                break;
            case "orthopedic":
                Picasso.get().load(R.drawable.orthopaedic).fit().centerCrop().into(holder.imageView);
                break;
            case "ent":
                Picasso.get().load(R.drawable.ent).fit().centerCrop().into(holder.imageView);
                break;
            case "gastroenterologist":
                Picasso.get().load(R.drawable.gastroenterologist).fit().centerCrop().into(holder.imageView);
                break;
        }


        holder.SpecialityItemView.setOnClickListener(v -> {
            int copyOfLastCheckedPosition = lastCheckedPosition;
            lastCheckedPosition = position;
            notifyItemChanged(copyOfLastCheckedPosition);
            notifyItemChanged(lastCheckedPosition);

            int selectedSpeciality = (specialitiesModelArrayList.get(lastCheckedPosition).getId());

            SharedPreferences preferences = context.getSharedPreferences("selected_speciality_item", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("selectedSpeciality", selectedSpeciality);
            editor.apply();

            if (bool) {
                context.startActivity(new Intent(context, NewAppointmentActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return specialitiesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private RadioButton radioButton;
        private RadioGroup radio_group;
        //private ProgressBar image_progress;
        private RelativeLayout SpecialityItemView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.row_image_view);
            textView = itemView.findViewById(R.id.row_text_view);
            radioButton = itemView.findViewById(R.id.row_radio_view);
            radio_group = itemView.findViewById(R.id.radio_group);
            SpecialityItemView = itemView.findViewById(R.id.SpecialityItemView);


        }
    }
}
