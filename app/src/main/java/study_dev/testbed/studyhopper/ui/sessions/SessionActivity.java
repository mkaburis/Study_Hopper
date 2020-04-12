package study_dev.testbed.studyhopper.ui.sessions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Session;
import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;

public class SessionActivity extends AppCompatActivity {
    public static final String TAG = "SessionActivity";

    private String sessionPath;
    private String userGroupDocId;
    private String groupDocId;
    private Session session;

    private TextView sessionNameTextView;
    private TextView sessionDateTextView;
    private TextView sessionLocationTextView;
    private TextView sessionStartTimeTextView;
    private TextView sessionEndTimeTextView;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference sessionDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Intent intent = getIntent();
        sessionPath = intent.getStringExtra("sessionPath");
        userGroupDocId = intent.getStringExtra("userGroupDocId");
        groupDocId = intent.getStringExtra("groupDocId");

        sessionNameTextView = findViewById(R.id.text_view_session_name);
        sessionDateTextView = findViewById(R.id.text_view_session_date);
        sessionStartTimeTextView = findViewById(R.id.text_view_session_start_time);
        sessionEndTimeTextView = findViewById(R.id.text_view_session_end_time);
        sessionLocationTextView = findViewById(R.id.text_view_session_location);


        sessionDoc = db.document(sessionPath);

        sessionDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Toast.makeText(SessionActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()){
                    session = documentSnapshot.toObject(Session.class);

                    Date sessionDate;
                    Date sessionStartTime, sessionEndTime;

                    sessionDate = session.getSessionDate().toDate();
                    String date = dateFormat.format(sessionDate);

                    sessionStartTime = session.getSessionStartTime().toDate();
                    sessionEndTime = session.getSessionEndTime().toDate();
                    String startTime = timeFormat.format(sessionStartTime);
                    String endTime = timeFormat.format(sessionEndTime);

                    sessionNameTextView.setText(session.getSessionName());
                    sessionLocationTextView.setText(session.getSessionLocation());
                    sessionDateTextView.setText(date);
                    sessionStartTimeTextView.setText(startTime);
                    sessionEndTimeTextView.setText(endTime);


                }

            }
        });


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
