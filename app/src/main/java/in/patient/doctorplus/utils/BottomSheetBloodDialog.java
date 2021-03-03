package in.patient.doctorplus.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.patient.doctorplus.R;

public class BottomSheetBloodDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    private TextView tvA_Plus, tvB_Plus, tvO_Plus, tvAB_Plus, tvA_Negative, tvB_Negative, tvO_Negative, tvAB_Negative;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_blood_layout, container, false);
        tvA_Plus = v.findViewById(R.id.tvA_Plus);
        tvB_Plus = v.findViewById(R.id.tvB_Plus);
        tvO_Plus = v.findViewById(R.id.tvO_Plus);
        tvAB_Plus = v.findViewById(R.id.tvAB_Plus);
        tvA_Negative = v.findViewById(R.id.tvA_Negative);
        tvB_Negative = v.findViewById(R.id.tvB_Negative);
        tvO_Negative = v.findViewById(R.id.tvO_Negative);
        tvAB_Negative = v.findViewById(R.id.tvAB_Negative);

        //init view
        tvA_Plus.setOnClickListener(this);
        tvB_Plus.setOnClickListener(this);
        tvO_Plus.setOnClickListener(this);
        tvAB_Plus.setOnClickListener(this);
        tvA_Negative.setOnClickListener(this);
        tvB_Negative.setOnClickListener(this);
        tvO_Negative.setOnClickListener(this);
        tvAB_Negative.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvA_Plus:
                mListener.onButtonClicked(tvA_Plus.getText().toString());
                dismiss();
                break;

            case R.id.tvB_Plus:
                mListener.onButtonClicked(tvB_Plus.getText().toString());
                dismiss();
                break;
            case R.id.tvO_Plus:
                mListener.onButtonClicked(tvO_Plus.getText().toString());
                dismiss();
                break;

            case R.id.tvAB_Plus:
                mListener.onButtonClicked(tvAB_Plus.getText().toString());
                dismiss();
                break;

            case R.id.tvA_Negative:
                mListener.onButtonClicked(tvA_Negative.getText().toString());
                dismiss();
                break;

            case R.id.tvB_Negative:
                mListener.onButtonClicked(tvB_Negative.getText().toString());
                dismiss();
                break;

            case R.id.tvO_Negative:
                mListener.onButtonClicked(tvO_Negative.getText().toString());
                dismiss();
                break;

            case R.id.tvAB_Negative:
                mListener.onButtonClicked(tvAB_Negative.getText().toString());
                dismiss();
                break;


        }
    }


    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
