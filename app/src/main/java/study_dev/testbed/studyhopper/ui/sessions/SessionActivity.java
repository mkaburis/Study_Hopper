package study_dev.testbed.studyhopper.ui.sessions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private TextView sessionTypeTextView;
    private ImageView sessionTypeImageView;
    private EditText sessionDateEditText;
    private EditText sessionStartTimeEditText;
    private EditText sessionEndTimeEditText;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;


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

        setTitle("Study Session");
        sessionNameTextView = findViewById(R.id.text_view_session_name);
        sessionDateTextView = findViewById(R.id.text_view_session_date);
        sessionStartTimeTextView = findViewById(R.id.text_view_session_start_time);
        sessionEndTimeTextView = findViewById(R.id.text_view_session_end_time);
        sessionLocationTextView = findViewById(R.id.text_view_session_location);
        sessionTypeTextView = findViewById(R.id.session_type_text);
        sessionTypeImageView = findViewById(R.id.session_type_image);
        sessionDateEditText = findViewById(R.id.edit_text_change_session_date);
        sessionStartTimeEditText = findViewById(R.id.edit_text_change_session_start_time);
        sessionEndTimeEditText = findViewById(R.id.edit_text_change_session_end_time);


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

                    int sessionType = session.getSessionType();

                    sessionStartTime = session.getSessionStartTime().toDate();
                    sessionEndTime = session.getSessionEndTime().toDate();
                    String startTime = timeFormat.format(sessionStartTime);
                    String endTime = timeFormat.format(sessionEndTime);

                    sessionNameTextView.setText(session.getSessionName());
                    sessionLocationTextView.setText(session.getSessionLocation());
                    sessionDateTextView.setText(date);
                    sessionStartTimeTextView.setText(startTime);
                    sessionEndTimeTextView.setText(endTime);

                    switch (sessionType) {
                        case 0:
                            sessionTypeImageView.setImageResource(R.drawable.ic_collaborative_session);
                            sessionTypeTextView.setText(R.string.session_type_collaborative);
                            break;
                        case 1:
                            sessionTypeImageView.setImageResource(R.drawable.ic_quiet_session);
                            sessionTypeTextView.setText(R.string.session_type_quiet);
                            break;
                        case 2:
                            sessionTypeImageView.setImageResource(R.drawable.ic_review_session);
                            sessionTypeTextView.setText(R.string.session_type_review);
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

    public void deleteSession(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Delete Session")
                .setMessage("Are you sure you would like to delete this session?")
                .setIcon(R.drawable.ic_alert_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionDoc.delete();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .create()
                .show();
    }

    public void updateSessionDateTime(final View view) {
        boolean invalidInput = false;
        final String updateSessionDate = sessionDateEditText.getText().toString();
        final String updateSessionStartTime = sessionStartTimeEditText.getText().toString();
        final String updateSessionEndTime = sessionEndTimeEditText.getText().toString();

        if(updateSessionDate.trim().isEmpty()) {
            sessionDateEditText.setError("A date is required!");
            invalidInput = true;
        } else {
            sessionDateEditText.setError(null);
        }

        if(updateSessionStartTime.trim().isEmpty()) {
            sessionStartTimeEditText.setError("A start time is required!");
            invalidInput = true;
        }
        else {
            sessionStartTimeEditText.setError(null);
        }

        if(updateSessionEndTime.trim().isEmpty()) {
            sessionEndTimeEditText.setError("An end time is required!");
            invalidInput = true;
        }
        else {
            sessionEndTimeEditText.setError(null);
        }

        if(!invalidInput)
        {
            sessionDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if(e != null){
                        Toast.makeText(SessionActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }

                    if (documentSnapshot.exists()) {
                        session = documentSnapshot.toObject(Session.class);

                        try {
                            Date updateDate = dateFormat.parse(updateSessionDate);
                            Date updateStartTime = timeFormat.parse(updateSessionStartTime);
                            Date updateEndTime = timeFormat.parse(updateSessionEndTime);

                            Timestamp timestampDate = new Timestamp(updateDate);
                            Timestamp timestampStartTime = new Timestamp(updateStartTime);
                            Timestamp timestampEndTime = new Timestamp(updateEndTime);

                            session.setSessionDate(timestampDate);
                            session.setSessionStartTime(timestampStartTime);
                            session.setSessionEndTime(timestampEndTime);

                            sessionDoc.set(session);

                            Snackbar.make(view,"Updated session date and time", Snackbar.LENGTH_LONG).show();

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }


                    }

                }
            });
        }
    }

    public void updateSessionDate(View v) {

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(SessionActivity.this, dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDate(Calendar cal) {
        String format = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        sessionDateEditText.setText(simpleDateFormat.format(cal.getTime()));
    }

    public void updateSessionTime(View v) {
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

        new TimePickerDialog(SessionActivity.this, timeListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), false).show();


    }

    private void updateTime(Calendar cal, int viewId) {
        String format = "hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        if(viewId == R.id.edit_text_change_session_start_time) {
            sessionStartTimeEditText.setText(simpleDateFormat.format(cal.getTime()));
        }

        if(viewId == R.id.edit_text_change_session_end_time) {
            sessionEndTimeEditText.setText(simpleDateFormat.format(cal.getTime()));
        }

    }
}
