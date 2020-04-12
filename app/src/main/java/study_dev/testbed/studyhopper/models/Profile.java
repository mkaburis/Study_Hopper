package study_dev.testbed.studyhopper.models;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class Profile {
    String firstName;
    String lastName;
    Date dob;
    String gender;
    String university;
    String email;
    String major;
    String college;

    public Profile(String firstName, String lastName, Date dob, String gender, String university, String email, String major, String college) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.university = university;
        this.email = email;
        this.major = major;
        this.college = college;
    }

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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAge() {

        Instant instant = Instant.ofEpochMilli(dob.getTime());
        LocalDate birthday = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now();
        Period p = Period.between(birthday, today);

        return "" + p.getYears();
    }

}
