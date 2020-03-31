package study_dev.testbed.studyhopper.models;

public class StudentClass {
    String ClassName;
    String Subject;
    String Number;
    String Section;
    String Semester;
    int Year;

    public StudentClass() {

    }

    public StudentClass(String className, String subject, String number, String section) {
        ClassName = className;
        Subject = subject;
        Number = number;
        Section = section;
    }

    public StudentClass(String className, String subject, String number, String section, String semester, int year) {
        ClassName = className;
        Subject = subject;
        Number = number;
        Section = section;
        Semester = semester;
        Year = year;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

}
