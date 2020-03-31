package study_dev.testbed.studyhopper;

import android.widget.ImageView;
import android.widget.Switch;

public class Group {
    private String groupName;
    private String courseCode;
    private String groupColor;
    private boolean coedGroup, femalesOnlyGroup, malesOnlyGroup;
    private int maxGroupMembers;
    private String id;

    public Group() {
        // empty constructor needed
    }

    public Group(String groupName, String courseCode, String groupColor,
                 boolean coedGroup, boolean femalesOnlyGroup, boolean malesOnlyGroup,
                 int maxGroupMembers) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.groupColor = groupColor;
        this.coedGroup = coedGroup;
        this.femalesOnlyGroup = femalesOnlyGroup;
        this.malesOnlyGroup = malesOnlyGroup;
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

    public boolean isCoedGroup() {
        return coedGroup;
    }

    public boolean isFemalesOnlyGroup() {
        return femalesOnlyGroup;
    }

    public boolean isMalesOnlyGroup() {
        return malesOnlyGroup;
    }

    public int getMaxGroupMembers() {
        return maxGroupMembers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

