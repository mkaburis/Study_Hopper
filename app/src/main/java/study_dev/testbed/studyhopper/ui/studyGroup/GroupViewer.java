package study_dev.testbed.studyhopper.ui.studyGroup;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.models.Member;
import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;
import study_dev.testbed.studyhopper.ui.profile.ProfileViewer;

public class GroupViewer extends AppCompatActivity {

    TextView groupNameTextView;
    TextView courseCodeTextView;
    TextView maxSizeTextView;
    TextView genderTextView;
    TextView locationTextView;
    TextView ageRangeTextView;
    TextView noResultsText;

    Button requestJoinButton;
    RecyclerView memberRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MemberAdapter mAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference groupRef;
    private String primaryUserId;
    private String groupId;

    private ColorDrawable profileBackground = new ColorDrawable(Color.parseColor("#0000ff"));
    private Drawable profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_viewer);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        primaryUserId = intent.getStringExtra("primary-user-Id");

        groupRef = db.collection("groups").document(groupId);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("View Group Info");

        setUpComponents();
        fetchFirebaseInfo();
    }

    public void setUpComponents() {
        groupNameTextView = findViewById(R.id.groupNameTextView);
        courseCodeTextView = findViewById(R.id.courseCodeTextView);
        maxSizeTextView = findViewById(R.id.maxSizeTextView);
        genderTextView = findViewById(R.id.genderTextView);
        locationTextView = findViewById(R.id.locationTextView);
        ageRangeTextView = findViewById(R.id.ageRangeTextView);
        noResultsText = findViewById(R.id.noResultsText);
        memberRecycleView = findViewById(R.id.memberRecycleView);
        profileIcon = ContextCompat.getDrawable(this, R.drawable.ic_group_member);

        requestJoinButton = findViewById(R.id.requestJoinButton);
        // TODO add event listener

    }

    public void fetchFirebaseInfo() {
        groupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document == null) {
                        return;
                    }

                    Group group = document.toObject(Group.class);

                    if (group == null) {
                        return;
                    }

                    int maxSize = group.getMaxGroupMembers();

                    groupNameTextView.setText(group.getGroupName());
                    courseCodeTextView.setText(group.getCourseCode());
                    maxSizeTextView.setText(String.valueOf(maxSize));
                    genderTextView.setText(group.getGenderPreference());
                    locationTextView.setText(group.getLocationPreference());
                    ageRangeTextView.setText(group.getAgePreference());

                }
            }
        });

        CollectionReference groupMemberRef = groupRef
                .collection("members");

        // Get the group size
        groupMemberRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int groupSize = queryDocumentSnapshots.size();

                if (groupSize == 0) {
                    noResultsText.setVisibility(View.VISIBLE);
                } else {
                    noResultsText.setVisibility(View.GONE);
                }
            }
        });

        Query query = groupMemberRef.orderBy("firstName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();

        mAdapter = new MemberAdapter(options);
        memberRecycleView.setLayoutManager(new LinearLayoutManager(this));
        memberRecycleView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - profileIcon.getIntrinsicHeight()) / 2;

                if (dX > 0) {
                    profileBackground.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                    profileIcon.setBounds(itemView.getLeft() + iconMargin, itemView.getTop() + iconMargin,
                            itemView.getLeft() + iconMargin + profileIcon.getIntrinsicWidth(),
                            itemView.getBottom() - iconMargin);

                }

                profileBackground.draw(c);

                c.save();

                if (dX > 0)
                    c.clipRect(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                else
                    c.clipRect(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                profileIcon.draw(c);

                c.restore();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                Intent intent = new Intent(getApplicationContext(), ProfileViewer.class);
                intent.putExtra("primary-user-Id", primaryUserId);
                intent.putExtra("groupId", mAdapter.getGroupId(position));
                intent.putExtra("return-to", "GroupViewer");
                intent.putExtra("found-user-docId", mAdapter.getUserId(position));

                startActivity(intent);

                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(memberRecycleView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getBaseContext(), StudyGroupFinder.class);
        in.putExtra("firestore-id", primaryUserId);
        startActivity(in);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
