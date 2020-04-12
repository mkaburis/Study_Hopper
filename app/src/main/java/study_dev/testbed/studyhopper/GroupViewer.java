package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.models.Member;
import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;
import study_dev.testbed.studyhopper.ui.studyGroup.MemberAdapter;

public class GroupViewer extends AppCompatActivity {

    TextView groupNameTextView;
    TextView courseCodeTextView;
    TextView maxSizeTextView;
    TextView genderTextView;
    TextView locationTextView;
    TextView ageRangeTextView;
    TextView noResultsText;

    Button requestJoinButton;
    RecyclerView memberRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MemberAdapter mAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference groupRef;
    private String primaryUserId;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_viewer);

        Intent intent = getIntent();
        //groupId = intent.getStringExtra("groupId");
        //primaryUserId = intent.getStringExtra("primary-user-Id");

        groupId = "VzsLSaWT72kYz92PuvQd";
        primaryUserId = "MykxE4OiK0qpBDkzF6cl";

        groupRef = db.collection("groups").document(groupId);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("View Group Info");

        setUpComponents();
        fetchFirebaseInfo();
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

        memberRecycleView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        memberRecycleView.setLayoutManager(mLayoutManager);

        requestJoinButton = findViewById(R.id.requestJoinButton);
        // TODO add event listener

    }

    public void fetchFirebaseInfo() {
        groupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document == null) {
                        return;
                    }

                    Group group = document.toObject(Group.class);

                    if (group == null) {
                        return;
                    }

                    int maxSize = group.getMaxGroupMembers();

                    groupNameTextView.setText(group.getGroupName());
                    courseCodeTextView.setText(group.getCourseCode());
                    maxSizeTextView.setText(String.valueOf(maxSize));
                    genderTextView.setText(group.getGroupPreference());
                    locationTextView.setText(group.getLocationPreference());
                    ageRangeTextView.setText(group.getAgePreference());

                }
            }
        });

        Query query = groupRef.collection("members").orderBy("firstName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();

        mAdapter = new MemberAdapter(options);
        memberRecycleView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        }

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
