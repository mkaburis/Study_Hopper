package study_dev.testbed.studyhopper.ui.profile;

import android.os.Parcel;
import android.os.Parcelable;

public class classListItem implements Parcelable {
    public static final Creator<classListItem> CREATOR = new Creator<classListItem>() {
        @Override
        public classListItem createFromParcel(Parcel in) {
            return new classListItem(in);
        }

        @Override
        public classListItem[] newArray(int size) {
            return new classListItem[size];
        }
    };
    private int mClassId;
    private String mClassName;
    private String mClassSubject;
    private String mClassNumber;
    private String mClassSection;

    public classListItem(int classId, String text1, String text2, String text3, String text4) {
        mClassId = classId;
        mClassName = text1;
        mClassSubject = text2;
        mClassNumber = text3;
        mClassSection = text4;
    }

    protected classListItem(Parcel in) {
        mClassId = in.readInt();
        mClassName = in.readString();
        mClassSubject = in.readString();
        mClassNumber = in.readString();
        mClassSection = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getClassId() {
        return mClassId;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getClassSubject() {
        return mClassSubject;
    }

    public String getClassNumber() {
        return mClassNumber;
    }

    public String getClassSection() {
        return mClassSection;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mClassId);
        dest.writeString(mClassName);
        dest.writeString(mClassSubject);
        dest.writeString(mClassNumber);
        dest.writeString(mClassSection);
    }
}
