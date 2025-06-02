@SuppressWarnings("deprecation")

public class CourseModel extends java.util.Observable {
    private int CourseId;
    private String CoursePath;
    private String content;

    public CourseModel(int CourseId, String CoursePath, String content) {
        this.CourseId = CourseId;
        this.CoursePath = CoursePath;
        this.content = content;
    }

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int Id) {
        this.CourseId = Id;
    }

    public String getCoursePath() {
        return CoursePath;
    }

    public void setCoursePath(String CoursePath) {
        this.CoursePath = CoursePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}