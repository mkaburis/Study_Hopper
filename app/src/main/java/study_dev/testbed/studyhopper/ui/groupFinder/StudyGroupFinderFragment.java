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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import study_dev.testbed.studyhopper.R;

public class StudyGroupFinderFragment extends Fragment {

    private StudyGroupFinderViewModel mViewModel;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Spinner ageRangeSpinner;
    private Spinner locationSpinner;
    private Spinner groupSizeSpinner;
    private Spinner genderSpinner;

    private EditText sTextInputubjectSearch;
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

        View v = inflater.inflate(R.layout.fragment_study_group_finder, container, false);
        TextView noResultsText = v.getRootView().findViewById(R.id.NoResultsText);
        RecyclerView resultViewer = v.getRootView().findViewById(R.id.ResultRecycleViewer);

        ageRangeSpinner = v.getRootView().findViewById(R.id.AgeRangeSpinner);
        locationSpinner = v.getRootView().findViewById(R.id.LocationSpinner);
        groupSizeSpinner = v.getRootView().findViewById(R.id.SizeSpinner);
        genderSpinner = v.getRootView().findViewById(R.id.GenderSpinner);
        fillGroupPreferencesSpinners();

        if (resultViewer.getChildCount() == 0) {
            noResultsText.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudyGroupFinderViewModel.class);
        // TODO: Use the ViewModel
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

}
