package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostMedicalRecord {
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
    private Object doctor;
    @SerializedName("consultation_id")
    @Expose
    private Integer consultationId;

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

    public Object getDoctor() {
        return doctor;
    }

    public void setDoctor(Object doctor) {
        this.doctor = doctor;
    }

    public Integer getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Integer consultationId) {
        this.consultationId = consultationId;
    }

}
