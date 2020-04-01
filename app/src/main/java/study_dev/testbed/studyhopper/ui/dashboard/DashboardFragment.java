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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.StudyRoomReservations;
import study_dev.testbed.studyhopper.models.studyGroupItem;
import study_dev.testbed.studyhopper.ui.groupFinder.StudyGroupFinder;
import study_dev.testbed.studyhopper.ui.studyGroup.StudyGroupActivity;


public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerView;
    private StudyGroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView welcomeMsg;
    private CardView groupFinderCard;
    private CardView studyRoomReservationCard;

    private String userName, personaName;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("users").document(getUserName());

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final ArrayList<studyGroupItem> studyGroupList = new ArrayList<>();
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_purple, "Mobile Devices", "COP 4656"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Automata Fun", "COT 4210"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_green, "How to not go to Jail", "CIS 4250"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_purple, "Hendrix Fun", "COP 4970"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Hadoop & Big Data", "CIS 4930"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_green, "GRE Prep", "TestPrep"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_blue, "Test", "Test"));


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
        mRecyclerView = v.getRootView().findViewById(R.id.study_group_recycler_view);
        mRecyclerView.setHasFixedSize(true); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new StudyGroupAdapter(studyGroupList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new StudyGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // start the dashboard activity
                Intent intent = new Intent(getContext(), StudyGroupActivity.class);
                intent.putExtra("Study Group Item", studyGroupList.get(position));
                startActivity(intent);
            }
        });

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


    private String getUserName() {
        String email = mAuth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        return email.substring(0, parseIndex);
    }
}
