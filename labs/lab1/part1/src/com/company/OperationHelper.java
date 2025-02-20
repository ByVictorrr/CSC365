
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

class OperationHelper {
    private static OperationHelper instance = null;
    private List<String> command_list;
    private static List<Student> students;

    private OperationHelper() {
    }

    public static OperationHelper getInstance() {
        if (instance == null) {
            instance = new OperationHelper();
            students = new ArrayList<>();
            setStudents();
        }

        return instance;
    }

    private static void setStudents() {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
            while((line = reader.readLine()) != null) {
                List<String> fields = Arrays.asList(line.split(","));
                // step 2 - fill list with student objects by reading file
                students.add(new Student(fields.get(0), fields.get(1),
                        Integer.parseInt(fields.get(2)),
                        Integer.parseInt(fields.get(3)),
                        Integer.parseInt(fields.get(4)),
                        Float.parseFloat(fields.get(5)),
                        fields.get(6), fields.get(7))
                );
            }
            reader.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void setCommand_list(List<String> command_list) {
        this.command_list = command_list;
    }


    public List<Student> getStudents() {
        return students;
    }

    public List<String> getCommand_list() {
        return command_list;
    }

    public void command_S() {
        if (command_list.get(0).equals("Student:") | command_list.get(0).equals("S:")) {
            if (command_list.size() == 3 && (command_list.get(2).equals("B") | command_list.get(2).equals("Bus"))) {
                String lastName = command_list.get(1);
                students.stream().filter(s -> s.stLastName.equals(lastName)).forEach((s) -> {
                    System.out.println(s.stLastName + "," + s.stFirstName + "," + s.busRoute);
                });
            } else if (command_list.size() == 2) {
                String lastName = command_list.get(1);
                students.stream().filter(s -> s.stLastName.equals(lastName)).forEach((s) -> {
                    System.out.println(s.stLastName + "," + s.stFirstName + "," + s.grade +
                            "," + s.classRoom + "," + s.tLastName + "," + s.tFirstName);
                });
            }
        }
    }
    public void command_T() {
        if ((command_list.get(0).equals("Teacher:") | command_list.get(0).equals("T:")) && command_list.size() == 2) {
            String lastName = command_list.get(1);
            students.stream().filter(s -> s.tLastName.equals(lastName)).forEach((s) -> {
                System.out.println(s.stLastName + "," + s.stFirstName);
            });
        }
    }
    public void command_B() {
        if ((command_list.get(0).equals("Bus:") | command_list.get(0).equals("B:")) && command_list.size() == 2) {
            int bus = Integer.parseInt(command_list.get(1));
            students.stream().filter(s -> s.busRoute == bus).forEach((s) -> {
                System.out.println(s.stFirstName+ "," + s.stLastName +","+s.grade);
            });
        }
    }
    public void command_G() {
        boolean isGrade =  (command_list.get(0).equals("Grade:") | command_list.get(0).equals("G:"));
        // with Option H/L
        if(command_list.size() == 3 && isGrade) {
            int grade = Integer.parseInt(command_list.get(1));
            List<Student> filter = students.stream().filter(s -> s.grade == grade).collect(Collectors.toList());
            if (command_list.get(2).equals("High") | command_list.get(2).equals("H")) {
                Student max_s = filter.stream().max(Comparator.comparing(s->s.gpa)).get();
                System.out.println(max_s);
            } else if (command_list.get(2).equals("Low") | command_list.get(2).equals("L")) {
                Student low_s = filter.stream().min(Comparator.comparing(s->s.gpa)).get();
                System.out.println(low_s);
            }
        }else if (command_list.size() == 2 && isGrade) {
            int grade = Integer.parseInt(command_list.get(1));
            students.stream().filter(s -> s.grade == grade).forEach((s) -> {
                System.out.println(s.stLastName + "," + s.stFirstName );
            });
        }
    }
    public void command_A(){
        if((command_list.get(0).equals("Average:") | command_list.get(0).equals("A:")) && command_list.size() == 2) {
            int grade = Integer.parseInt(command_list.get(1));
            List<Student> found = students.stream().filter(s -> s.grade == grade).collect(Collectors.toList());
            if(found != null && found.size() > 0){
                double avg = found.stream().collect(Collectors.summarizingDouble(s->s.gpa)).getAverage();
                System.out.println(grade + "," + avg);
            }
        }
    }
    public void command_I(){
        Queue<Student> sorted = new LinkedList<>(students.stream().sorted(Comparator.comparing(s->s.grade)).collect(Collectors.toList()));
        Map<Integer, Integer> map = new HashMap<>();
        int count;
        while (!sorted.isEmpty()){
            count = sorted.poll().grade;
            if(!map.containsKey(count)){
                map.put(count, 1);
            }else{
                map.put(count, map.get(count)+1);
            }
        }
        map.forEach((k,v)->{ System.out.println("" + k + ":" + v);});
    }


}
