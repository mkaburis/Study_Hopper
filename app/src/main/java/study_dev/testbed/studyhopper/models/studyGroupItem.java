package study_dev.testbed.studyhopper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class studyGroupItem implements Parcelable {
    private int mImageResource;
    private String mText1;
    private String mText2;

    public studyGroupItem(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    protected studyGroupItem(Parcel in) {
        mImageResource = in.readInt();
        mText1 = in.readString();
        mText2 = in.readString();
    }

    public static final Creator<studyGroupItem> CREATOR = new Creator<studyGroupItem>() {
        @Override
        public studyGroupItem createFromParcel(Parcel in) {
            return new studyGroupItem(in);
        }

        @Override
        public studyGroupItem[] newArray(int size) {
            return new studyGroupItem[size];
        }
    };

    public void changeText1(String text){
        mText1 = text;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageResource);
        dest.writeString(mText1);
        dest.writeString(mText2);
    }
}
