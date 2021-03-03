package in.patient.doctorplus.model;

import android.view.SurfaceControl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletDetailsModel {

    @SerializedName("wallet")
    @Expose
    private Wallet wallet;
    @SerializedName("transaction")
    @Expose
    private List<Transaction> transaction = null;

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }
}
