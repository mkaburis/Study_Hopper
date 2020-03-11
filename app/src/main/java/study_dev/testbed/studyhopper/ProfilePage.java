package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
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
import java.util.Date;

public class ProfilePage extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Bundle extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();

        mFirstName = findViewById(R.id.fnameText);
        mLastName = findViewById(R.id.lnameText);
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mDob = findViewById(R.id.dobText);
        mGender = findViewById(R.id.genderText);
        mUniversity = findViewById(R.id.universityText);
        mCollege = findViewById(R.id.collegeText);
        mMajor = findViewById(R.id.majorText);

        mSaveButton = findViewById(R.id.saveButton);
        db = FirebaseFirestore.getInstance();

        if (extras != null) {
            newProfile = extras.getBoolean("new-profile");
        }

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

    // registers account with database and calls method to store info in DB
    private void createAccount() {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if (email.isEmpty()) {
            Toast.makeText(ProfilePage.this, "Email cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(ProfilePage.this, "Password cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(ProfilePage.this, "User created successfully.",
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

                            Toast.makeText(ProfilePage.this, task.getException().getMessage(),
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
                    Toast.makeText(ProfilePage.this, "Profile info not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirstName.setText(profile.firstName);
                mLastName.setText(profile.lastName);
                mDob.setText(profile.dob.toString());
                mGender.setText(profile.gender);
                mEmail.setText(email);
                mUniversity.setText(profile.university);


                TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
                passwordLayout.setVisibility(View.GONE);
                mPassword.setVisibility(View.GONE);
                mEmail.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Error loading profile info",
                        Toast.LENGTH_SHORT).show();
            }
        });

        majorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Major currMajor = document.toObject(Major.class);
                        mMajor.setText(currMajor.major);
                        mCollege.setText(currMajor.college);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Error loading profile info",
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
        String userName = email.split("@")[0];

        return userName;
    }

    private Major getMajor(){
        String collegeStr = mCollege.getText().toString();
        final String majorStr = mMajor.getText().toString();

        Major major = new Major();
        major.college = collegeStr;
        major.major = majorStr;

        return major;
    }

    private void saveNewProfileToFireBase(final String docId, Profile newProfile, final Major newMajor) {
        final CollectionReference userRef = db.collection("users");

        userRef.document(docId).set(newProfile).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                boolean done = true;
                userRef.document(docId).collection("majors").document().set(newMajor).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProfilePage.this, "User information saved to database.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfilePage.this, Dashboard.class);
                        intent.putExtra("user-ids", user.getUid());
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePage.this, "Error saving profile info to database",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Error saving profile info to database",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Profile getProfile() throws ParseException {
        String fName = mFirstName.getText().toString();
        String lName = mLastName.getText().toString();
        String birthday = mDob.getText().toString();
        String gender = mGender.getText().toString();
        String universityStr = mUniversity.getText().toString();

        DateFormat df = DateFormat.getDateInstance();
        Date dob = new Date();

        Profile profile = new Profile();
        profile.firstName = fName;
        profile.lastName = lName;
        profile.dob = dob;
        profile.gender = gender;
        profile.university = universityStr;

        return profile;
    }

    private class Profile {
        String firstName;
        String lastName;
        Date dob;
        String gender;
        String university;
        Timestamp lastLogin;

        Profile() {
        }

        Profile(String firstName, String lastName, Date dob, String gender, String university, Timestamp lastLogin) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.gender = gender;
            this.university = university;
            this.lastLogin = lastLogin;

        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getDob() {
            return dob;
        }

        public void setDob(Date dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUniversity() {
            return university;
        }

        public void setUniversity(String university) {
            this.university = university;
        }

        public Timestamp getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Timestamp lastLogin) {
            this.lastLogin = lastLogin;
        }

    }

    private class Major{
        String college;
        String major;

        Major(){}

        Major(String major, String college) {
            this.major = major;
            this.college = college;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }
    }

    private class StudentClass {

    }

}
