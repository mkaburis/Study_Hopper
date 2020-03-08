package study_dev.testbed.studyhopper;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView welcomeMsg;

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

        //recyclerView = v.getRootView().findViewById(R.id.study_group_list);
        welcomeMsg = v.getRootView().findViewById(R.id.welcome_txt);
        mRecyclerView = v.getRootView().findViewById(R.id.study_group_recycler_view);
        mRecyclerView.setHasFixedSize(true); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new StudyGroupAdapter(studyGroupList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        welcomeMsg.setText("Welcome Jose-Pablo Mantilla");


        return v;

    }


}
