package in.patient.doctorplus.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import in.patient.doctorplus.R;
import in.patient.doctorplus.model.AppointmentModel;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.model.PromoCodeModel;
import in.patient.doctorplus.model.WalletDetailsModel;
import in.patient.doctorplus.network.RetrofitClient;
import in.patient.doctorplus.utils.CommonSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentSummaryActivity extends AppCompatActivity implements PaymentResultWithDataListener, View.OnClickListener {

    private TextView summery_doctorName, summery_about, summery_time, summery_date, summery_consultAbout, promo_txt;
    private Button summery_button_confirm;
    private ImageView summery_img;
    private int selectedDoctorId;
    String DoctorId, apt_date, apt_time;
    private double finalFeeAmount = 0;
    private String strMsg, feeAmount = "360";
    private Checkout checkout;
    private SharedPreferences pref;
    private String selectedDay;
    private int selectedSpeciality;
    private String email, phNumber;
    private AlertDialog progressDialog;
    private String retriveToke;
    private double WalletBal, net_amount;
    private TextView txt_my_wallet, txt_net_amount;
    String DoctorName;
    private String TAG = "AppointmentSummaryActivity";
    //checkbox
    private CheckBox checkbox_wallet;
    private LinearLayout lnr_promoCode;
    private EditText edt_promoCode;
    private Button btn_promoCode;
    double Deducted_WalletBal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_summary);
        Checkout.preload(getApplicationContext());

        Intent intent = getIntent();
        strMsg = intent.getStringExtra("consultationMsg");
        feeAmount = intent.getStringExtra("finalFeeAmount");
        selectedDoctorId = intent.getIntExtra("id", 0);
        retriveToke = CommonSetting.getToken();

        init();

        SharedPreferences sharedPreferences = getSharedPreferences("selected_speciality_item", MODE_PRIVATE);
        selectedSpeciality = sharedPreferences.getInt("selectedSpeciality", 0);

        sharedPreferences = getSharedPreferences("selectedDay", MODE_PRIVATE);
        selectedDay = sharedPreferences.getString("selectedDay", "null");

        //GetDoctorsList(doctorId);

        GetDoctorsList(selectedSpeciality, selectedDay, selectedDoctorId);


    }

    private void GetWalletDetail(String retriveToke) {
        progressDialog.show();
        Call<List<WalletDetailsModel>> listCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetWalletDetails(retriveToke);

        listCall.enqueue(new Callback<List<WalletDetailsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<WalletDetailsModel>> call, Response<List<WalletDetailsModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    Log.d("TAG", "onResponse: " + response);
                    WalletBal = response.body().get(0).getWallet().getBalance();
                    Log.d("TAG", "onResponse: " + WalletBal);
                    txt_my_wallet.setText("₹ " + WalletBal);
                    // txt_net_amount.setText("100");

                    if (WalletBal == 0.0) {
                        checkbox_wallet.setEnabled(false);
                    }

                    checkbox_wallet.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            if (net_amount <= WalletBal) {
                                Deducted_WalletBal = WalletBal - net_amount;
                                txt_net_amount.setText("₹ 0.0");
                                txt_my_wallet.setText("₹ " + Deducted_WalletBal);
                                finalFeeAmount = 0.0;
                            } else if (net_amount > WalletBal) {
                                txt_my_wallet.setText("₹ 0.0");
                                Deducted_WalletBal = WalletBal;
                                double net_amount1 = net_amount - WalletBal;
                                txt_net_amount.setText("₹ " + net_amount1);
                                finalFeeAmount = net_amount1;
                            }
                            Log.d("TAG", "onCheckedChanged: " + finalFeeAmount);
                            lnr_promoCode.setEnabled(false);
                            edt_promoCode.setEnabled(false);
                            btn_promoCode.setEnabled(false);
                            Toast.makeText(AppointmentSummaryActivity.this, "Deducted_WalletBal : " + Deducted_WalletBal, Toast.LENGTH_SHORT).show();

                        } else {
                            finalFeeAmount = net_amount;
                            txt_my_wallet.setText("₹ " + WalletBal);
                            txt_net_amount.setText(String.valueOf(net_amount));
                            Log.d("TAG", "onCheckedChanged: " + net_amount);
                            Deducted_WalletBal = 0.0;
                            lnr_promoCode.setEnabled(true);
                            edt_promoCode.setEnabled(true);
                            btn_promoCode.setEnabled(true);
                            Toast.makeText(AppointmentSummaryActivity.this, "Deducted_WalletBal : " + Deducted_WalletBal, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    checkbox_wallet.setEnabled(false);
                }

                btn_promoCode.setOnClickListener(v -> {
                    if (!edt_promoCode.getText().toString().isEmpty()) {
                        ApplyPromoCode(edt_promoCode.getText().toString());
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edt_promoCode.getWindowToken(), 0);

                    }
                });
            }

            @Override
            public void onFailure(Call<List<WalletDetailsModel>> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    private void ApplyPromoCode(String code) {
        progressDialog.show();
        Call<PromoCodeModel> listCall = RetrofitClient
                .getInstance()
                .getApi()
                .ApplyPromoCode("Token " + retriveToke, code);

        promo_txt.setVisibility(View.VISIBLE);
        listCall.enqueue(new Callback<PromoCodeModel>() {
            @Override
            public void onResponse(Call<PromoCodeModel> call, Response<PromoCodeModel> response) {
                if (response.code() == 200) {
                    if (response.body().getCanApplied()) {
                        Log.d(TAG, "onResponse: " + response.body().getCode().get(0).getDiscountPercent());
                        double discount = Double.parseDouble(response.body().getCode().get(0).getDiscountPercent());
                        double net_amount1 = net_amount - (net_amount * (discount / 100));
                        txt_net_amount.setText(String.valueOf(net_amount1));
                        finalFeeAmount = net_amount1;
                        Toast.makeText(AppointmentSummaryActivity.this, "You Got " + discount + "% discount...", Toast.LENGTH_SHORT).show();
                        checkbox_wallet.setEnabled(false);
                        promo_txt.setText("Promo code applied successfully");
                        promo_txt.setTextColor(Color.parseColor("#1ec659"));

                    } else {
                        finalFeeAmount = net_amount;
                        txt_net_amount.setText(String.valueOf(net_amount));
                        checkbox_wallet.setEnabled(true);
                        promo_txt.setText("Invalid Promo code!!!");
                        promo_txt.setTextColor(Color.parseColor("#d30915"));
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PromoCodeModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        summery_doctorName = findViewById(R.id.summery_doctorName);
        summery_about = findViewById(R.id.summery_about);
        summery_date = findViewById(R.id.summery_date);
        summery_time = findViewById(R.id.summery_time);
        summery_consultAbout = findViewById(R.id.summery_consultAbout);
        summery_button_confirm = findViewById(R.id.summery_button_confirm);
        summery_img = findViewById(R.id.summery_img);
        txt_my_wallet = findViewById(R.id.my_wallet);
        txt_net_amount = findViewById(R.id.net_amount);
        checkbox_wallet = findViewById(R.id.checkbox_wallet);
        lnr_promoCode = findViewById(R.id.lnr_promoCode);
        btn_promoCode = findViewById(R.id.btn_promoCode);
        edt_promoCode = findViewById(R.id.edt_promoCode);
        promo_txt = findViewById(R.id.promo_txt);

        progressDialog = new SpotsDialog(this, R.style.CustomDialog);
        summery_button_confirm.setOnClickListener(this);

        pref = getApplicationContext().getSharedPreferences("SignUp Details", Context.MODE_PRIVATE);
        email = pref.getString("email", "Default@example.com");
        phNumber = pref.getString("fullPhoneNumber", "Default");
    }

    private void GetDoctorsList(int selectedSpeciality, String selectedDay, int ID) {
        progressDialog.show();
        Call<List<DoctorListModel>> listCall = RetrofitClient
                .getInstance()
                .getApi()
                .GetDoctorsList(selectedSpeciality, selectedDay);


        listCall.enqueue(new Callback<List<DoctorListModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onResponse(Call<List<DoctorListModel>> call, Response<List<DoctorListModel>> response) {
                if (response.code() == 200) {
                    List<DoctorListModel> doctorsListModels = response.body();
                    assert response.body() != null;
                    summery_doctorName.setText(response.body().get(ID).getDoctor().getFullName());
                    summery_about.setText(response.body().get(ID).getDoctor().getSpecialistType());
                    String avl_time = response.body().get(ID).getFromTime();
                    DoctorName = response.body().get(ID).getDoctor().getFullName();
                    DoctorId = String.valueOf(response.body().get(ID).getDoctor().getId());
                    apt_date = summery_date.getText().toString();
                    apt_time = summery_time.getText().toString();

                    if (response.body().get(ID).getDoctor().getCommissionType().equalsIgnoreCase("Percent")) {
                        double ConsultationFee = Double.parseDouble(response.body().get(ID).getDoctor().getConsultationFee());
                        double CommissionVal = Double.parseDouble(response.body().get(ID).getDoctor().getCommissionVal());
                        double percent = (ConsultationFee / 100.0f) * CommissionVal;
                        net_amount = percent + ConsultationFee;
                        finalFeeAmount = percent + ConsultationFee;
                        txt_net_amount.setText("₹ " + net_amount);
                    } else {
                        txt_net_amount.setText("₹ " + response.body().get(ID).getDoctor().getConsultationFee());
                        net_amount = Double.parseDouble(response.body().get(ID).getDoctor().getConsultationFee());
                        finalFeeAmount = Double.parseDouble(response.body().get(ID).getDoctor().getConsultationFee());

                    }

                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat outputTime = new SimpleDateFormat("hh:mm a");
                    Date d1 = null;
                    try {
                        d1 = sdf1.parse(avl_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    apt_time = outputTime.format(d1);

                    summery_date.setText(selectedDay);
                    summery_time.setText(apt_time);

                    apt_date = summery_date.getText().toString();

                    if (strMsg == null) {
                        summery_consultAbout.setText("Not Specified");
                    } else {
                        summery_consultAbout.setText(strMsg);
                    }

                    Picasso.get().load(doctorsListModels.get(ID).getDoctor().getProfilePic()).fit().into(summery_img);

                    GetWalletDetail("Token " + retriveToke);

                } else {
                    Toast.makeText(AppointmentSummaryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DoctorListModel>> call, Throwable t) {

                progressDialog.dismiss();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void startPayment() {
        checkout = new Checkout();  /**   * Set your logo here  */
        checkout.setKeyID("rzp_live_LCNY0ui8G8CFiO");    /**   * Instantiate Checkout   */
        final Activity activity = this;  /**   * Pass your payment options to the Razorpay Checkout as a JSONObject   */
        try {
            JSONObject options = new JSONObject();
            options.put("name", DoctorName);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://www.doctor-plus.in/static/website/img/logo.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb"); //from response of step 3.
            options.put("theme.color", "#1D3963");
            options.put("currency", "INR");
            options.put("amount", finalFeeAmount * 100);//pass amount in currency subunits
            options.put("prefill.email", CommonSetting.getEmail());
            options.put("prefill.contact", CommonSetting.getPhoneNumber());
            Log.d(TAG, "startPayment: " + CommonSetting.getEmail() + CommonSetting.getPhoneNumber());
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.summery_button_confirm:
                Log.d("TAG", "onClick: " + summery_button_confirm.getText());
                if (finalFeeAmount != 0.00)
                    startPayment();
                else {
                    SendAppointmentDetails("Token " + retriveToke, "N/A", 0, "N/A");
                }

                break;
        }
    }

    @Override
    public void onPaymentSuccess(String paymentID, PaymentData paymentData) {
        SendAppointmentDetails("Token " + retriveToke, paymentID, (int) finalFeeAmount, "Online");
    }

    private void SendAppointmentDetails(String token, String paymentID, int finalFeeAmount, String Online) {
        progressDialog.show();

        Call<AppointmentModel> appointmentModelCall = RetrofitClient.getInstance()
                .getApi()
                .SendAppointmentDetails(token, apt_date, apt_time, DoctorId, finalFeeAmount, Online, paymentID, strMsg, "Pending");

        appointmentModelCall.enqueue(new Callback<AppointmentModel>() {
            @Override
            public void onResponse(Call<AppointmentModel> call, Response<AppointmentModel> response) {

                if (response.code() == 201) {
                    Log.d("TAG", "onResponse: " + response.body().getConsultationStatus());
                    Intent intent = new Intent(getApplicationContext(), AppointmentConfirmActivity.class);
                    intent.putExtra("consultationMsg", strMsg);
                    intent.putExtra("id", selectedDoctorId);
                    startActivity(intent);
                    finish();
                    finish();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<AppointmentModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Error" + s, Toast.LENGTH_LONG).show();
    }

}