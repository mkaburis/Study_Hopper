package study_dev.testbed.studyhopper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudyGroupAdapter extends RecyclerView.Adapter<StudyGroupAdapter.StudyGroupViewHolder> {
    private ArrayList<studyGroupItem> mStudyGroupList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class StudyGroupViewHolder extends RecyclerView.ViewHolder {

        // Contains data from our group_card
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public StudyGroupViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.studyGroupName);
            mTextView2 = itemView.findViewById(R.id.courseName);
            mImageView = itemView.findViewById(R.id.studyGroupColor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public StudyGroupAdapter(ArrayList<studyGroupItem> studyGroupList) {
        mStudyGroupList = studyGroupList;
    }

    @NonNull
    @Override
    public StudyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        StudyGroupViewHolder sgvh = new StudyGroupViewHolder(v, mListener);
        return sgvh;
    }

    @Override
    public void onBindViewHolder(@NonNull StudyGroupViewHolder holder, int position) {
        studyGroupItem currentItem = mStudyGroupList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mImageView.setImageResource(currentItem.getImageResource());

    }

    @Override
    public int getItemCount() {
        return mStudyGroupList.size();
    }
}
