package in.patient.doctorplus.utils;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.patient.doctorplus.R;
import in.patient.doctorplus.activity.AppointmentActivity;
import in.patient.doctorplus.activity.HomeActivity;
import in.patient.doctorplus.activity.MyWalletActivity;
import in.patient.doctorplus.activity.ProfileActivity;
import in.patient.doctorplus.activity.StartConsultActivity;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else if (itemId == R.id.menu_new_consult) {
                startActivity(new Intent(this, StartConsultActivity.class));
            } else if (itemId == R.id.menu_appointment) {
                startActivity(new Intent(this, AppointmentActivity.class));
            } else if (itemId == R.id.menu_my_wallet) {
                startActivity(new Intent(this, MyWalletActivity.class));
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }

        }, 300);
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();
}
