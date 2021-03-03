package in.patient.doctorplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import in.patient.doctorplus.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        findViewById(R.id.btn_log_in).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        });
        findViewById(R.id.tv_sign_up).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });
        findViewById(R.id.btn_call).setOnClickListener(v -> {
            String phone = "08045549495";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        });

    }
}