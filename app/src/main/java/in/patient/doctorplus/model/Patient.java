package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Patient {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pat_id")
    @Expose
    private String patId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("rel_type")
    @Expose
    private Object relType;
    @SerializedName("relation")
    @Expose
    private Object relation;
    @SerializedName("ph_no")
    @Expose
    private String phNo;
    @SerializedName("s_ph_no")
    @Expose
    private Object sPhNo;
    @SerializedName("pref_lang")
    @Expose
    private Object prefLang;
    @SerializedName("street_address")
    @Expose
    private Object streetAddress;
    @SerializedName("locality")
    @Expose
    private Object locality;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("pincode")
    @Expose
    private Object pincode;
    @SerializedName("medical_history")
    @Expose
    private Object medicalHistory;
    @SerializedName("other_history")
    @Expose
    private Object otherHistory;
    @SerializedName("groups")
    @Expose
    private Object groups;
    @SerializedName("profile_pic")
    @Expose
    private Object profilePic;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("marital_status")
    @Expose
    private Object maritalStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt;
    @SerializedName("is_logged_in")
    @Expose
    private Boolean isLoggedIn;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("user")
    @Expose
    private UserDetailsModel user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Object getRelType() {
        return relType;
    }

    public void setRelType(Object relType) {
        this.relType = relType;
    }

    public Object getRelation() {
        return relation;
    }

    public void setRelation(Object relation) {
        this.relation = relation;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public Object getSPhNo() {
        return sPhNo;
    }

    public void setSPhNo(Object sPhNo) {
        this.sPhNo = sPhNo;
    }

    public Object getPrefLang() {
        return prefLang;
    }

    public void setPrefLang(Object prefLang) {
        this.prefLang = prefLang;
    }

    public Object getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(Object streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Object getLocality() {
        return locality;
    }

    public void setLocality(Object locality) {
        this.locality = locality;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getPincode() {
        return pincode;
    }

    public void setPincode(Object pincode) {
        this.pincode = pincode;
    }

    public Object getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(Object medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Object getOtherHistory() {
        return otherHistory;
    }

    public void setOtherHistory(Object otherHistory) {
        this.otherHistory = otherHistory;
    }

    public Object getGroups() {
        return groups;
    }

    public void setGroups(Object groups) {
        this.groups = groups;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Object getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Object maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UserDetailsModel getUser() {
        return user;
    }

    public void setUser(UserDetailsModel user) {
        this.user = user;
    }

}
