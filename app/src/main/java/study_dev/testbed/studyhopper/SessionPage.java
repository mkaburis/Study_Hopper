package study_dev.testbed.studyhopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SessionPage extends AppCompatActivity {
    private studyGroupItem item;
    private EditText sessionDateEditText;
    private EditText sessionTimeStartEditText, sessionTimeEndEditText;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_page);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("Study Group Data");

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        new DatePickerDialog(SessionPage.this, dateListener, myCalendar
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

        new TimePickerDialog(SessionPage.this, timeListener, myCalendar.get(Calendar.HOUR_OF_DAY),
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
        Intent in = new Intent(getBaseContext(), Dashboard.class);
        in.putExtra("Study Group Item", item);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

}
