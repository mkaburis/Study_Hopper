package study_dev.testbed.studyhopper.ui.studyGroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.dashboard.Dashboard;
import study_dev.testbed.studyhopper.ui.messages.Messages;
import study_dev.testbed.studyhopper.ui.sessions.SessionPage;

public class StudyGroupActivity extends AppCompatActivity {

    private static final String TAG = "StudyGroupActivity";
    private study_dev.testbed.studyhopper.models.studyGroupItem studyGroupItem;

    private LineChart mChart;
    CardView groupMembersCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_group);

        Intent intent = getIntent();
        studyGroupItem = intent.getParcelableExtra("Study Group Item");

        int imageRes = studyGroupItem.getImageResource();
        String line1 = studyGroupItem.getText1();
        String line2 = studyGroupItem.getText2();

        ImageView imageView = findViewById(R.id.groupCircleImage);
        imageView.setImageResource(imageRes);

        TextView textView1 = findViewById(R.id.groupName);
        textView1.setText(line1);

        TextView textView2 = findViewById(R.id.groupCourseCode);
        textView2.setText(line2);

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
                intent.putExtra("Study Group Data", studyGroupItem);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        groupMembersCard = findViewById(R.id.studyGroupMemberCard);
        groupMembersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, GroupMemberList.class);
                intent.putExtra("Study Group Data", studyGroupItem);
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
                Intent in = new Intent(StudyGroupActivity.this, SessionPage.class);
                in.putExtra("Study Group Data", studyGroupItem);
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
}

