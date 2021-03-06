package study_dev.testbed.studyhopper.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.models.Member;

public class CreateGroup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "CreateGroup";
    private EditText editTextGroupName;
    private EditText editTextCourseSubject;
    private EditText editTextCourseNumber;
    private NumberPicker groupMaxSizePicker;
    private EditText editTextMaxSize;
    private TextView groupPreferencesPrompt;

    private Spinner groupPreferencesSpinner;
    private Spinner ageSpinner;
    private Spinner locationSpinner;

    private String groupColor, groupPreference, preferenceSelected;
    private String userEmail;
    private String userDocId;
    private String groupId;
    private String userGroupId;

    private int colorSelected = 0;
    private ImageView blue, green, yellow, red, purple, orange, brown, gray;
    private Member newMember;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private String userProfileId;
    private Group groupTemplate;
    private String userUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        userProfileId = intent.getStringExtra("firestore-id");

        editTextGroupName = findViewById(R.id.edit_text_group_name);
        editTextCourseSubject = findViewById(R.id.edit_text_group_course_subject);
        editTextCourseNumber = findViewById(R.id.edit_text_group_course_number);

        editTextMaxSize = findViewById(R.id.edit_text_group_max);
        blue = findViewById(R.id.image_view_blue);
        green = findViewById(R.id.image_view_green);
        yellow = findViewById(R.id.image_view_yellow);
        red = findViewById(R.id.image_view_red);
        purple = findViewById(R.id.image_view_purple);
        orange = findViewById(R.id.image_view_orange);
        brown = findViewById(R.id.image_view_brown);
        gray = findViewById(R.id.image_view_gray);

        blue.setOnClickListener(this);
        green.setOnClickListener(this);
        yellow.setOnClickListener(this);
        red.setOnClickListener(this);
        purple.setOnClickListener(this);
        orange.setOnClickListener(this);
        brown.setOnClickListener(this);
        gray.setOnClickListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Create A Group");

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
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                userDocId = documentSnapshot.getId();
                                userUniversity = documentSnapshot.getString("university");
                            }
                        }

                    }
                });

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        groupPreferencesSpinner = findViewById(R.id.spinner_group_preferences);
        locationSpinner = findViewById(R.id.locationSpinner);
        ageSpinner = findViewById(R.id.ageSpinner);
        fillGroupPreferencesSpinner();
    }

    public void createStudyGroup(View view) {
        String groupName = editTextGroupName.getText().toString();
        String courseSubject = editTextCourseSubject.getText().toString();
        String courseNumber = editTextCourseNumber.getText().toString();
        String groupPreference = groupPreferencesSpinner.getSelectedItem().toString();
        String agePreference = ageSpinner.getSelectedItem().toString();
        String locationPreference = locationSpinner.getSelectedItem().toString();
        int groupSizeMax;

        boolean validGroup = true;

        if(groupName.trim().isEmpty()){
            editTextGroupName.setError("A group name is required!");
            validGroup = false;
        }

        if (courseSubject.trim().isEmpty()) {
            editTextCourseSubject.setError("A course code is required!");
            validGroup = false;
        }
        if (courseNumber.trim().isEmpty()) {
            editTextCourseNumber.setError("A course code is required!");
            validGroup = false;
        }

        if(editTextMaxSize.getText().toString().equals("")) {
            editTextMaxSize.setError("Please select the maximum number of members for the group!");
            validGroup = false;
        }

        if(groupPreference.trim().isEmpty()) {
            Toast.makeText(this, "Please select a group preference from the dropdown!", Toast.LENGTH_SHORT).show();
            validGroup = false;
        }

        if (colorSelected == 0)
        {
            Snackbar.make(view, "Please select a color for the group", Snackbar.LENGTH_LONG);
            validGroup = false;

        }

        if(groupPreference.equals("Coed Group"))
            preferenceSelected = "Coed";
        else if(groupPreference.equals("Females Only Group"))
            preferenceSelected = "Females Only";
        else if(groupPreference.equals("Males Only Group"))
            preferenceSelected = "Males Only";


        if(validGroup) {
            groupSizeMax = groupMaxSizePicker.getValue();

            // Firebase reference for "Groups" Collection
            CollectionReference groupRef = db.collection("groups");

            // Firebase reference for "Groups" in users sub-collection
            CollectionReference userGroupRef = db
                    .collection("users").document(userProfileId)
                    .collection("groups");

            groupId = db.collection("groups").document().getId();
            DocumentReference groupDocRef = db.collection("groups").document(groupId);

            groupTemplate = new Group(groupName, courseSubject, courseNumber, groupColor, preferenceSelected,
                    getUserName(), groupSizeMax, groupDocRef, userUniversity,
                    locationPreference, agePreference);

            db.collection("groups").document(groupId).set(groupTemplate);

            // Firebase reference for "Groups" in users sub-collection
            userGroupId = db.collection("user").document(userDocId)
                    .collection("groups").document().getId();
            db.collection("users").document(userDocId)
                    .collection("groups").document(userGroupId).set(groupTemplate);

            DocumentReference docRef = db.collection("users").document(userDocId);

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(CreateGroup.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");

                        CollectionReference groupMemberRef = db.collection("groups").document(groupId)
                                .collection("members");
                        newMember = new Member(firstName, lastName, userDocId, userGroupId, getUserName(), true,
                                Timestamp.now());
                        groupMemberRef.add(newMember);
                    }
                }
            });

            Toast.makeText(this, "Group successfully created!", Toast.LENGTH_SHORT).show();
            MyGroups.adapter.notifyDataSetChanged();
            finish();

        }
    }

    public void selectMaxGroupSize(View v) {
        View v1 = getLayoutInflater().inflate(R.layout.dialogue_group_max_size, null);
        groupMaxSizePicker = v1.findViewById(R.id.max_group_np);
        groupMaxSizePicker.setWrapSelectorWheel(false);
        groupMaxSizePicker.setMinValue(2);
        groupMaxSizePicker.setMaxValue(300);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v1);
        builder.setTitle("Maximum Group Size: ");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int maxGroupSize = groupMaxSizePicker.getValue();
                editTextMaxSize.setText(Integer.toString(maxGroupSize));
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fillGroupPreferencesSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.group_preferences, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        groupPreferencesSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.location_preference, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        ArrayAdapter<CharSequence> ageRangeAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_range_preference, android.R.layout.simple_spinner_item);
        ageRangeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageRangeAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub DO NOT DELETE
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getBaseContext(), MyGroups.class);
        in.putExtra("firestore-id", userProfileId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {

        Drawable highlight = getResources().getDrawable(R.drawable.highlight, null);
        colorSelected = 0;
        clearBackground();

        switch(v.getId()) {
            case R.id.image_view_blue:
                blue.setBackground(highlight);
                groupColor = "Blue";
                colorSelected = 1;
                break;

            case R.id.image_view_green:
                green.setBackground(highlight);
                groupColor = "Green";
                colorSelected = 2;
                break;

            case R.id.image_view_yellow:
                yellow.setBackground(highlight);
                groupColor = "Yellow";
                colorSelected = 3;
                break;

            case R.id.image_view_red:
                red.setBackground(highlight);
                groupColor = "Red";
                colorSelected = 4;
                break;

            case R.id.image_view_purple:
                purple.setBackground(highlight);
                groupColor = "Purple";
                colorSelected = 5;
                break;

            case R.id.image_view_orange:
                orange.setBackground(highlight);
                groupColor = "Orange";
                colorSelected = 6;
                break;

            case R.id.image_view_brown:
                brown.setBackground(highlight);
                groupColor = "Brown";
                colorSelected = 7;
                break;

            case R.id.image_view_gray:
                gray.setBackground(highlight);
                groupColor = "Gray";
                colorSelected = 8;
                break;
        }

    }

    private void clearBackground() {
        blue.setBackgroundResource(0);
        green.setBackgroundResource(0);
        yellow.setBackgroundResource(0);
        red.setBackgroundResource(0);
        purple.setBackgroundResource(0);
        orange.setBackgroundResource(0);
        brown.setBackgroundResource(0);
        gray.setBackgroundResource(0);
    }

    private String getEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getEmail();
    }

    private String getUserName() {
        String username = getEmail();

        int index = username.indexOf('@');

        return username.substring(0, index);
    }
}
