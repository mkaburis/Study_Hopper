package study_dev.testbed.studyhopper.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import study_dev.testbed.studyhopper.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileClasses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileClasses extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView mClassRecycleView;
    private TextInputEditText mCourseName;
    private TextInputEditText mSubject;
    private TextInputEditText mNumber;
    private TextInputEditText mSection;
    private TextInputEditText mSemester;
    private TextInputEditText mYear;
    private Button mAddButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public ProfileClasses() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileClasses newInstance(int index) {
        ProfileClasses fragment = new ProfileClasses();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_classes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        mClassRecycleView = view.findViewById(R.id.classRecycleViewer);
        mAddButton = view.findViewById(R.id.addCourseButton);

        mCourseName = view.findViewById(R.id.courseNameText);
        mSubject = view.findViewById(R.id.courseSubjectText);
        mNumber = view.findViewById(R.id.courseNumberText);
        mSection = view.findViewById(R.id.courseSectionText);
        mYear = view.findViewById(R.id.courseYearText);
        mSemester = view.findViewById(R.id.courseSemesterText);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Class Saved.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
