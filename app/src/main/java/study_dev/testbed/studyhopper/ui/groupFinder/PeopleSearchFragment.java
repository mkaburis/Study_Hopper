package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;


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

    private TextView noResultsText;
    private EditText collegeText;
    private EditText majorText;
    private Spinner ageRangeSpinner;
    private Spinner genderSpinner;
    private Button searchButton;


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


        View v = inflater.inflate(R.layout.fragment_people_search, container, false);
        noResultsText = v.getRootView().findViewById(R.id.NoResultsText);

        mPeopleRecycleView = v.getRootView().findViewById(R.id.ResultRecycleViewer);
        mPeopleRecycleView.setHasFixedSize(false); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());

        return v;
    }
}
