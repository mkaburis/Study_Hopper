package study_dev.testbed.studyhopper.ui.studyGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import study_dev.testbed.studyhopper.R;

public class GroupMemberList extends AppCompatActivity {
    private String userGroupDocID;
    private String groupDocID;

    private EditText memberCandidateEditText;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private Pattern emailPattern = Pattern.compile(emailRegex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_list);

        Intent intent = getIntent();
        userGroupDocID = intent.getStringExtra("userDocId");


        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addGroupMember(View v){
        View addMemberView = getLayoutInflater().inflate(R.layout.dialog_add_member, null);
        memberCandidateEditText = addMemberView.findViewById(R.id.edit_member);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(addMemberView);
        builder.setTitle("Add Member");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Leave empty will override in onClickListener
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog addMemberDialog = builder.create();
        addMemberDialog.show();

        addMemberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean closeDialog = false;

                String memberEmail = memberCandidateEditText.getText().toString();

                Matcher matcher = emailPattern.matcher(memberEmail.trim());

                if(!matcher.matches()){
                    memberCandidateEditText.setError("Invalid email!");
                }
                else
                    closeDialog = true;

                if(closeDialog)
                    addMemberDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_members_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent in = new Intent(getBaseContext(), StudyGroupActivity.class);
        in.putExtra("documentID", userGroupDocID);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

    private String getUserName() {
        String email = auth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }

    private String getUsername(String email) {
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }
}
