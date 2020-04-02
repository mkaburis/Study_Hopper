package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

}
