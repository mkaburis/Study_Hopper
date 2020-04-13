package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Parcel;
import android.os.Parcelable;

public class groupListItem implements Parcelable {
    public static final Creator<groupListItem> CREATOR = new Creator<groupListItem>() {
        @Override
        public groupListItem createFromParcel(Parcel in) {
            return new groupListItem(in);
        }

        @Override
        public groupListItem[] newArray(int size) {
            return new groupListItem[size];
        }
    };

    private String groupName;
    private String courseCode;
    private String color;
    private String groupID;
    private String primaryId;

    public groupListItem(String groupName, String courseCode, String color, String groupID, String primaryId) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.color = color;
        this.groupID = groupID;
        this.primaryId = primaryId;
    }

    protected groupListItem(Parcel in) {
        this.groupName = in.readString();
        this.courseCode = in.readString();
        this.color = in.readString();
        this.groupID = in.readString();
        this.primaryId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
        dest.writeString(courseCode);
        dest.writeString(color);
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
