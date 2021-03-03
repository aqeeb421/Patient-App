package in.patient.doctorplus.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.patient.doctorplus.R;

public class BottomSheetDateDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private BottomSheetListener2 mListener;
    private RadioButton today, tomorrow, calender;
    private ImageView img_cross;
    private TextView selectedDate;
    private String dateFormatter = "dd/MM/yyyy";
    private Button bookAppointment;

    DatePickerDialog StartTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_selected_date_layout, container, false);

        today = v.findViewById(R.id.today);
        tomorrow = v.findViewById(R.id.tomorrow);
        calender = v.findViewById(R.id.calender);
        img_cross = v.findViewById(R.id.img_cross);
        selectedDate = v.findViewById(R.id.selectedDate);
        bookAppointment = v.findViewById(R.id.btn_book_appointment);

        //Button init
        today.setOnClickListener(this);
        tomorrow.setOnClickListener(this);
        calender.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        bookAppointment.setOnClickListener(this);

        getCurrentDate();


        final Calendar newCalendar = Calendar.getInstance();
        StartTime = new DatePickerDialog(v.getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            CharSequence output = DateFormat.format("EEE, dd/MM/yyyy", newDate);
            selectedDate.setText(output);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today:
                today.setTextColor(Color.WHITE);
                tomorrow.setTextColor(Color.BLACK);
                calender.setTextColor(Color.BLACK);
                calender.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

                getCurrentDate();

                break;

            case R.id.tomorrow:
                today.setTextColor(Color.BLACK);
                tomorrow.setTextColor(Color.WHITE);
                calender.setTextColor(Color.BLACK);
                calender.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

                getNextDate();

                break;

            case R.id.calender:
                today.setTextColor(Color.BLACK);
                tomorrow.setTextColor(Color.BLACK);
                calender.setTextColor(Color.WHITE);
                calender.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                getCalendarDate();

                break;

            case R.id.img_cross:
                dismiss();
                break;

            case R.id.btn_book_appointment:
                mListener.onButtonClicked2(selectedDate.getText().toString());
                Log.d("TAG", "onClick: "+selectedDate.getText().toString());
                dismiss();
                break;
        }
    }

    private void getCalendarDate() {
        StartTime.show();
    }

    private void getCurrentDate() {

        if (today.isChecked()) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy");
            selectedDate.setText(sdf.format(cal.getTime()));
        }

    }

    public void getNextDate() {
        if (tomorrow.isChecked()) {
            Calendar cal = Calendar.getInstance();
            cal.add(cal.DAY_OF_YEAR, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy");
            selectedDate.setText(sdf.format(cal.getTime()));
        }
    }


    public interface BottomSheetListener2 {
        void onButtonClicked2(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
