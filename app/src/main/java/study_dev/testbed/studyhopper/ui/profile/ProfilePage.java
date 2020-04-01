package study_dev.testbed.studyhopper.ui.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import study_dev.testbed.studyhopper.R;

public class ProfilePage extends AppCompatActivity {
    public boolean IsNewProfile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Bundle extras = getIntent().getExtras();
        if (extras!= null){
            IsNewProfile = extras.getBoolean("new-profile");
        }
        ViewPager viewPager = findViewById(R.id.view_pager_profile);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.profile_tabs);
        tabs.setupWithViewPager(viewPager);
    }



}

