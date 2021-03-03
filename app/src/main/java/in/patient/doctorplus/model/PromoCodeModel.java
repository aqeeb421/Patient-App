package in.patient.doctorplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoCodeModel {
    @SerializedName("can_applied")
    @Expose
    private Boolean canApplied;
    @SerializedName("code")
    @Expose
    private List<Code> code = null;

    public Boolean getCanApplied() {
        return canApplied;
    }

    public void setCanApplied(Boolean canApplied) {
        this.canApplied = canApplied;
    }

    public List<Code> getCode() {
        return code;
    }

    public void setCode(List<Code> code) {
        this.code = code;
    }
}
