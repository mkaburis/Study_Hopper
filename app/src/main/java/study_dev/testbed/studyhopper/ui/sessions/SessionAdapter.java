package study_dev.testbed.studyhopper.ui.sessions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import study_dev.testbed.studyhopper.R;
import study_dev.testbed.studyhopper.models.Session;

public class SessionAdapter extends FirestoreRecyclerAdapter<Session, SessionAdapter.SessionHolder> {
    private OnItemClickListener listener;

    public SessionAdapter(@NonNull FirestoreRecyclerOptions<Session> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SessionHolder holder, int position, @NonNull Session model) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        Timestamp startTime, endTime, sessionTimestamp;
        Date startTimeDate, endTimeDate, sessionDate;
        startTime = model.getSessionStartTime();
        startTimeDate = startTime.toDate();

        endTime = model.getSessionEndTime();
        endTimeDate = endTime.toDate();

        sessionTimestamp = model.getSessionDate();
        sessionDate = sessionTimestamp.toDate();
        String sessionMonth = monthFormat.format(sessionDate);
        String sessionDay = dayFormat.format(sessionDate);

        String timeStart = timeFormat.format(startTimeDate);
        String timeEnd = timeFormat.format(endTimeDate);


        holder.textViewSessionName.setText(model.getSessionName());
        holder.textViewSessionTime.setText(timeStart + " - " + timeEnd);
        holder.textViewSessionMonth.setText(sessionMonth);
        holder.textViewSessionDay.setText(sessionDay);

        int sessionPreference = model.getSessionType();

        switch (sessionPreference){
            case 0:
                holder.imageViewSessionType.setImageResource(R.drawable.ic_collaborative_session);
                break;
            case 1:
                holder.imageViewSessionType.setImageResource(R.drawable.ic_quiet_session);
                break;
            case 2:
                holder.imageViewSessionType.setImageResource(R.drawable.ic_review_session);
                break;
        }

    }

    @NonNull
    @Override
    public SessionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item,
                parent, false);

        return new SessionHolder(v);
    }

    class SessionHolder extends RecyclerView.ViewHolder {
        TextView textViewSessionName;
        TextView textViewSessionTime;
        TextView textViewSessionMonth;
        TextView textViewSessionDay;
        ImageView imageViewSessionType;

        public SessionHolder(@NonNull View itemView) {
            super(itemView);
            textViewSessionName = itemView.findViewById(R.id.text_view_session_name);
            textViewSessionTime = itemView.findViewById(R.id.text_view_session_time);
            textViewSessionMonth = itemView.findViewById(R.id.text_view_session_month);
            textViewSessionDay = itemView.findViewById(R.id.text_view_session_date);
            imageViewSessionType = itemView.findViewById(R.id.imageview_session_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;

    }
}
