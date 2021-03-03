package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("commission_val")
    @Expose
    private String commissionVal;
    @SerializedName("commission_type")
    @Expose
    private String commissionType;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("Registration_Number")
    @Expose
    private String registrationNumber;
    @SerializedName("specialist_type")
    @Expose
    private String specialistType;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("consultation_fee")
    @Expose
    private String consultationFee;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("mou_file")
    @Expose
    private String mouFile;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("Last_modied")
    @Expose
    private String lastModied;
    @SerializedName("Modified_by")
    @Expose
    private Object modifiedBy;
    @SerializedName("is_loggedin")
    @Expose
    private Boolean isLoggedin;
    @SerializedName("web_registration")
    @Expose
    private Boolean webRegistration;
    @SerializedName("user")
    @Expose
    private Integer user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCommissionVal() {
        return commissionVal;
    }

    public void setCommissionVal(String commissionVal) {
        this.commissionVal = commissionVal;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMouFile() {
        return mouFile;
    }

    public void setMouFile(String mouFile) {
        this.mouFile = mouFile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModied() {
        return lastModied;
    }

    public void setLastModied(String lastModied) {
        this.lastModied = lastModied;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Boolean getIsLoggedin() {
        return isLoggedin;
    }

    public void setIsLoggedin(Boolean isLoggedin) {
        this.isLoggedin = isLoggedin;
    }

    public Boolean getWebRegistration() {
        return webRegistration;
    }

    public void setWebRegistration(Boolean webRegistration) {
        this.webRegistration = webRegistration;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
