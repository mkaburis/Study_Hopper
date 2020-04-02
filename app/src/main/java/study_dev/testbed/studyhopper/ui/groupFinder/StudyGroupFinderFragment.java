package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import study_dev.testbed.studyhopper.R;

public class StudyGroupFinderFragment extends Fragment {

    private StudyGroupFinderViewModel mViewModel;

    public static StudyGroupFinderFragment newInstance() {
        return new StudyGroupFinderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.study_group_finder_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudyGroupFinderViewModel.class);
        // TODO: Use the ViewModel
    }

}
