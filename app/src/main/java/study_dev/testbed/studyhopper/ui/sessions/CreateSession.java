package study_dev.testbed.studyhopper.ui.sessions;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Session;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;

public class CreateSession extends AppCompatActivity {
//    private studyGroupItem item;
    private EditText sessionNameEditText;
    private EditText sessionDescriptionEditText;
    private EditText sessionLocationEditText;
    private EditText sessionDateEditText;
    private EditText sessionTimeStartEditText, sessionTimeEndEditText;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private DocumentReference groupDocRef;
    private String userGroupId;
    private String groupDocId;
    private int sessionSelected = -1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        Intent intent = getIntent();
        userGroupId = intent.getStringExtra("userGroupId");
        groupDocId = intent.getStringExtra("groupId");

        sessionRef = db.collection("groups").document(groupDocId).collection("sessions");


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Create a Study Session");
        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        sessionNameEditText = findViewById(R.id.edit_text_session_name);
        sessionDescriptionEditText = findViewById(R.id.edit_text_session_description);
        sessionLocationEditText = findViewById(R.id.edit_text_session_location);
        sessionDateEditText = findViewById(R.id.edit_text_session_date);
        sessionTimeStartEditText = findViewById(R.id.edit_text_session_start_time);
        sessionTimeEndEditText = findViewById(R.id.edit_text_session_end_time);

    }


    public void selectSessionDate(View v) {

        final Calendar myCalendar = Calendar.getInstance();
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(myCalendar);
            }
        };

        new DatePickerDialog(CreateSession.this, dateListener, myCalendar
        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void selectSessionTime(View v) {
        final Calendar myCalendar = Calendar.getInstance();
        final int idVal = v.getId();
        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTime(myCalendar, idVal);
            }
        };

        new TimePickerDialog(CreateSession.this, timeListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), false).show();


    }

    private void updateDate(Calendar cal) {
        String format = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        sessionDateEditText.setText(simpleDateFormat.format(cal.getTime()));
    }

    private void updateTime(Calendar cal, int viewId) {
        String format = "hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        if(viewId == R.id.edit_text_session_start_time) {
            sessionTimeStartEditText.setText(simpleDateFormat.format(cal.getTime()));
        }

        if(viewId == R.id.edit_text_session_end_time) {
            sessionTimeEndEditText.setText(simpleDateFormat.format(cal.getTime()));
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
        in.putExtra("documentID", userGroupId);
        in.putExtra("groupDocId", groupDocId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){

            case R.id.radio_button_collaborative:
                if(checked)
                    sessionSelected = 0;
                break;

            case R.id.radio_button_quiet:
                if(checked)
                    sessionSelected = 1;
                break;

            case R.id.radio_button_review:
                if(checked)
                    sessionSelected = 2;
                break;
        }
    }

    public void addSession(View view) throws ParseException {

        String sessionName = sessionNameEditText.getText().toString();
        String sessionDescription = sessionDescriptionEditText.getText().toString();
        String sessionLocation = sessionLocationEditText.getText().toString();
        String sessionDate = sessionDateEditText.getText().toString();
        String sessionStartTime = sessionTimeStartEditText.getText().toString();
        String sessionEndTime = sessionTimeEndEditText.getText().toString();

        boolean invalidInput = false;

        if(sessionName.trim().isEmpty()) {
            sessionNameEditText.setError("A title is required!");
            invalidInput = true;
        }
        else {
            sessionNameEditText.setError(null);
        }

        if(sessionLocation.trim().isEmpty()) {
            sessionLocationEditText.setError("A location is required!");
            invalidInput = true;
        } else {
            sessionLocationEditText.setError(null);
        }



        if(sessionDate.trim().isEmpty()) {
            sessionDateEditText.setError("A date is required!");
            invalidInput = true;
        } else {
            sessionDateEditText.setError(null);
        }

        if(sessionStartTime.trim().isEmpty()) {
            sessionTimeStartEditText.setError("A start time is required!");
            invalidInput = true;
        }
        else {
            sessionTimeStartEditText.setError(null);
        }

        if(sessionEndTime.trim().isEmpty()) {
            sessionTimeEndEditText.setError("An end time is required!");
            invalidInput = true;
        }
        else {
            sessionTimeEndEditText.setError(null);
        }

        if(sessionSelected == -1) {
            Snackbar.make(view, "You must select a preference for this study session!", Snackbar.LENGTH_LONG).show();
            invalidInput = true;
        }

        if(!invalidInput) {
            Date dateOfSession = new SimpleDateFormat("MM/dd/yyyy").parse(sessionDate);
            Date startTimeOfSession = new SimpleDateFormat("hh:mm aa").parse(sessionStartTime);
            Date endTimeOfSession = new SimpleDateFormat("hh:mm aa").parse(sessionEndTime);

            Timestamp newDate = new Timestamp(dateOfSession);
            Timestamp newStartTime = new Timestamp(startTimeOfSession);
            Timestamp newEndTime = new Timestamp(endTimeOfSession);

            Session newSession = new Session(sessionName, sessionDescription, sessionLocation,
                    newDate, newStartTime, newEndTime, sessionSelected);

            sessionRef.add(newSession);
            finish();
        }

    }

}
