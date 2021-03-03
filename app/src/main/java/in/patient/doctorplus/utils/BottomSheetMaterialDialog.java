package in.patient.doctorplus.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.patient.doctorplus.R;

public class BottomSheetMaterialDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private in.patient.doctorplus.utils.BottomSheetMaterialDialog.BottomSheetListener1 mListener;
    private RadioButton tv_single, tv_married;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_material_layout, container, false);

        //second screen
        tv_single = v.findViewById(R.id.tv_single);
        tv_married = v.findViewById(R.id.tv_married);

        tv_single.setOnClickListener(this);
        tv_married.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_single:
                mListener.onButtonClicked1(tv_single.getText().toString());
                dismiss();
                break;

            case R.id.tv_married:
                mListener.onButtonClicked1(tv_married.getText().toString());
                dismiss();
                break;
        }
    }

    public interface BottomSheetListener1 {
        void onButtonClicked1(String text);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (in.patient.doctorplus.utils.BottomSheetMaterialDialog.BottomSheetListener1) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
