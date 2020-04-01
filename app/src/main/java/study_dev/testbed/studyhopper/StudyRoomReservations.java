package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;

public class StudyRoomReservations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room_reservations);

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getBaseContext(), Dashboard.class);
        startActivity(in);
        overridePendingTransition(0, 0);

    }
}
