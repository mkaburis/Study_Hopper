package study_dev.testbed.studyhopper.ui.studyGroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.models.Session;
import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;
import study_dev.testbed.studyhopper.ui.messages.Messages;
import study_dev.testbed.studyhopper.ui.sessions.CreateSession;
import study_dev.testbed.studyhopper.ui.sessions.SessionAdapter;

public class StudyGroupActivity extends AppCompatActivity {
    private static final String TAG = "StudyGroupActivity";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Group templateGroup;
    private String userEmail;
    private String userGroupDocId, userDocId, groupID;
    private String groupDocId;
    private DocumentReference userGroupDocRef ;
    private DocumentReference groupDocRef;
    private CollectionReference userRef = db.collection("users");
    private CollectionReference groupRef = db.collection("groups");
    private CollectionReference sessionRef;
    private TextView groupNameTextView;
    private TextView courseCodeTextView;
    private ImageView groupColorImageView;
    private LineChart mChart;
    CardView groupMembersCard;

    private SessionAdapter sessionAdapter;
    private ArrayList<Session> sessionList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_group);
        Intent intent = getIntent();

        // Value for user groupDocId in user
        userGroupDocId = intent.getStringExtra("documentID");

        // Value for group document ID
        groupDocId = intent.getStringExtra("groupDocId");

        setUpSessionRecyclerView();

        groupNameTextView = findViewById(R.id.groupName);
        courseCodeTextView = findViewById(R.id.groupCourseCode);
        groupColorImageView = findViewById(R.id.groupCircleImage);


        // Retrieve user's email address from FirebaseAuth
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userEmail = user.getEmail();
        }

        Query userQuery = userRef.whereEqualTo("email", userEmail);
        userQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                userDocId = document.getId();

                                userGroupDocRef = userRef.document(userDocId)
                                        .collection("groups").document(userGroupDocId);

                                getGroupInformation();
                                break;
                            }
                        }
                    }
                });

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Floating action button
        FloatingActionButton fab = findViewById(R.id.messageFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, Messages.class);
                intent.putExtra("docId", userDocId);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        groupMembersCard = findViewById(R.id.studyGroupMemberCard);
        groupMembersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, GroupMemberList.class);
                intent.putExtra("userGroupDocId", userGroupDocId);
                intent.putExtra("userDocId", userDocId);
                intent.putExtra("groupDocId", groupDocId);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        mChart = findViewById(R.id.lineChart);

//        mChart.setOnChartGestureListener(StudyGroupActivity.this);
//        mChart.setOnChartValueSelectedListener(StudyGroupActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);


        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 70f));
        yValues.add(new Entry(3, 30f));
        yValues.add(new Entry(4, 50f));
        yValues.add(new Entry(5, 60f));
        yValues.add(new Entry(6, 65f));



        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

    }

    private void getGroupInformation() {
        userGroupDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(StudyGroupActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
                if(documentSnapshot.exists()){
                    templateGroup = documentSnapshot.toObject(Group.class);

                    groupNameTextView.setText(templateGroup.getGroupName());
                    courseCodeTextView.setText(templateGroup.getCourseCode());
                    groupColorImageView.setImageResource(findGroupColorId(templateGroup.getGroupColor()));

                    groupDocRef = templateGroup.getDocumentId();

                    groupDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if(documentSnapshot.exists()){

                                groupID = documentSnapshot.getId();
                            }
                        }
                    });
                }

                else {
                    groupNameTextView.setText("Error!");
                    courseCodeTextView.setText("N/A");
                    groupColorImageView.setImageResource(findGroupColorId("Gray"));
                }
            }
        });
    }

    private void setUpSessionRecyclerView() {

        sessionRef = db.collection("groups").document(groupDocId).collection("sessions");
        Query query = sessionRef.orderBy("sessionDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Session> options = new FirestoreRecyclerOptions.Builder<Session>()
                .setQuery(query, Session.class)
                .build();

        sessionAdapter = new SessionAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.sessionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionAdapter.startListening();
        recyclerView.setAdapter(sessionAdapter);

        sessionAdapter.setOnItemClickListener(new SessionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String sessionPath = documentSnapshot.getReference().getPath();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sessionAdapter != null)
            sessionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sessionAdapter.stopListening();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.study_group_menu, menu);

        MenuItem leaveGroupMenuItem = menu.findItem(R.id.leave_group_option);
        SpannableString s = new SpannableString(leaveGroupMenuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        leaveGroupMenuItem.setTitle(s);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_session_option:
                Intent in = new Intent(StudyGroupActivity.this, CreateSession.class);
                in.putExtra("userGroupId",userGroupDocId);
                in.putExtra("groupId", groupDocId);
                startActivity(in);
                overridePendingTransition(0, 0);
                return true;

            case R.id.group_settings_option:
                //TODO implement functionality for when group owner wishes to change
                // the group's settings
            case R.id.leave_group_option:

            // TODO implement functionality for when user leaves the group
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    private int findGroupColorId(String color){
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Blue", Integer.valueOf(R.drawable.ic_group_color_blue));
        colorMap.put("Green", Integer.valueOf(R.drawable.ic_group_color_green));
        colorMap.put("Yellow", Integer.valueOf(R.drawable.ic_group_color_yellow));
        colorMap.put("Red", Integer.valueOf(R.drawable.ic_group_color_red));
        colorMap.put("Purple", Integer.valueOf(R.drawable.ic_group_color_purple));
        colorMap.put("Orange", Integer.valueOf(R.drawable.ic_group_color_orange));
        colorMap.put("Brown", Integer.valueOf(R.drawable.ic_group_color_brown));
        colorMap.put("Gray", Integer.valueOf(R.drawable.ic_group_color_gray));

        return colorMap.get(color);
    }

}

