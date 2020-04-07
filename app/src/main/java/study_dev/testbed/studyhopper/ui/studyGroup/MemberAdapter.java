package study_dev.testbed.studyhopper.ui.studyGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Member;

public class MemberAdapter extends FirestoreRecyclerAdapter<Member, MemberAdapter.MemberHolder> {

    public MemberAdapter(@NonNull FirestoreRecyclerOptions<Member> options) {
        super(options);
        // Do not delete
    }

    @Override
    protected void onBindViewHolder(@NonNull MemberHolder holder, int position, @NonNull Member model) {
        holder.textViewName.setText(model.getFirstName() + " " + model.getLastName());
        holder.screenName.setText(model.getScreenName());
        if(model.isOwner())
        {
            holder.imageViewGroupOwner.setImageResource(R.drawable.ic_group_leader);
        }
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item,
                parent, false);

        return new MemberHolder(v);
    }


    class MemberHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName;
        TextView screenName;
        ImageView imageViewGroupOwner;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.image_view_user_photo);
            textViewName = itemView.findViewById(R.id.text_view_username);
            screenName = itemView.findViewById(R.id.text_view_screen_name);
            imageViewGroupOwner = itemView.findViewById(R.id.image_view_group_owner);

        }
    }
}
