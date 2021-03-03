package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelAppModel {
    @SerializedName("cancle_note")
    @Expose
    private String cancleNote;
    @SerializedName("consultation_status")
    @Expose
    private String consultationStatus;

    public CancelAppModel(String cancleNote, String consultationStatus) {
        this.cancleNote = cancleNote;
        this.consultationStatus = consultationStatus;
    }

    public String getCancleNote() {
        return cancleNote;
    }

    public void setCancleNote(String cancleNote) {
        this.cancleNote = cancleNote;
    }

    public String getConsultationStatus() {
        return consultationStatus;
    }

    public void setConsultationStatus(String consultationStatus) {
        this.consultationStatus = consultationStatus;
    }

}
