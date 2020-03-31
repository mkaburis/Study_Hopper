package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mForgotPasswordButton;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.username);
        mPasswordField = findViewById(R.id.password);

        mLoginButton = findViewById(R.id.login);
        mRegisterButton = findViewById(R.id.register);
        mForgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailField.getText().toString();
                final String password = mPasswordField.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(Login.this, "Email cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Password cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                signIn(mAuth, email, password);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ProfilePage.class);
                intent.putExtra("new-profile", true);
                startActivity(intent);
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailField.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(Login.this, "Email cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                resetPassword(email);
            }
        });
    }


    private void signIn(final FirebaseAuth mAuth, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            Toast.makeText(Login.this, "Logged in successfully.",
                                    Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();

                            // add an update to last-login in firebase


                            Intent intent = new Intent(Login.this, Dashboard.class);
                            intent.putExtra("user-ids", user.getUid());
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(Login.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount(final FirebaseAuth mAuth, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();

                            Intent intent = new Intent(Login.this, ProfilePage.class);
                            intent.putExtra("new-profile", true);

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Error creating user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Email Sent.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Email not found.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
