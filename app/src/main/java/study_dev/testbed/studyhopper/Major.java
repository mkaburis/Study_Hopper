package study_dev.testbed.studyhopper;

public class Major {
    String college;
    String major;

    Major() {

    }

    Major(String major, String college) {
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
