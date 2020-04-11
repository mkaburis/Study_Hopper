package study_dev.testbed.studyhopper.ui.studyGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.models.Member;
import study_dev.testbed.studyhopper.models.Profile;

public class GroupMemberList extends AppCompatActivity {
    private String userGroupDocID;
    private String userDocId;
    private String groupDocID;
    private String memberDocId;
    private String memberEmail;
    private int maxMembers;
    private int groupSize;
    private String groupName;

    private EditText memberCandidateEditText;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private CollectionReference groupMemberRef;
    private DocumentReference groupDoc;
    private String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private Pattern emailPattern = Pattern.compile(emailRegex);
    private boolean closeDialog = false, memberExists = false;
    private ColorDrawable swipeBackground = new ColorDrawable(Color.parseColor("#FF0000"));
    private Drawable deleteIcon;

    private int removedPosition;
    private int removedMember;


    private AlertDialog addMemberDialog;
    private MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_list);

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete);

        Intent intent = getIntent();
        userGroupDocID = intent.getStringExtra("userGroupDocId");
        userDocId = intent.getStringExtra("userDocId");
        Toast.makeText(this, "Keep value " + userGroupDocID, Toast.LENGTH_SHORT).show();
        groupDocID = intent.getStringExtra("groupDocId");

        groupDoc = db.collection("groups").document(groupDocID);

        groupMemberRef = db.collection("groups").document(groupDocID)
                .collection("members");

        // Get the group size
        groupMemberRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                groupSize = queryDocumentSnapshots.size();
            }
        });


        groupDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Group group = documentSnapshot.toObject(Group.class);
                        maxMembers = group.getMaxGroupMembers();
                        groupName = group.getGroupName();
                    }
                });

        setUpRecyclerView();


        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getMemberDocId(String email) {
        Query query = usersRef.whereEqualTo("email", email);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        memberDocId = document.getId();
                        nextPhase();
                        break;
                    }
                }
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = groupMemberRef.orderBy("firstName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();

        adapter = new MemberAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.member_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                if(dX > 0) {
                    swipeBackground.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                    deleteIcon.setBounds(itemView.getLeft() + iconMargin, itemView.getTop() + iconMargin,
                            itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth(),
                            itemView.getBottom() - iconMargin);

                } else {
                    swipeBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    deleteIcon.setBounds(itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth(), itemView.getTop() + iconMargin,
                            itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin);
                }

                swipeBackground.draw(c);

                c.save();

                if(dX >0)
                    c.clipRect(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                else
                    c.clipRect(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteIcon.draw(c);

                c.restore();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Are you sure you would like to delete this member?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int position = viewHolder.getAdapterPosition();

                                db.collection("groups").document(groupDocID)
                                        .collection("members").document(adapter.getDocId(position))
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if(e != null) {
                                                    Toast.makeText(GroupMemberList.this, "Error while loading!", Toast.LENGTH_SHORT).show();

                                                }

                                                if(documentSnapshot.exists())
                                                {
                                                   final Member member = documentSnapshot.toObject(Member.class);
                                                    if(!member.getIsOwner()) {
                                                        adapter.deleteItem(position);

                                                        Query findMemberToDelete = db.collection("users").document(member.getUserDocId()).collection("groups").
                                                                whereEqualTo("groupName",groupName);

                                                        findMemberToDelete.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                String memberGroupDocId;

                                                                if(task.isSuccessful())
                                                                {
                                                                    for(QueryDocumentSnapshot document : task.getResult()){
                                                                        memberGroupDocId = document.getId();
                                                                        db.collection("users").document(member.getUserDocId())
                                                                                .collection("groups").document(memberGroupDocId).delete();
                                                                        break;
                                                                    }

                                                                }
                                                            }
                                                        });
                                                        Snackbar.make(viewHolder.itemView, member.getFirstName() + " " +
                                                                member.getLastName() + " has been removed!", Snackbar.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Snackbar.make(viewHolder.itemView, "Group owner cannot be removed from group!", Snackbar.LENGTH_LONG).show();
                                                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                                    }
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                .create()
                .show();
            }
        }).attachToRecyclerView(recyclerView);

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

        addMemberDialog = builder.create();
        addMemberDialog.show();

        addMemberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberExists = false;
                memberEmail = memberCandidateEditText.getText().toString();

                Matcher matcher = emailPattern.matcher(memberEmail.trim());

                if (!matcher.matches()) {
                    memberCandidateEditText.setError("Invalid email!");
                } else {

                    getMemberDocId(memberEmail);
                }
            }
        });
    }

    private void addGroupToUserProfile()
    {
        final CollectionReference newUserGroup = usersRef.document(memberDocId).collection("groups");

        groupDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Group groupData = documentSnapshot.toObject(Group.class);
                newUserGroup.add(groupData);
            }
        });
    }

    private void nextPhase() {
            usersRef.document(memberDocId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Profile tempProfile = documentSnapshot.toObject(Profile.class);

                            if (tempProfile != null) {
                                String fName = tempProfile.getFirstName();
                                String lName = tempProfile.getLastName();
                                String userName = getUsername(memberEmail);

                                final Member newMember = new Member(fName, lName, memberDocId, userGroupDocID,
                                        userName, false, Timestamp.now());

                                groupMemberRef.whereEqualTo("userDocId", memberDocId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (document != null) {
                                                            memberCandidateEditText.setError("Member is already in group!");
                                                            memberExists = true;
                                                            break;
                                                        }
                                                    }
                                                    // Only add a new member once we ensure a member does not already exist
                                                    if (!memberExists) {
                                                        if(groupSize + 1 <= maxMembers )
                                                        {
                                                            groupMemberRef.add(newMember);
                                                            addGroupToUserProfile();
                                                            closeDialog = true;
                                                            addMemberDialog.dismiss();

                                                        } else {
                                                            memberCandidateEditText.setError("The group is full!");
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                            else {
                                memberCandidateEditText.setError("Email does not exist within system");
                            }
                        }
                    });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
        in.putExtra("groupDocId", groupDocID);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

    private String getUsername(String email) {
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }
}
