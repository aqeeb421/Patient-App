package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultationModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("consultation_date_time")
    @Expose
    private String consultationDateTime;
    @SerializedName("subscription_based")
    @Expose
    private Boolean subscriptionBased;
    @SerializedName("inv_number")
    @Expose
    private String invNumber;
    @SerializedName("video_audio_rating")
    @Expose
    private Integer videoAudioRating;
    @SerializedName("consultation_rating")
    @Expose
    private Integer consultationRating;
    @SerializedName("overall_rating")
    @Expose
    private Integer overallRating;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("consultation_amt")
    @Expose
    private String consultationAmt;
    @SerializedName("comp_share")
    @Expose
    private String compShare;
    @SerializedName("doctor_id")
    @Expose
    private Integer doctorId;
    @SerializedName("patient")
    @Expose
    private Integer patient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsultationDateTime() {
        return consultationDateTime;
    }

    public void setConsultationDateTime(String consultationDateTime) {
        this.consultationDateTime = consultationDateTime;
    }

    public Boolean getSubscriptionBased() {
        return subscriptionBased;
    }

    public void setSubscriptionBased(Boolean subscriptionBased) {
        this.subscriptionBased = subscriptionBased;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    public Integer getVideoAudioRating() {
        return videoAudioRating;
    }

    public void setVideoAudioRating(Integer videoAudioRating) {
        this.videoAudioRating = videoAudioRating;
    }

    public Integer getConsultationRating() {
        return consultationRating;
    }

    public void setConsultationRating(Integer consultationRating) {
        this.consultationRating = consultationRating;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getConsultationAmt() {
        return consultationAmt;
    }

    public void setConsultationAmt(String consultationAmt) {
        this.consultationAmt = consultationAmt;
    }

    public String getCompShare() {
        return compShare;
    }

    public void setCompShare(String compShare) {
        this.compShare = compShare;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }
}
