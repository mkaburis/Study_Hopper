package study_dev.testbed.studyhopper.ui.messages;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;

public class Messages extends AppCompatActivity {
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        Intent intent = getIntent();
        docId = intent.getStringExtra("docId");

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
        in.putExtra("documentID", docId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }
}
