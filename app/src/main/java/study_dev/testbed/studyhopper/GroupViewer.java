package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;

public class GroupViewer extends AppCompatActivity {

    TextView groupNameTextView;
    TextView courseCodeTextView;
    TextView maxSizeTextView;
    TextView genderTextView;
    TextView locationTextView;
    TextView ageRangeTextView;
    TextView noResultsText;

    RecyclerView memberRecycleView;
    Button requestJoinButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference groupRef;
    private String primaryUserId;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_viewer);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        primaryUserId = intent.getStringExtra("primary-user-Id");

        groupRef = db.collection("groups").document(groupId);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("View Group Info");

        setUpComponents();
    }

    public void setUpComponents() {
        groupNameTextView = findViewById(R.id.groupNameTextView);
        courseCodeTextView = findViewById(R.id.courseCodeTextView);
        maxSizeTextView = findViewById(R.id.maxSizeTextView);
        genderTextView = findViewById(R.id.genderTextView);
        locationTextView = findViewById(R.id.locationTextView);
        ageRangeTextView = findViewById(R.id.ageRangeTextView);
        noResultsText = findViewById(R.id.noResultsText);
        memberRecycleView = findViewById(R.id.memberRecycleView);
        requestJoinButton = findViewById(R.id.requestJoinButton);
    }

    public void fetchFirebaseInfo() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getBaseContext(), StudyGroupFinder.class);
        in.putExtra("firestore-id", primaryUserId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

}
