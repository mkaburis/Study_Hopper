package study_dev.testbed.studyhopper.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.StudentClass;


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
    private ClassesListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String TAG;
    private TextInputEditText mCourseName;
    private TextInputEditText mSubject;
    private TextInputEditText mNumber;
    private TextInputEditText mSection;
    private TextInputEditText mSemester;
    private TextInputEditText mYear;
    private Button mAddButton;

    private ArrayList<classListItem> classList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference globalUserRef;


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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        classList = new ArrayList<>();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isAnonymous()) {
            fetchClassesFromDB();
        }
        String email = getEmail();
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        globalUserRef = doc.getReference();
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_classes, container, false);

        mClassRecycleView = v.getRootView().findViewById(R.id.classRecycleViewer);
        mClassRecycleView.setHasFixedSize(false); //ONLY for FIXED Size recylcerView remove later
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ClassesListAdapter(classList, this);
        mClassRecycleView.setLayoutManager(mLayoutManager);
        mClassRecycleView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
                String name = mCourseName.getText().toString();
                String subject = mSubject.getText().toString();
                String number = mNumber.getText().toString();
                String section = mSection.getText().toString();

                boolean isValidated = true;

                if (validateText(name)) {
                    mCourseName.setError("Course Name cannot be empty");
                    isValidated = false;
                }
                if (validateText(subject)) {
                    mSubject.setError("Course Name cannot be empty");
                    isValidated = false;
                }
                if (validateText(number)) {
                    mNumber.setError("Course Name cannot be empty");
                    isValidated = false;
                }

                if (isValidated) {
                    StudentClass newClass = new StudentClass(name, subject, number, section);
                    addClassToFireBase(newClass);
                }
            }
        });
    }

    private void fetchClassesFromDB() {

        CollectionReference classRef = globalUserRef.collection("classes");
        classList.clear();
        classRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        StudentClass sClass = document.toObject(StudentClass.class);

                        classListItem listItem = new classListItem(document.getId(), sClass.getClassName(), sClass.getSubject(), sClass.getNumber(), sClass.getSection());
                        classList.add(listItem);
                        reloadRecycler();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error pulling classes from database",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addClassToFireBase(StudentClass newClass) {
        CollectionReference classRef = globalUserRef.collection("classes");

        classRef.document().set(newClass).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Class added to db", Toast.LENGTH_SHORT).show();
                clearNewClassCard();
                fetchClassesFromDB();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error saving profile info to database",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateText(String text) {
        return text != null && text.isEmpty();
    }

    private String getEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            return null;
        }

        return user.getEmail();
    }

    private String getUsername() {
        FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();
        if (email == null) {
            return null;
        }

        return email.split("@")[0];
    }

    private void reloadRecycler() {
        mAdapter = new ClassesListAdapter(classList, this);
        mClassRecycleView.setAdapter(mAdapter);
    }

    private void clearNewClassCard() {
        mCourseName.setText("");
        mSubject.setText("");
        mNumber.setText("");
        mSection.setText("");
        mSemester.setText("");
        mYear.setText("");
    }

    public void deleteClassFromFirebase(String id) {
        DocumentReference classRef = globalUserRef.collection("classes").document(id);

        classRef.delete();

        fetchClassesFromDB();
    }
}
