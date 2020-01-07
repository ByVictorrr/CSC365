
class Student{
    public String stLastName, stFirstName, tLastName, tFirstName;
    public int grade, classRoom, busRoute;
    public float gpa;


    public Student(String stLastName, String stFirstName, int grade, int classRoom, int busRoute, float gpa, String tLastName, String tFirstName ) {
        this.stLastName = stLastName;
        this.stFirstName = stFirstName;
        this.tLastName = tLastName;
        this.tFirstName = tFirstName;
        this.grade = grade;
        this.classRoom = classRoom;
        this.busRoute = busRoute;
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return stLastName + ","
                + stFirstName + ","
                + gpa + ","
                + tLastName + ","
                + tFirstName + ","
                + busRoute;

    }

}
