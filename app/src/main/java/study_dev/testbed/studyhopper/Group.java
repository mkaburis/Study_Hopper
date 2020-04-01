package study_dev.testbed.studyhopper;

import android.widget.ImageView;
import android.widget.Switch;

public class Group {
    private String groupName;
    private String courseCode;
    private String groupColor;
    private String groupPreference;
    private int maxGroupMembers;
    private String documentId;

    public Group() {
        // empty constructor needed
    }

    public Group(String groupName, String courseCode, String groupColor,
                 String groupPreference, int maxGroupMembers) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.groupColor = groupColor;
        this.groupPreference = groupPreference;
        this.maxGroupMembers = maxGroupMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getGroupColor() {
        return groupColor;
    }

    public String getGroupPreference() {
        return groupPreference;
    }

    public int getMaxGroupMembers() {
        return maxGroupMembers;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

