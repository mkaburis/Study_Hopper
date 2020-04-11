package study_dev.testbed.studyhopper.ui.groupFinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import study_dev.testbed.studyhopper.R;

public class PeopleSearchAdapter extends RecyclerView.Adapter<PeopleSearchAdapter.PeopleSearchViewHolder> {
    private ArrayList<peopleListItem> peopleList;

    public PeopleSearchAdapter(ArrayList<peopleListItem> peopleList) {
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public PeopleSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        PeopleSearchViewHolder groupListViewHolder = new PeopleSearchViewHolder(v);
        return groupListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleSearchViewHolder holder, int position) {
        peopleListItem currentItem = peopleList.get(position);

        holder.textViewGroupName.setText(currentItem.getPeopleName());
        holder.textViewCourseCode.setText(currentItem.getPrimaryMajor());
        holder.imageViewGroupColor.setImageResource(0);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public static class PeopleSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewGroupName;
        public TextView textViewCourseCode;
        public ImageView imageViewGroupColor;

        public PeopleSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.text_view_group_name);
            textViewCourseCode = itemView.findViewById(R.id.text_view_course_code);
            imageViewGroupColor = itemView.findViewById(R.id.image_view_group_color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
