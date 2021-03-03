package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChange {
    @SerializedName("done")
    @Expose
    private String done;

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

}

