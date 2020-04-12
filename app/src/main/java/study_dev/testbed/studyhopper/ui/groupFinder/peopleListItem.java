package study_dev.testbed.studyhopper.ui.groupFinder;

import android.os.Parcel;
import android.os.Parcelable;

public class peopleListItem implements Parcelable {
    public static final Creator<peopleListItem> CREATOR = new Creator<peopleListItem>() {
        @Override
        public peopleListItem createFromParcel(Parcel in) {
            return new peopleListItem(in);
        }

        @Override
        public peopleListItem[] newArray(int size) {
            return new peopleListItem[size];
        }
    };
    private String peopleName;
    private String primaryMajor;
    private String gender;
    private String userID;

    public peopleListItem(String peopleName, String primaryMajor, String gender, String id) {
        this.peopleName = peopleName;
        this.primaryMajor = primaryMajor;
        this.gender = gender;
        this.userID = id;
    }

    protected peopleListItem(Parcel in) {
        this.peopleName = in.readString();
        this.gender = in.readString();
        this.primaryMajor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(peopleName);
        dest.writeString(gender);
        dest.writeString(primaryMajor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPrimaryMajor() {
        return primaryMajor;
    }

    public void setPrimaryMajor(String primaryMajor) {
        this.primaryMajor = primaryMajor;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
