package study_dev.testbed.studyhopper.ui.groupFinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.ui.studyGroup.GroupViewer;

public class GroupSearchAdapter extends RecyclerView.Adapter<GroupSearchAdapter.GroupSearchViewHolder> {
    //groupListItem
    private ArrayList<groupListItem> mGroupList;


    public GroupSearchAdapter(ArrayList<groupListItem> groupList) {
        mGroupList = groupList;
    }

    @NonNull
    @Override
    public GroupSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        GroupSearchViewHolder groupListViewHolder = new GroupSearchViewHolder(v);
        return groupListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupSearchViewHolder holder, int position) {
        final groupListItem currentItem = mGroupList.get(position);

        holder.textViewGroupName.setText(currentItem.getGroupName());
        holder.textViewCourseCode.setText(currentItem.getCourseCode());
        holder.imageViewGroupColor.setImageResource(findGroupColorId(currentItem.getColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();

                Intent intent = new Intent(context, GroupViewer.class);
                intent.putExtra("groupId", currentItem.getGroupID());
                intent.putExtra("primary-user-Id", currentItem.getPrimaryId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    private int findGroupColorId(String color) {

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Blue", Integer.valueOf(R.drawable.ic_group_color_blue));
        colorMap.put("Green", Integer.valueOf(R.drawable.ic_group_color_green));
        colorMap.put("Yellow", Integer.valueOf(R.drawable.ic_group_color_yellow));
        colorMap.put("Red", Integer.valueOf(R.drawable.ic_group_color_red));
        colorMap.put("Purple", Integer.valueOf(R.drawable.ic_group_color_purple));
        colorMap.put("Orange", Integer.valueOf(R.drawable.ic_group_color_orange));
        colorMap.put("Brown", Integer.valueOf(R.drawable.ic_group_color_brown));
        colorMap.put("Gray", Integer.valueOf(R.drawable.ic_group_color_gray));

        return colorMap.get(color);
    }

    public static class GroupSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewGroupName;
        public TextView textViewCourseCode;
        public ImageView imageViewGroupColor;

        public GroupSearchViewHolder(@NonNull final View itemView) {
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
