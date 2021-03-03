package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPatientPrescriptionModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("patient")
    @Expose
    private Integer patient;
    @SerializedName("record_type")
    @Expose
    private String recordType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("record_files")
    @Expose
    private String recordFiles;
    @SerializedName("is_prescription")
    @Expose
    private Boolean isPrescription;
    @SerializedName("doctor")
    @Expose
    private DoctorModel doctor;
    @SerializedName("consultation_id")
    @Expose
    private Integer consultationId;
    @SerializedName("Last_modied")
    @Expose
    private String lastModied;
    @SerializedName("format_last_modified")
    @Expose
    private String formatLastModified;
    @SerializedName("follow_up_date")
    @Expose
    private String followUpDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordFiles() {
        return recordFiles;
    }

    public void setRecordFiles(String recordFiles) {
        this.recordFiles = recordFiles;
    }

    public Boolean getIsPrescription() {
        return isPrescription;
    }

    public void setIsPrescription(Boolean isPrescription) {
        this.isPrescription = isPrescription;
    }

    public DoctorModel getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorModel doctor) {
        this.doctor = doctor;
    }

    public Integer getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Integer consultationId) {
        this.consultationId = consultationId;
    }

    public String getLastModied() {
        return lastModied;
    }

    public void setLastModied(String lastModied) {
        this.lastModied = lastModied;
    }

    public String getFormatLastModified() {
        return formatLastModified;
    }

    public void setFormatLastModified(String formatLastModified) {
        this.formatLastModified = formatLastModified;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

}
