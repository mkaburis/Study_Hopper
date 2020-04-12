package study_dev.testbed.studyhopper.ui.sessions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;

public class SessionActivity extends AppCompatActivity {
    private String sessionPath;
    private String userGroupDocId;
    private String groupDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Intent intent = getIntent();
        sessionPath = intent.getStringExtra("sessionPath");
        userGroupDocId = intent.getStringExtra("userGroupDocId");
        groupDocId = intent.getStringExtra("groupDocId");


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
        Intent in = new Intent(getBaseContext(), StudyGroupActivity.class);
        in.putExtra("userGroupDocId", userGroupDocId);
        in.putExtra("groupDocId", groupDocId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }
}
