package study_dev.testbed.studyhopper.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Group;

public class GroupAdapter extends FirestoreRecyclerAdapter<Group, GroupAdapter.GroupHolder> {
    private OnItemClickListener listener;

    public GroupAdapter(@NonNull FirestoreRecyclerOptions<Group> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupHolder holder, int position, @NonNull Group model) {
        holder.textViewGroupName.setText(model.getGroupName());
        holder.textViewCourseCode.setText(model.getCourseCode());
        holder.imageViewGroupColor.setImageResource(findGroupColorId(model.getGroupColor()));
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card,
                parent, false);

        return new GroupHolder(v);
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        TextView textViewGroupName;
        TextView textViewCourseCode;
        ImageView imageViewGroupColor;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.text_view_group_name);
            textViewCourseCode = itemView.findViewById(R.id.text_view_course_code);
            imageViewGroupColor = itemView.findViewById(R.id.image_view_group_color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }



    private int findGroupColorId(String color){
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



}
