package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
        return inflater.inflate(R.layout.fragment_people_search, container, false);
    }
}
