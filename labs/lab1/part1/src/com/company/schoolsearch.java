
import java.util.*;

public class schoolsearch {

    public static void main(String[] args) {

        boolean isRunning = true;
        String line;
        List<Student> students = new ArrayList<>();
        OperationHelper helper = OperationHelper.getInstance(); // reads file and gets instance

        Scanner input;
        String command;

        try {
            input = new Scanner(System.in);
            while (isRunning){
                //input = new Scanner(System.in);
                command = input.nextLine();
                helper.setCommand_list(Arrays.asList(command.split(" ")));
                switch (helper.getCommand_list().get(0).charAt(0)) {
                    case 'S':
                        helper.command_S();
                        break;
                    case 'T':
                        helper.command_T();
                        break;
                    case 'B':
                        helper.command_B();
                        break;
                    case 'G':
                        helper.command_G();
                        break;
                    case 'A':
                        helper.command_A();
                        break;
                    case 'I':
                        helper.command_I();
                        break;
                    case 'Q':
                        if (helper.getCommand_list().get(0).equals("Quit") | helper.getCommand_list().get(0).equals("Q"))
                            isRunning = false;
                        break;
                    default:
                        System.out.println("Enter a valid command");
                        break;
                }
            }}catch(Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
    }
}


