package study_dev.testbed.studyhopper;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Profile {
    String firstName;
    String lastName;
    Date dob;
    String gender;
    String university;
    Timestamp lastLogin;

    Profile() {

    }

    Profile(String firstName, String lastName, Date dob, String gender, String university, Timestamp lastLogin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.university = university;
        this.lastLogin = lastLogin;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

}
