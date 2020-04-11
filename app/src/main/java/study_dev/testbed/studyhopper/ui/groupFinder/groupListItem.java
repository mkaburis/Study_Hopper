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

    public groupListItem(String groupName, String courseCode, String color) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.color = color;
    }

    protected groupListItem(Parcel in) {
        groupName = in.readString();
        courseCode = in.readString();
        color = in.readString();
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
}
