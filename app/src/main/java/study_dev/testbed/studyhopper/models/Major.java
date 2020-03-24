package study_dev.testbed.studyhopper.models;

public class Major {
    String college;
    String major;

    Major() {

    }

    public Major(String major, String college) {
        this.major = major;
        this.college = college;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
