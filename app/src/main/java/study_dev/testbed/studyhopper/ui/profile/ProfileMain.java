package study_dev.testbed.studyhopper.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import study_dev.testbed.studyhopper.Dashboard;
import study_dev.testbed.studyhopper.models.Major;
import study_dev.testbed.studyhopper.models.Profile;
import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.main.PageViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileMain extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;

    private String TAG;
    private TextInputEditText mFirstName;
    private TextInputEditText mLastName;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextInputEditText mDob;
    private TextInputEditText mGender;
    private TextInputEditText mUniversity;
    private TextInputEditText mCollege;
    private TextInputEditText mMajor;
    private Button mSaveButton;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean newProfile = false;

    public ProfileMain() {
        // Required empty public constructor
    }

    public static ProfileMain newInstance(int index) {
        ProfileMain fragment = new ProfileMain();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfilePage activity = (ProfilePage) getActivity();
        newProfile = activity.IsNewProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        mAuth = FirebaseAuth.getInstance();

        mFirstName = view.findViewById(R.id.fnameText);
        mLastName = view.findViewById(R.id.lnameText);
        mEmail = view.findViewById(R.id.emailText);
        mPassword = view.findViewById(R.id.passwordText);
        mDob = view.findViewById(R.id.dobText);
        mGender = view.findViewById(R.id.genderText);
        mUniversity = view.findViewById(R.id.universityText);
        mCollege = view.findViewById(R.id.collegeText);
        mMajor = view.findViewById(R.id.majorText);

        mSaveButton = view.findViewById(R.id.saveButton);
        db = FirebaseFirestore.getInstance();

        if (!newProfile) {
            fillInformation();
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newProfile) {
                    createAccount();
                } else {
                    updateAccount();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // registers account with database and calls method to store info in DB
    private void createAccount() {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((ProfilePage) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "User created successfully.",
                                    Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            try {
                                addAccount();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // adds new user's info to database
    private void addAccount() throws ParseException {
        Profile newProfile = getProfile();
        Major newMajor = getMajor();
        String docId = getUsername();

        saveNewProfileToFireBase(docId, newProfile, newMajor);
    }

    // overwrites existing user's info in database
    private void updateAccount() {
        String userName = getUsername();
        DocumentReference ref = db.collection("users").document(userName);

    }

    // fills text boxes with info from database
    private void fillInformation() {
        String userName = getUsername();
        DocumentReference profileRef = db.collection("users").document(userName);
        CollectionReference majorsRef = db.collection("users").document(userName).collection("majors");

        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile profile = documentSnapshot.toObject(Profile.class);
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                if (profile == null) {
                    Toast.makeText(getContext(), "Profile info not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                mFirstName.setText(profile.getFirstName());
                mLastName.setText(profile.getLastName());
                mDob.setText(dateFormat.format(profile.getDob()));
                mGender.setText(profile.getGender());
                mEmail.setText(email);
                mUniversity.setText(profile.getUniversity());


                TextInputLayout passwordLayout = getActivity().findViewById(R.id.passwordLayout);
                passwordLayout.setVisibility(View.GONE);
                mPassword.setVisibility(View.GONE);
                mEmail.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error loading profile info",
                        Toast.LENGTH_SHORT).show();
            }
        });

        majorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Major currMajor = document.toObject(Major.class);
                        mMajor.setText(currMajor.getMajor());
                        mCollege.setText(currMajor.getCollege());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error loading profile info",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getUsername(){
        FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();
        if (email == null){
            return null;
        }

        return email.split("@")[0];
    }

    private Major getMajor() {
        String collegeStr = mCollege.getText().toString();
        final String majorStr = mMajor.getText().toString();

        Major major = new Major(majorStr, collegeStr);

        return major;
    }

    private void saveNewProfileToFireBase(final String docId, Profile newProfile, final Major newMajor) {
        final CollectionReference userRef = db.collection("users");

        userRef.document(docId).set(newProfile).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                boolean done = true;
                userRef.document(docId).collection("majors").document(newMajor.getMajor()).set(newMajor).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "User information saved to database.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), Dashboard.class);
                        intent.putExtra("user-ids", user.getUid());
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error saving profile info to database",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error saving profile info to database",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Profile getProfile() {
        String fName = mFirstName.getText().toString();
        String lName = mLastName.getText().toString();
        String birthday = mDob.getText().toString();
        String gender = mGender.getText().toString();
        String universityStr = mUniversity.getText().toString();

        DateFormat df = DateFormat.getDateInstance();
        Date dob = new Date();

        return new Profile(fName, lName, dob, gender, universityStr);
    }
}
