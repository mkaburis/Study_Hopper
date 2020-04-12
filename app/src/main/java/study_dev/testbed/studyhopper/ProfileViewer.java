package study_dev.testbed.studyhopper;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.models.Profile;
import study_dev.testbed.studyhopper.models.StudentClass;
import study_dev.testbed.studyhopper.ui.profile.classListItem;

public class ProfileViewer extends AppCompatActivity {
    DocumentReference userRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView nameTextView;
    TextView ageTextView;
    TextView genderTextView;

    TextView collegeTextView;
    TextView majorTextView;

    Button messageButton;
    Button inviteGroupButton;

    RecyclerView classRecycleView;
    private ArrayList<classListItem> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_viewer);

        /*Bundle extras = getIntent().getExtras();
        if (extras == null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            return;
        }*/

        //String docId = extras.getString("user-docId");
        userRef = db.collection("users").document("MykxE4OiK0qpBDkzF6cl");


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

        classRecycleView = findViewById(R.id.classRecycleView);
        classList = new ArrayList<>();
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
                }
            }
        });

    }

}
