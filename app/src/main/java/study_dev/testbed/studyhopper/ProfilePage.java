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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                            addAccount();

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
    private void addAccount() {
        String fName = mFirstName.getText().toString();
        String lName = mLastName.getText().toString();
        String birthday = mDob.getText().toString();
        String gender = mGender.getText().toString();
        String email = mEmail.getText().toString();

        Timestamp dob = new Timestamp(new Date(birthday));

        String university = mUniversity.getText().toString();
        String college = mCollege.getText().toString();
        final String major = mMajor.getText().toString();

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("first-name", fName);
        newUser.put("last-name", lName);
        newUser.put("dob", dob);
        newUser.put("gender", gender);
        newUser.put("university", university);
        newUser.put("college", college);
        newUser.put("major", major);
        newUser.put("last-login", FieldValue.serverTimestamp());

        String docId = email.toLowerCase().split("@")[0];

        db.collection("users").document(docId).set(newUser)
            .addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ProfilePage.this, "User information saved to database.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfilePage.this, Dashboard.class);
                    intent.putExtra("user-ids", user.getUid());
                    startActivity(intent);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilePage.this, "Error saving profile info to database",
                            Toast.LENGTH_SHORT).show();
                }
            });

    }

    // overwrites existing user's info in database
    private void updateAccount() {
        String userName = getUsername();

        DocumentReference ref = db.collection("users").document(userName);



    }
    // fills text boxes with info from database
    private void fillInformation() {
        String userName = getUsername();

        DocumentReference ref = db.collection("users").document(userName);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            Map x = documentSnapshot.getData();
            String fname = x.get("first-name").toString();
            String lname = x.get("last-name").toString();
            String dob = x.get("dob").toString();
            String gender = x.get("gender").toString();
            String major = x.get("major").toString();
            String college = x.get("college").toString();
            String uni = x.get("university").toString();
            String email = mAuth.getCurrentUser().getEmail();

            mFirstName.setText(fname);
            mLastName.setText(lname);
            mDob.setText(dob);
            mGender.setText(gender);
            mEmail.setText(email);
            mMajor.setText(major);
            mCollege.setText(college);
            mUniversity.setText(uni);

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

}
