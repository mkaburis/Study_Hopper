package study_dev.testbed.studyhopper.ui.profile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.studyGroupItem;

public class ClassesListAdapter extends RecyclerView.Adapter<ClassesListAdapter.ClassesListViewHolder> {

    private ArrayList<studyGroupItem> mClassList;

    @NonNull
    @Override
    public ClassesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    public static class ClassesListViewHolder extends RecyclerView.ViewHolder {
        public TextView mClassName;
        public TextView mClassSubject;
        public TextView mClassNumber;
        public TextView mClassSection;

        public ClassesListViewHolder(@NonNull View itemView) {
            super(itemView);
            // make card view for to make each class
        }
    }
}
