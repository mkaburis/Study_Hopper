package study_dev.testbed.studyhopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import study_dev.testbed.studyhopper.ui.dashboard.DashboardFragment;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getApplicationContext(), "Unauthorized access", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        // Call our toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        View headerview = navigationView.getHeaderView(0);
        ImageView profileButton = headerview.findViewById(R.id.profile_image);

        LinearLayout header = headerview.findViewById(R.id.nav_header);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "profile image", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, ProfilePage.class);
                intent.putExtra("new-profile", false);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

        });
        toggle.syncState();

        if (savedInstanceState == null) {
            // Start activity in the dashboard fragment...
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dashboard_option);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Intent is used to switch between activities
        Intent in;
        switch (menuItem.getItemId()) {

            case R.id.dashboard_option:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new study_dev.testbed.studyhopper.DashboardFragment()).commit();
                break;
            case R.id.study_safari:
                in = new Intent(getBaseContext(), StudyGroupFinder.class);
                startActivity(in);
                overridePendingTransition(0, 0);
                break;
            case R.id.messages:
                in = new Intent(getBaseContext(), Messages.class);
                startActivity(in);
                overridePendingTransition(0, 0);
                break;

            case R.id.my_groups:
                in = new Intent(getBaseContext(), MyGroups.class);
                startActivity(in);
                overridePendingTransition(0, 0);
                break;

            case R.id.study_locations:
                in = new Intent(getBaseContext(), StudyLocationsMap.class);
                startActivity(in);
                overridePendingTransition(0, 0);
                break;

            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                logOut();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void logOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
