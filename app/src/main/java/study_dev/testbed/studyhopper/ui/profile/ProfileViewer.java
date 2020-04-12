package study_dev.testbed.studyhopper.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Profile;
import study_dev.testbed.studyhopper.models.StudentClass;
import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;
import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;
import study_dev.testbed.studyhopper.ui.studyGroup.GroupMemberList;

public class ProfileViewer extends AppCompatActivity {
    private DocumentReference userRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String foundUserId;
    private String primaryUserId;

    private TextView noResultsText;
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView genderTextView;

    private TextView collegeTextView;
    private TextView majorTextView;

    private Button messageButton;
    private Button inviteGroupButton;

    private RecyclerView classRecycleView;
    private ArrayList<classListItem> classList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ClassesListAdapter mAdapter;

    String userGroupDocId;
    String groupDocId;
    String returnTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_viewer);

        Intent intent = getIntent();
        foundUserId = intent.getStringExtra("found-user-docId");
        primaryUserId = intent.getStringExtra("primary-user-Id");

        returnTo = intent.getStringExtra("return-to");
        if (returnTo.equals("GroupMemberList")) {
            userGroupDocId = intent.getStringExtra("userGroupDocId");
            groupDocId = intent.getStringExtra("groupDocId");
        }

        if (primaryUserId == null) {
            Intent newIntent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(newIntent);
            return;
        }

        if (foundUserId == null) {
            Intent newIntent = new Intent(getApplicationContext(), StudyGroupFinder.class);
            startActivity(newIntent);
            return;
        }

        userRef = db.collection("users").document(foundUserId);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("View Profile");

        setupComponents();
        fillProfile();
    }

    public void setupComponents() {
        nameTextView = findViewById(R.id.nameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        genderTextView = findViewById(R.id.genderTextView);

        collegeTextView = findViewById(R.id.collegeTextView);
        majorTextView = findViewById(R.id.majorTextView);

        messageButton = findViewById(R.id.messageButton);
        inviteGroupButton = findViewById(R.id.inviteGroupButton);

        noResultsText = findViewById(R.id.noResultsText);

        classList = new ArrayList<>();
        classRecycleView = findViewById(R.id.classRecycleView);
        classRecycleView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        classRecycleView.setLayoutManager(mLayoutManager);
        reloadRecycler();


    }

    public void fillProfile() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    Profile profile = document.toObject(Profile.class);

                    nameTextView.setText(profile.getFullName());
                    genderTextView.setText(profile.getGender());
                    ageTextView.setText(profile.getAge());

                    collegeTextView.setText(profile.getCollege());
                    majorTextView.setText(profile.getMajor());
                }
            }
        });

        userRef.collection("classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        StudentClass studentClass = doc.toObject(StudentClass.class);

                        classListItem listItem = new classListItem(doc.getId(),
                                studentClass.getClassName(), studentClass.getSubject(),
                                studentClass.getNumber(), studentClass.getSection());
                        classList.add(listItem);
                    }
                    reloadRecycler();
                }
            }
        });

    }

    private void reloadRecycler() {
        if (classList.size() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        } else {
            noResultsText.setVisibility(View.GONE);
            mAdapter = new ClassesListAdapter(classList);
            classRecycleView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (returnTo.equals("GroupMemberList")) {


            Intent in = new Intent(getBaseContext(), GroupMemberList.class);
            in.putExtra("userDocId", primaryUserId);
            in.putExtra("userGroupDocId", userGroupDocId);
            in.putExtra("groupDocId", groupDocId);

            startActivity(in);
            overridePendingTransition(0, 0);
        } else {
            Intent in = new Intent(getBaseContext(), StudyGroupFinder.class);
            in.putExtra("firestore-id", primaryUserId);
            startActivity(in);
            overridePendingTransition(0, 0);
        }
    }

}
