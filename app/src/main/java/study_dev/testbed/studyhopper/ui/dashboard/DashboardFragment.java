package study_dev.testbed.studyhopper.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.StudyRoomReservations;
import study_dev.testbed.studyhopper.models.Group;
import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;


public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private RecyclerView recyclerView;
    private GroupAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView welcomeMsg;
    private CardView groupFinderCard;
    private CardView studyRoomReservationCard;

    private String userName, personaName;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("users").document(getUserName());
    private CollectionReference groupRef = db.collection("users")
            .document(getUserName()).collection("groups");

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//        final ArrayList<studyGroupItem> studyGroupList = new ArrayList<>();
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_purple, "Mobile Devices", "COP 4656"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Automata Fun", "COT 4210"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_green, "How to not go to Jail", "CIS 4250"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_purple, "Hendrix Fun", "COP 4970"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Hadoop & Big Data", "CIS 4930"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_green, "GRE Prep", "TestPrep"));
//        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Test", "Test"));


        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        welcomeMsg = v.getRootView().findViewById(R.id.welcome_txt);

        groupFinderCard = v.getRootView().findViewById(R.id.studyGroupFinderCard);


        groupFinderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                Intent in = new Intent(getContext(), StudyGroupFinder.class);
                startActivity(in);
                activity.overridePendingTransition(0, 0);
            }
        });

        studyRoomReservationCard = v.getRootView().findViewById(R.id.studyRoomReservationsCard);
        studyRoomReservationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                Intent in = new Intent(getContext(), StudyRoomReservations.class);
                startActivity(in);
                activity.overridePendingTransition(0, 0);
            }
        });

        // Setup recycler view for study groups
        recyclerView = v.getRootView().findViewById(R.id.study_group_recycler_view);
        setUpRecyclerView();

        // Retrieve first and last name of user from the database
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
                if (documentSnapshot.exists()) {
                    String first_name = documentSnapshot.getString("firstName");
                    String last_name = documentSnapshot.getString("lastName");

                    personaName = "Welcome " + first_name + " " + last_name + "!";
                    welcomeMsg.setText(personaName);
                } else {
                    welcomeMsg.setText("Error!");
                }
            }
        });

        return v;

    }

    private void setUpRecyclerView() {
        Query query = groupRef.orderBy("groupName", Query.Direction.DESCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            }
        });

        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();

        adapter = new GroupAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    private String getUserName() {
        String email = mAuth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
