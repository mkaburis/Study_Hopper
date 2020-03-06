package study_dev.testbed.studyhopper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Bundle extras = getIntent().getExtras();
        boolean newProfile = extras.getBoolean("new-profile");

        if (newProfile) {
            // blank entries
        } else {
            //fill out user data
        }

    }

}
