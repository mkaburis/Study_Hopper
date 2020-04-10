package study_dev.testbed.studyhopper.models;

import com.google.firebase.Timestamp;

public class Session {
    private String sessionName;
    private String sessionDescription;
    private String sessionLocation;
    private Timestamp sessionDate;
    private Timestamp sessionStartTime;
    private Timestamp sessionEndTime;
    private int sessionType;

    public Session() {
        //Empty constructor needed
    }

    public Session(String sessionName, String sessionDescription, String sessionLocation, Timestamp sessionDate,
                   Timestamp sessionStartTime, Timestamp sessionEndTime, int sessionType) {
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.sessionLocation = sessionLocation;
        this.sessionDate = sessionDate;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionType = sessionType;
    }

    public String getSessionLocation() {
        return sessionLocation;
    }

    public void setSessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public Timestamp getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Timestamp sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Timestamp getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(Timestamp sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public Timestamp getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(Timestamp sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }
}
