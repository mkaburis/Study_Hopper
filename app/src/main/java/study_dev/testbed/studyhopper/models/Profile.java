package study_dev.testbed.studyhopper.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Profile {
    String firstName;
    String lastName;

    String email;

    public Profile(String firstName, String lastName, Date dob, String gender, String university, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.university = university;
        this.email = email;
    }

    public Profile(String firstName, String lastName, Date dob, String gender, String university, String email, Timestamp lastLogin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.university = university;
        this.email = email;
        this.lastLogin = lastLogin;
    }

    Date dob;
    String gender;
    String university;
    Timestamp lastLogin;

    Profile() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
