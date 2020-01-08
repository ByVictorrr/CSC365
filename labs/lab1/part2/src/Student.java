
class Student{
    public String stLastName, stFirstName;
    public int grade, classRoom, busRoute;
    public float gpa;


    public Student(String stLastName, String stFirstName, int grade, int classRoom, int busRoute, float gpa) {
        this.stLastName = stLastName;
        this.stFirstName = stFirstName;
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
                + busRoute;

    }

}



