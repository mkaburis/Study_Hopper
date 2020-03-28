package study_dev.testbed.studyhopper.ui.profile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.studyGroupItem;

public class ClassesListAdapter extends RecyclerView.Adapter<ClassesListAdapter.ClassesListViewHolder> {

    private ArrayList<studyGroupItem> mClassList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

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
        public ImageButton mDeleteClassButton;

        public ClassesListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            // make card view for to make each class
            mClassName = itemView.findViewById(R.id.courseName);
            mClassSubject = itemView.findViewById(R.id.courseSubject);
            mClassNumber = itemView.findViewById(R.id.courseNumber);
            mClassSection = itemView.findViewById(R.id.courseSection);
            mDeleteClassButton = itemView.findViewById(R.id.deleteClassButton);

            // add onclick to delete class

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
