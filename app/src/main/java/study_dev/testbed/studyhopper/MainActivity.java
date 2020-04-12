package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else {
            Intent intent = new Intent(this, GroupViewer.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

    }
}
