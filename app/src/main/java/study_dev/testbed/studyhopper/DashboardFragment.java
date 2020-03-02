package study_dev.testbed.studyhopper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class DashboardFragment extends Fragment {

    TextView welcomeMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        welcomeMsg = v.getRootView().findViewById(R.id.welcome_txt);
        welcomeMsg.setText("Welcome JPapi");
        return v;


    }

}
