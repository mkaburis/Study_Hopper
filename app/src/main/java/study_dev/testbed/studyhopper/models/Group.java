package study_dev.testbed.studyhopper.models;

import com.google.firebase.firestore.DocumentReference;

public class Group {
    private String groupName;
    private String courseSubject;
    private String courseNumber;
    private String groupColor;
    private String genderPreference;
    private String groupOwner;
    private int maxGroupMembers;
    private DocumentReference documentId;
    private String locationPreference;
    private String agePreference;
    private String university;

    public Group() {
        // empty constructor needed
    }

    public Group(String groupName, String courseSubject, String courseNumber, String groupColor,
                 String genderPreference, String groupOwner, int maxGroupMembers,
                 DocumentReference documentId, String university, String locationPreference,
                 String agePreference) {
        this.groupName = groupName;
        this.courseSubject = courseSubject;
        this.courseNumber = courseNumber;
        this.groupColor = groupColor;
        this.genderPreference = genderPreference;
        this.groupOwner = groupOwner;
        this.maxGroupMembers = maxGroupMembers;
        this.documentId = documentId;
        this.locationPreference = locationPreference;
        this.agePreference = agePreference;
        this.university = university;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupColor() {
        return groupColor;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public int getMaxGroupMembers() {
        return maxGroupMembers;
    }

    public DocumentReference getDocumentId() {
        return documentId;
    }

    public void setDocumentId(DocumentReference documentId) {
        this.documentId = documentId;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String getAgePreference() {
        return agePreference;
    }

    public void setAgePreference(String agePreference) {
        this.agePreference = agePreference;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCourseSubject() {
        return courseSubject;
    }

    public void setCourseSubject(String courseSubject) {
        this.courseSubject = courseSubject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setGroupColor(String groupColor) {
        this.groupColor = groupColor;
    }

    public void setMaxGroupMembers(int maxGroupMembers) {
        this.maxGroupMembers = maxGroupMembers;
    }

    public String getCourseCode() {
        return courseSubject + " " + courseNumber;
    }
}

