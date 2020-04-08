package study_dev.testbed.studyhopper.models;

import com.google.firebase.Timestamp;

public class Member {

    private String firstName;
    private String lastName;
    private String userDocId;
    private String userGroupDocId;
    private String screenName;
    private boolean isOwner;
    private Timestamp whenJoined;

    public Member(){
        // empty constructor needed
    }

    public Member(String firstName, String lastName, String userDocId, String userGroupDocId, String screenName,
                  boolean isOwner, Timestamp whenJoined) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userDocId = userDocId;
        this.userGroupDocId = userGroupDocId;
        this.screenName = screenName;
        this.isOwner = isOwner;
        this.whenJoined = whenJoined;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserDocId() {
        return userDocId;
    }

    public String getUserGroupDocId() {
        return userGroupDocId;
    }

    public String getScreenName(){
        return screenName;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public Timestamp getWhenJoined() {
        return whenJoined;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserDocId(String userDocId) {
        this.userDocId = userDocId;
    }

    public void setUserGroupDocId(String userGroupDocId) {
        this.userGroupDocId = userGroupDocId;
    }

    public void setScreenName(String screenName){ this.screenName = screenName; }

    public void setIsOwner(boolean owner) {
        isOwner = owner;
    }

    public void setWhenJoined(Timestamp whenJoined) {
        this.whenJoined = whenJoined;
    }
}
