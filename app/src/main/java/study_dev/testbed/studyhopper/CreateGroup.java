package study_dev.testbed.studyhopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGroup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner groupPreferencesSpinner;
    private NumberPicker groupMaxSizePicker;
    private EditText editTextMaxSize;
    private TextView groupPreferencesPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editTextMaxSize = findViewById(R.id.edit_text_group_max);


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
}
