package study_dev.testbed.studyhopper;

import android.widget.ImageView;
import android.widget.Switch;

public class Group {
    private String groupName;
    private String courseCode;
    private String groupColor;
    private String id;

    public Group() {
        // empty constructor needed
    }

    public Group(String groupName, String courseCode, String groupColor) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.groupColor = groupColor;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

