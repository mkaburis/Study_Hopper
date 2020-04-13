package study_dev.testbed.studyhopper.ui.sessions;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Session;
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

    private static DecimalFormat formatDecimal = new DecimalFormat("#.##");

    private TextView audioLevelTextView;
    private ImageView audioHappinessImageView;
    MediaRecorder mediaRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    private final double REF_AMP = 1.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateTv();
        }
    };

    final Handler handler = new Handler();



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

        audioLevelTextView = findViewById(R.id.audio_level);
        audioHappinessImageView = findViewById(R.id.sound_happiness_image_view);

//        if(runner == null){
//            runner = new Thread(){
//                @Override
//                public void run() {
//                    while(runner != null)
//                    {
//                        try {
//                            Thread.sleep(1000);
//                            Log.i("Noise", "Tock");
//                        }catch (InterruptedException e){ }
//                        handler.post(updater);
//                    }
//                }
//            };
//            runner.start();
//            Log.d("Noise", "start runner()");
//        }


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
    protected void onResume() {
        super.onResume();
        startRecorder();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }


        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null");

            try {
                mediaRecorder.prepare();
            }catch(java.io.IOException ioe){
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));
            }catch (java.lang.SecurityException e){
                android.util.Log.e(TAG, "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }

            try{
                mediaRecorder.start();
            }catch (java.lang.SecurityException e){
                android.util.Log.e(TAG, "SecurityException: " +
                android.util.Log.getStackTraceString(e));
            }
        }
    }

    public void stopRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public double updateTv() {
//        Toast.makeText(this, getAmplitudeEMA() + " db", Toast.LENGTH_SHORT).show();

        double dBRating = soundDb(REF_AMP);
        if (!Double.isFinite(dBRating)) {
            dBRating = 0;
        }
        audioLevelTextView.setText(formatDecimal.format(dBRating) + " db");

        if(dBRating >= 0.0 && dBRating <= 60.0) {
            audioHappinessImageView.setImageResource(R.drawable.ic_happy_face);
        }
        else if (dBRating > 60.0 && dBRating <= 80.0) {
            audioHappinessImageView.setImageResource(R.drawable.ic_neutral_face);
        }
        else{
            audioHappinessImageView.setImageResource(R.drawable.ic_sad_face);
        }

        return dBRating;
    }

    public double soundDb(double ampl){
        return 20 * Math.log10(getAmplitudeEMA() / ampl);
    }

    public double getAmplitude() {
        if(mediaRecorder != null)
            return mediaRecorder.getMaxAmplitude();
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
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

    public void recordAudio(final View view) {
        final double[] sumNoiseLevel = {0};
        startRecorder();
        CountDownTimer countDowntimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                sumNoiseLevel[0] = sumNoiseLevel[0] + updateTv();
            }

            public void onFinish() {
                final double avgNoiseLevel = sumNoiseLevel[0] / 10;
                Snackbar.make(view, "Completed sampling noise level in room!", Snackbar.LENGTH_LONG);

                stopRecorder();
                saveNoiseLevel(avgNoiseLevel);

            }};countDowntimer.start();
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

    private void saveNoiseLevel(final double avgNoiseLevel) {
        sessionDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(SessionActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }


                if (documentSnapshot.exists() && Double.isFinite(avgNoiseLevel)) {
                    session = documentSnapshot.toObject(Session.class);

                    session.setNoiseLevel(avgNoiseLevel);

                    sessionDoc.set(session);
                }
            }
        });
    }
}
