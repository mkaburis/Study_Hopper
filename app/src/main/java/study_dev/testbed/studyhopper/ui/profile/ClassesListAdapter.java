package study_dev.testbed.studyhopper.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;

public class ClassesListAdapter extends RecyclerView.Adapter<ClassesListAdapter.ClassesListViewHolder> {

    private ArrayList<classListItem> mClassList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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

    public ClassesListAdapter(ArrayList<classListItem> classList) {
        mClassList = classList;
    }

    @NonNull
    @Override
    public ClassesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        ClassesListViewHolder classListViewHolder = new ClassesListViewHolder(v, mListener);
        return classListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesListViewHolder holder, int position) {
        classListItem currentItem = mClassList.get(position);

        holder.mClassName.setText(currentItem.getClassName());
        holder.mClassSubject.setText(currentItem.getClassSubject());
        holder.mClassNumber.setText(currentItem.getClassNumber());
        holder.mClassSection.setText(currentItem.getClassSection());
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
}
