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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView welcomeMsg;
    private CardView groupFinderCard;
    private CardView studyRoomReservationCard;

    private FirebaseAuth mAuth;

    private String personaName = "Welcome Demo Demo";

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ArrayList<studyGroupItem> studyGroupList = new ArrayList<>();
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
            public void onClick(View v){
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

        // Retrieve user email and parse it
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        int parseIndex = email.indexOf('@');
        String userName = email.substring(0, parseIndex);



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String first_name = documentSnapshot.getString("first-name");
                        String last_name = documentSnapshot.getString("last-name");

                        personaName = "Welcome " + first_name + " " + last_name + "!";

                    }
                });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        welcomeMsg.setText(personaName);
        
        return v;

    }

}
