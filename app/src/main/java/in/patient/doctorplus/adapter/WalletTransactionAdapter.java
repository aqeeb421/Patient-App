package in.patient.doctorplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.patient.doctorplus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.patient.doctorplus.model.Transaction;

import static android.content.ContentValues.TAG;

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder> {

    ArrayList<Transaction> walletDetailsModels;
    private Context context;
    private LayoutInflater layoutInflater;

    public WalletTransactionAdapter(ArrayList<Transaction> walletDetailsModels, Context context) {
        this.walletDetailsModels = walletDetailsModels;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public WalletTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.transaction_row, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull WalletTransactionAdapter.ViewHolder holder, int position) {

        Transaction transaction = walletDetailsModels.get(position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy | HH:mm aa");
        Date date = null;
        try {
            date = inputFormat.parse(transaction.getTransDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        System.out.println(formattedDate); // prints 10-04-2018

        holder.transactionType.setText(transaction.getTransType());
        holder.transactionDateTime.setText(formattedDate);

        if (transaction.getDebit().equals("0.00")) {
            Log.d(TAG, "onBindViewHolder: " + transaction.getCredit());
            holder.transactionAmount.setText("+ ₹ " + transaction.getCredit());
            holder.transactionAmount.setTextColor(Color.parseColor("#32a852"));

        }
        if (transaction.getCredit().equals("0.00")) {
            Log.d(TAG, "onBindViewHolder: " + transaction.getDebit());
            holder.transactionAmount.setText("- ₹ " + transaction.getDebit());
            holder.transactionAmount.setTextColor(Color.parseColor("#ff0000"));

        }


    }

    @Override
    public int getItemCount() {
        return walletDetailsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transactionDateTime, transactionType, transactionAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionDateTime = itemView.findViewById(R.id.transactionDateTime);
            transactionType = itemView.findViewById(R.id.transactionType);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
        }
    }
}
