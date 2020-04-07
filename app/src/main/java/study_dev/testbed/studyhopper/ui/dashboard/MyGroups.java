package study_dev.testbed.studyhopper.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;

public class MyGroups extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userEmail, userDocId;
    private CollectionReference userRef = db.collection("users");
    private CollectionReference groupRef;
    private CollectionReference ref = db.collection("groups");

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        // Enable back button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve user's email address from FirebaseAuth
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userEmail = user.getEmail();
            Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();
        }

        Query userQuery = userRef.whereEqualTo("email", userEmail);
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        userDocId = document.getId();
                        groupRef = db.collection("users")
                                .document(userDocId).collection("groups");

                        // Establishes the recycler view
                        setUpRecyclerView();

                        adapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                                String id = documentSnapshot.getId();
                                Toast.makeText(MyGroups.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MyGroups.this, StudyGroupActivity.class);
                                intent.putExtra("documentID", id);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        });
                    }
                }
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = groupRef.orderBy("groupName", Query.Direction.DESCENDING);

        query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            }
        });

        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();

        adapter = new GroupAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.group_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.startListening();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_groups_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group_option:
                Intent intent = new Intent(MyGroups.this, CreateGroup.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
