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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;

public class StudyGroupFinderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseFirestore db;
    private String TAG;
    private GroupSearchAdapter mAdapter;
    TextView noResultsText;
    private ArrayList<groupListItem> groupList;
    private RecyclerView mGroupRecycleView;

    private Spinner ageRangeSpinner;
    private Spinner locationSpinner;
    private Spinner groupSizeSpinner;
    private Spinner genderSpinner;

    private EditText subjectSearch;
    private EditText courseNumberSearch;
    private Button searchButton;
    private RecyclerView.LayoutManager mLayoutManager;

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
        groupList = new ArrayList<>();

        View v = inflater.inflate(R.layout.fragment_study_group_finder, container, false);
        noResultsText = v.getRootView().findViewById(R.id.NoResultsText);

        mGroupRecycleView = v.getRootView().findViewById(R.id.ResultRecycleViewer);
        mGroupRecycleView.setHasFixedSize(false); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new GroupSearchAdapter(groupList);
        mGroupRecycleView.setLayoutManager(mLayoutManager);
        mGroupRecycleView.setAdapter(mAdapter);


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

        if (mGroupRecycleView.getChildCount() == 0) {
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

        Query query = buildQuery(groupsRef);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    groupList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Group group = document.toObject(Group.class);

                        groupListItem listItem = new groupListItem(group.getGroupName(),
                                group.getCourseCode(),
                                group.getGroupColor());

                        groupList.add(listItem);
                    }
                    reloadRecycler();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void reloadRecycler() {
        if (groupList.size() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        } else {
            noResultsText.setVisibility(View.GONE);
            mAdapter = new GroupSearchAdapter(groupList);
            mGroupRecycleView.setAdapter(mAdapter);
        }
    }

    private Query buildQuery(CollectionReference groupsReference) {
        Query newQuery = groupsReference.whereEqualTo("isActive", true);

        String courseSubject = subjectSearch.getText().toString();
        String courseNumber = courseNumberSearch.getText().toString();
        String ageRange = ageRangeSpinner.getSelectedItem().toString();
        String meetingLocation = locationSpinner.getSelectedItem().toString();
        String groupSize = groupSizeSpinner.getSelectedItem().toString();
        String genderSpecific = genderSpinner.getSelectedItem().toString();

        if (!courseSubject.isEmpty() && !courseNumber.isEmpty()) {
            String subjectCode = courseSubject + " " + courseNumber;
            newQuery = newQuery.whereEqualTo("courseCode", subjectCode);
        }
        if (!ageRange.isEmpty()) {
            newQuery = newQuery.whereEqualTo("ageRange", ageRange);
        }
        if (!meetingLocation.isEmpty()) {
            newQuery = newQuery.whereEqualTo("meetingLocation", meetingLocation);
        }
        if (!groupSize.isEmpty()) {
            newQuery = newQuery.whereEqualTo("groupSize", groupSize);
        }
        if (!genderSpecific.isEmpty()) {
            String dbGender = "";
            if (genderSpecific.equals("Coed Group"))
                dbGender = "Coed";
            else if (genderSpecific.equals("Females Only Group"))
                dbGender = "Females Only";
            else if (genderSpecific.equals("Males Only Group"))
                dbGender = "Males Only";
            newQuery = newQuery.whereEqualTo("groupPreference", dbGender);
        }

        return newQuery;
    }

}
