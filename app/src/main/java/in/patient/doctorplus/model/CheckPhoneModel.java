package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPhoneModel {
    @SerializedName("patient_available")
    @Expose
    private Boolean patientAvailable;
    @SerializedName("patient")
    @Expose
    private Patient patient;

    public Boolean getPatientAvailable() {
        return patientAvailable;
    }

    public void setPatientAvailable(Boolean patientAvailable) {
        this.patientAvailable = patientAvailable;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
