package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RescheduleAppModel {

    @SerializedName("appointment_date")
    @Expose
    private String appointmentDate;
    @SerializedName("appointment_time")
    @Expose
    private String appointmentTime;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("doctor")
    @Expose
    private String doctor;

    public RescheduleAppModel(String appointmentDate, String appointmentTime, String paidAmount, String doctor) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.paidAmount = paidAmount;
        this.doctor = doctor;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
