
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

class OperationHelper {
    private static OperationHelper instance = null;
    private List<String> command_list;
    private static List<Student> students;
    private static Map<Integer, Pair<String, String>> mClassTeacher;

    private OperationHelper() {
    }

    public static OperationHelper getInstance() {
        if (instance == null) {
            instance = new OperationHelper();
            students = new ArrayList<>();
            mClassTeacher = new HashMap<>();
            setStudents();
            setmClassTeacher();
        }

        return instance;
    }

    private static void setStudents() {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/victor/CSC365/labs/lab1/part2/src/list.txt"));
            while((line = reader.readLine()) != null) {
                List<String> fields = Arrays.asList(line.split(","));
                // step 2 - fill list with student objects by reading file
                students.add(new Student(fields.get(0), fields.get(1),
                        Integer.parseInt(fields.get(2)),
                        Integer.parseInt(fields.get(3)),
                        Integer.parseInt(fields.get(4)),
                        Float.parseFloat(fields.get(5)))
                );
            }
            reader.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
    private static void setmClassTeacher(){
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/victor/CSC365/labs/lab1/part2/src/teachers.txt"));
            while((line = reader.readLine()) != null) {
                List<String> fields = Arrays.asList(line.split(", "));
                // step 2 - fill hash map
                mClassTeacher.put(Integer.parseInt(fields.get(2)),
                        new Pair<>(fields.get(0), fields.get(1))
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

    //=======new options===============\\

    // given a class number, list all students assign to it
    /* usage: C: <class number>

        requirements: NR1, NR2
    * */
    public void command_C(){
        if (command_list.get(0).equals("Class:") | command_list.get(0).equals("C:")) {
            int classRoom = Integer.parseInt(command_list.get(1));
            Map<Integer, Integer> classToTotStudents = new HashMap<>();
            Pair<String, String> teacher = null;
            // NR1
            students.stream().filter(s->s.classRoom == classRoom)
                    .forEach(s-> System.out.println(s.stLastName + "," + s.stFirstName));


            // N2
            if((teacher=mClassTeacher.get(classRoom)) != null)
                System.out.println("Teacher: " + teacher.getKey()  + "," + teacher.getValue());

            // NR4
            // Printing every class number with the number of students
            students.stream().forEach(s->{
                // NR4
                if(classToTotStudents.get(s.classRoom) != null){
                    classToTotStudents.put(s.classRoom, classToTotStudents.get(s.classRoom) + 1);
                }else{
                    classToTotStudents.put(s.classRoom, 1);
                }
            });
            // For NR4: print list class: numb of students
            classToTotStudents.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEach(i->System.out.println(i.getKey() + ": " + i.getValue()));
         }
    }


        //=================================\\



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
                    System.out.println(s.stLastName + "," + s.stFirstName + "," + s.grade);
                });
            }
        }
    }
    public void command_T() {
        if ((command_list.get(0).equals("Teacher:") | command_list.get(0).equals("T:")) && command_list.size() == 2) {
            String lastName = command_list.get(1);
            students.stream().filter(s -> mClassTeacher.get(s.classRoom).getKey().equals(lastName)).forEach((s) -> {
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
                System.out.println(low_s );
            }
        }else if (command_list.size() == 2 && isGrade) {
            int grade = Integer.parseInt(command_list.get(1));
            List<Student> grades =students.stream().filter(s -> s.grade == grade).collect(Collectors.toList());
            Map<Integer, Set<Pair<String, String>>> gradeToTeachers = new HashMap<>();
            grades.stream().forEach(s->System.out.println(s.stLastName + "," + s.stFirstName));
            // NR3 req
            grades.stream().forEach(s-> {
                Pair<String, String> teacher = mClassTeacher.get(s.classRoom);
                /**
                 *  if key is in map
                 *      then update set including the value
                 *      map.put(key, new Set(teacher_new, teacher)
                 *  else:
                 *      map.put(key, new Set(teacher)
                 */
                if(gradeToTeachers.get(s.grade) != null) {
                    Set<Pair<String, String>> teachers = gradeToTeachers.get(s.grade);
                    teachers.add(teacher);
                }else{
                    HashSet<Pair<String, String>> pairs = new HashSet<>();
                    pairs.add(teacher);
                    gradeToTeachers.put(s.grade, pairs);
                }
            });
            gradeToTeachers.entrySet().stream().forEach(e->{
                Map.Entry<Integer, Set<Pair<String, String>>> e1 = e;
                Set<Pair<String, String>> value = e1.getValue();
                Integer key = e1.getKey();
                System.out.print(key + ": ");
                value.stream().forEach(t-> System.out.print("(" + t.getKey() +"," +t.getValue() + ")"));
                System.out.println("");
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
