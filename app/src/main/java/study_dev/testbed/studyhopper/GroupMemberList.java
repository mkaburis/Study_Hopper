package study_dev.testbed.studyhopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GroupMemberList extends AppCompatActivity {
    private studyGroupItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_list);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("Study Group Data");
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
        in.putExtra("Study Group Item", item);
        startActivity(in);
        overridePendingTransition(0, 0);
    }
}
