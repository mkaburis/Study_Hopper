package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.profile.classListItem;

public class StudyGroupFinderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseFirestore db;
    private String TAG;
    private GroupSearchAdapter mAdapter;
    private ArrayList<classListItem> groupList;

    private Spinner ageRangeSpinner;
    private Spinner locationSpinner;
    private Spinner groupSizeSpinner;
    private Spinner genderSpinner;

    private EditText subjectSearch;
    private EditText courseNumberSearch;
    private Button searchButton;

    public static StudyGroupFinderFragment newInstance() {
        return new StudyGroupFinderFragment();
    }

    public static StudyGroupFinderFragment newInstance(int index) {
        StudyGroupFinderFragment fragment = new StudyGroupFinderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        View v = inflater.inflate(R.layout.fragment_study_group_finder, container, false);
        TextView noResultsText = v.getRootView().findViewById(R.id.NoResultsText);
        RecyclerView resultViewer = v.getRootView().findViewById(R.id.ResultRecycleViewer);

        ageRangeSpinner = v.getRootView().findViewById(R.id.AgeRangeSpinner);
        locationSpinner = v.getRootView().findViewById(R.id.LocationSpinner);
        groupSizeSpinner = v.getRootView().findViewById(R.id.SizeSpinner);
        genderSpinner = v.getRootView().findViewById(R.id.GenderSpinner);
        fillGroupPreferencesSpinners();

        courseNumberSearch = v.getRootView().findViewById(R.id.courseNumberText);
        subjectSearch = v.getRootView().findViewById(R.id.courseSubjectText);
        searchButton = v.getRootView().findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroupsFromFirebase();
            }
        });

        if (resultViewer.getChildCount() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void fillGroupPreferencesSpinners() {
        ArrayAdapter<CharSequence> ageRangeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.age_range_preference, android.R.layout.simple_spinner_item);
        ageRangeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ageRangeSpinner.setAdapter(ageRangeAdapter);
        //groupPreferencesSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.group_preferences, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.location_preference, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        ArrayAdapter<CharSequence> groupSizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.group_size_preference, android.R.layout.simple_spinner_item);
        groupSizeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        groupSizeSpinner.setAdapter(groupSizeAdapter);
    }

    private void getGroupsFromFirebase() {
        CollectionReference groupsRef = db.collection("groups");


        groupsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map group = document.getData();


                        //reloadRecycler();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void reloadRecycler() {
        mAdapter = new GroupSearchAdapter(new ArrayList<String>());
        //mClassRecycleView.setAdapter(mAdapter);
    }

}
