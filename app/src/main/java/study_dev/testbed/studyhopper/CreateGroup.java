package study_dev.testbed.studyhopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateGroup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText editTextGroupName;
    private EditText editTextCourseCode;
    private String groupColor, groupPreference, preferenceSelected;
    private Spinner groupPreferencesSpinner;
    private NumberPicker groupMaxSizePicker;
    private EditText editTextMaxSize;
    private TextView groupPreferencesPrompt;
    private int colorSelected = 0;
    private ImageView blue, green, yellow, red, purple, orange, brown, gray;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editTextGroupName = findViewById(R.id.edit_text_group_name);
        editTextCourseCode = findViewById(R.id.edit_text_group_course_code);

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
        setTitle("Create Group");

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        groupPreferencesSpinner = findViewById(R.id.spinner_group_preferences);
        fillGroupPreferencesSpinner();
    }

    public void createStudyGroup(View v) {
        String groupName = editTextGroupName.getText().toString();
        String courseCode = editTextCourseCode.getText().toString();
        String groupPreference = groupPreferencesSpinner.getSelectedItem().toString();
        int groupSizeMax;
        boolean coedGroup = false, femalesOnlyGroup = false, malesOnlyGroup = false;

        if(editTextMaxSize.getText().toString().equals("")) {
            Toast.makeText(this, "Please select the maximum number of members for the group!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            groupSizeMax = groupMaxSizePicker.getValue();
        }

        if(groupName.trim().isEmpty() || courseCode.trim().isEmpty()) {
            Toast.makeText(this, "Please input a group name and course code!", Toast.LENGTH_SHORT).show();
            return;
        }
            
        if(groupPreference.trim().isEmpty()) {
            Toast.makeText(this, "Please select a group preference from the dropdown!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (colorSelected == 0)
        {
            Toast.makeText(this, "Please select a color for the group", Toast.LENGTH_SHORT).show();
            return;
        }

        if(groupPreference.equals("Coed Group"))
            preferenceSelected = "Coed";
        else if(groupPreference.equals("Females Only Group"))
            preferenceSelected = "Females Only";
        else if(groupPreference.equals("Males Only Group"))
            preferenceSelected = "Males Only";

        // Firebase reference for "Groups" Collection
        CollectionReference groupRef = FirebaseFirestore.getInstance()
                .collection("groups");
        // Firebase Reference for "Groups" in users sub-collection
        CollectionReference userGroupRef = FirebaseFirestore.getInstance()
                .collection("users").document(getUserName())
                .collection("groups");

        Group groupTemplate = new Group(groupName, courseCode, groupColor, preferenceSelected, groupSizeMax);
        groupRef.add(groupTemplate);
        userGroupRef.add(groupTemplate);

        Toast.makeText(this, "Group added", Toast.LENGTH_SHORT).show();
        finish();
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
        groupPreferencesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        TextView groupPreferencesPrompt = (TextView) groupPreferencesSpinner.getChildAt(position);

        if(position == 0)
        {
            if(groupPreferencesPrompt != null) {
                groupPreferencesPrompt.setText("Select Gender Preferences for Group");
                groupPreferencesPrompt.setTextColor(Color.GRAY);
            }
        }
        else {
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        }


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

    private String getUserName() {
        String email = mAuth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }
}
