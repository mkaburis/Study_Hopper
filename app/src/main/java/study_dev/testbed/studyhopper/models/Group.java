package study_dev.testbed.studyhopper.models;

import com.google.firebase.firestore.DocumentReference;

public class Group {
    private String groupName;
    private String courseCode;
    private String groupColor;
    private String groupPreference;
    private String groupOwner;
    private int maxGroupMembers;
    private DocumentReference documentId;

    public Group() {
        // empty constructor needed
    }

    public Group(String groupName, String courseCode, String groupColor,
                 String groupPreference, String groupOwner, int maxGroupMembers, DocumentReference documentId) {
        this.groupName = groupName;
        this.courseCode = courseCode;
        this.groupColor = groupColor;
        this.groupPreference = groupPreference;
        this.groupOwner = groupOwner;
        this.maxGroupMembers = maxGroupMembers;
        this.documentId = documentId;
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

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupPreference() {
        return groupPreference;
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
}

