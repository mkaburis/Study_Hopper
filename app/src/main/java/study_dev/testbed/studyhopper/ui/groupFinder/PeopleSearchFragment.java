package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Profile;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeopleSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseFirestore db;
    private ArrayList<peopleListItem> peopleList;
    private RecyclerView mPeopleRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PeopleSearchAdapter mAdapter;

    private TextView noResultsText;
    private EditText collegeText;
    private EditText majorText;
    private Spinner ageRangeSpinner;
    private Spinner genderSpinner;
    private Button searchButton;

    private String userUniversity;


    public PeopleSearchFragment() {
        // Required empty public constructor
    }

    public static PeopleSearchFragment newInstance() {
        return new PeopleSearchFragment();
    }

    public static PeopleSearchFragment newInstance(int index) {
        PeopleSearchFragment fragment = new PeopleSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        peopleList = new ArrayList<>();

        Bundle extras = getActivity().getIntent().getExtras();
        String firestoreId = extras.getString("firestore-id");

        db.collection("users").document(firestoreId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    userUniversity = task.getResult().getString("university");
                }
            }
        });


        View v = inflater.inflate(R.layout.fragment_people_search, container, false);
        noResultsText = v.getRootView().findViewById(R.id.NoResultsText);

        mPeopleRecycleView = v.getRootView().findViewById(R.id.ResultRecycleViewer);
        mPeopleRecycleView.setHasFixedSize(false); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PeopleSearchAdapter(peopleList);
        mPeopleRecycleView.setLayoutManager(mLayoutManager);
        mPeopleRecycleView.setAdapter(mAdapter);

        ageRangeSpinner = v.getRootView().findViewById(R.id.AgeSpinner);
        genderSpinner = v.getRootView().findViewById(R.id.GenderSpinner);

        fillGroupPreferencesSpinners();

        collegeText = v.getRootView().findViewById(R.id.CollegeEditText);
        majorText = v.getRootView().findViewById(R.id.MajorEditText);
        searchButton = v.getRootView().findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPeople();
            }
        });

        if (mPeopleRecycleView.getChildCount() == 0) {
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
                R.array.gender_preferences, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
    }

    public void searchPeople() {
        CollectionReference usersRef = db.collection("users");
        Query query = buildQuery(usersRef);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    peopleList.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Profile group = document.toObject(Profile.class);

                        String name = group.getFirstName() + " " + group.getLastName();
                        peopleListItem listItem = new peopleListItem(name,
                                group.getEmail(),
                                group.getGender());

                        peopleList.add(listItem);
                    }
                    reloadRecycler();
                }
            }
        });
    }

    private Query buildQuery(CollectionReference collectionReference) {
        Query groupQuery = collectionReference.whereEqualTo("university", userUniversity);

        String major = majorText.getText().toString();
        String college = collegeText.getText().toString();
        String ageRange = ageRangeSpinner.getSelectedItem().toString();
        String genderSpecific = genderSpinner.getSelectedItem().toString();

        if (!major.isEmpty()) {
            groupQuery = groupQuery.whereEqualTo("major", major);
        }
        if (!college.isEmpty()) {
            groupQuery = groupQuery.whereEqualTo("college", college);
        }
        if (!ageRange.isEmpty()) {
            int lowerBoundInt;
            int upperBoundInt;

            if (ageRange.equals("40+")) {
                lowerBoundInt = 40;
                upperBoundInt = 200;
            } else {
                String[] splitString = ageRange.split(" - ");
                lowerBoundInt = Integer.parseInt(splitString[0]);
                upperBoundInt = Integer.parseInt(splitString[1]);
            }

            Calendar calLower = Calendar.getInstance();
            Calendar calUpper = Calendar.getInstance();
            calLower.add(Calendar.YEAR, -lowerBoundInt);
            calUpper.add(Calendar.YEAR, -upperBoundInt);

            Date upperBound = calUpper.getTime();
            Date lowerBound = calLower.getTime();


            groupQuery = groupQuery.whereGreaterThan("dob", lowerBound).whereLessThan("dob", upperBound);
        }
        if (!genderSpecific.isEmpty()) {
            groupQuery = groupQuery.whereEqualTo("gender", genderSpecific);
        }

        return groupQuery;
    }

    private void reloadRecycler() {
        if (peopleList.size() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        } else {
            noResultsText.setVisibility(View.GONE);
            mAdapter = new PeopleSearchAdapter(peopleList);
            mPeopleRecycleView.setAdapter(mAdapter);
        }
    }
}
