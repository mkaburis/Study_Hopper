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

public class CreateGroup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner groupPreferencesSpinner;
    private NumberPicker groupMaxSizePicker;
    private EditText editTextMaxSize;
    private TextView groupPreferencesPrompt;
    private ImageView blue, green, yellow, red, purple, orange, brown, gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

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

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        groupPreferencesSpinner = findViewById(R.id.spinner_group_preferences);
        fillGroupPreferencesSpinner();
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
        int selected = 0;
        clearBackground();

        switch(v.getId()) {
            case R.id.image_view_blue:
                blue.setBackground(highlight);
                selected = 1;
                break;

            case R.id.image_view_green:
                green.setBackground(highlight);
                selected = 2;
                break;

            case R.id.image_view_yellow:
                yellow.setBackground(highlight);
                selected = 3;
                break;

            case R.id.image_view_red:
                red.setBackground(highlight);
                selected = 4;
                break;

            case R.id.image_view_purple:
                purple.setBackground(highlight);
                selected = 5;
                break;

            case R.id.image_view_orange:
                orange.setBackground(highlight);
                selected = 6;
                break;

            case R.id.image_view_brown:
                brown.setBackground(highlight);
                selected = 7;
                break;

            case R.id.image_view_gray:
                gray.setBackground(highlight);
                selected = 8;
                break;
        }

        Toast.makeText(this, "Selected: " + selected, Toast.LENGTH_SHORT).show();

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
}
