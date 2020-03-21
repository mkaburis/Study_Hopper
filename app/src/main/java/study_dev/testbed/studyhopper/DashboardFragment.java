package study_dev.testbed.studyhopper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.nfc.Tag;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerView;
    private StudyGroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView welcomeMsg;
    private CardView groupFinderCard;
    private CardView studyRoomReservationCard;

    private FirebaseAuth mAuth;

    private String userName;

    private String personaName;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final ArrayList<studyGroupItem> studyGroupList = new ArrayList<>();
        studyGroupList.add(new studyGroupItem(R.drawable.ic_study_group_color, "Mobile Devices", "COP 4656"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_2, "Automata Fun", "COT 4210"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_3, "How to not go to Jail", "CIS 4250"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_study_group_color, "Hendrix Fun", "COP 4970"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_2, "Hadoop & Big Data", "CIS 4930"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_3, "GRE Prep", "TestPrep"));
        studyGroupList.add(new studyGroupItem(R.drawable.ic_group_color_2, "Test", "Test"));


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

        // Retrieve user email and parse it
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        userName = email.substring(0, parseIndex);

        // Retreive first and last name of user from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String first_name = document.getString("first-name");
                        String last_name = document.getString("last-name");

                        personaName = "Welcome " + first_name + " " + last_name + "!";
                        welcomeMsg.setText(personaName);

                    } else {
                        String exception = task.getException().toString();
                        Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;

    }

}
