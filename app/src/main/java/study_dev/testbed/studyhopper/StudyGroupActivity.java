package study_dev.testbed.studyhopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StudyGroupActivity extends AppCompatActivity {

    private static final String TAG = "StudyGroupActivity";

    private LineChart mChart;
    CardView groupMembersCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_group);

        Intent intent = getIntent();
        studyGroupItem studyGroupItem = intent.getParcelableExtra("Study Group Item");

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
                startActivity(intent);
            }
        });

        groupMembersCard = findViewById(R.id.studyGroupMemberCard);
        groupMembersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, GroupMemberList.class);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {


        Intent in = new Intent(getBaseContext(), StudyGroupActivity.class);
        startActivity(in);
        overridePendingTransition(0, 0);
    }
}
