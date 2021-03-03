package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAppointmentModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("doctor")
    @Expose
    private DoctorModel doctor;
    @SerializedName("patient")
    @Expose
    private PatientModel patient;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("patient_age")
    @Expose
    private Object patientAge;
    @SerializedName("patient_gender")
    @Expose
    private Object patientGender;
    @SerializedName("appointment_date")
    @Expose
    private String appointmentDate;
    @SerializedName("appointment_time")
    @Expose
    private String appointmentTime;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("pat_id")
    @Expose
    private Integer patId;
    @SerializedName("patient_login_status")
    @Expose
    private Boolean patientLoginStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DoctorModel getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorModel doctor) {
        this.doctor = doctor;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Object getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Object patientAge) {
        this.patientAge = patientAge;
    }

    public Object getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(Object patientGender) {
        this.patientGender = patientGender;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPatId() {
        return patId;
    }

    public void setPatId(Integer patId) {
        this.patId = patId;
    }

    public Boolean getPatientLoginStatus() {
        return patientLoginStatus;
    }

    public void setPatientLoginStatus(Boolean patientLoginStatus) {
        this.patientLoginStatus = patientLoginStatus;
    }

}
