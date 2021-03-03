package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChangeModel {

    @SerializedName("password_change")
    @Expose
    private Boolean passwordChange;

    public Boolean getPasswordChange() {
        return passwordChange;
    }

    public void setPasswordChange(Boolean passwordChange) {
        this.passwordChange = passwordChange;
    }
}
