package study_dev.testbed.studyhopper.ui.studyGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Member;
import study_dev.testbed.studyhopper.models.Profile;

public class GroupMemberList extends AppCompatActivity {
    private String userGroupDocID;
    private String groupDocID;

    private EditText memberCandidateEditText;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private CollectionReference groupMemberRef;
    private String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private Pattern emailPattern = Pattern.compile(emailRegex);
    private boolean closeDialog = false, memberExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_list);

        Intent intent = getIntent();
        userGroupDocID = intent.getStringExtra("userDocId");
        groupDocID = intent.getStringExtra("groupDocId");


        groupMemberRef = FirebaseFirestore.getInstance()
                .collection("groups").document(groupDocID)
                .collection("members");


        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addGroupMember(View v) {
        closeDialog = false;
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
                memberExists = false;

                final String memberEmail = memberCandidateEditText.getText().toString();

                Matcher matcher = emailPattern.matcher(memberEmail.trim());

                if (!matcher.matches()) {
                    memberCandidateEditText.setError("Invalid email!");
                    return;
                } else {
                    usersRef.document(getUsername(memberEmail)).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Profile tempProfile = documentSnapshot.toObject(Profile.class);

                                    if (tempProfile != null) {
                                        String fName = tempProfile.getFirstName();
                                        String lName = tempProfile.getLastName();

                                        final Member newMember = new Member(fName, lName, getUsername(memberEmail),
                                                getUsername(memberEmail), false, Timestamp.now());

                                        groupMemberRef.whereEqualTo("userDocId", getUsername(memberEmail))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                if (document != null) {
                                                                    memberCandidateEditText.setError("Member is already in group!");
                                                                    memberExists = true;
                                                                    closeDialog = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                        // Only add a new member once we ensure a member does not already exist
                                        if (!memberExists) {
                                            groupMemberRef.add(newMember);
                                            closeDialog = true;
                                        }

                                    }
                                    else {
                                        memberCandidateEditText.setError("Email does not exist within system");
                                        closeDialog = false;
                                    }
                                }
                            });
                }


                if (closeDialog)
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
