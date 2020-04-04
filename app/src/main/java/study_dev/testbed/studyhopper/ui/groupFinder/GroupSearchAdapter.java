package study_dev.testbed.studyhopper.ui.groupFinder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupSearchAdapter extends RecyclerView.Adapter<GroupSearchAdapter.GroupSearchViewHolder> {
    //groupListItem
    private ArrayList<String> mGroupList;
    private GroupSearchViewHolder.OnItemClickListener mListener;

    public GroupSearchAdapter(ArrayList<String> groupList) {
        mGroupList = groupList;
    }

    @NonNull
    @Override
    public GroupSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSearchViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public static class GroupSearchViewHolder extends RecyclerView.ViewHolder {
        public GroupSearchViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
