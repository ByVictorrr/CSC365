package com.company;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main program executes commands
 * command == q | quit  : quits the program
 *
 */
public class InnReservations {

    public static void main(String[] args) {
        String command;
        Executor executor = Executor.getInstance();

        do {
            printMenu();

                switch (command = new Scanner(System.in).next()) {
                    case "FR1":
                        executor.optionFR1();
                        break;
                    case "FR2":
                        executor.optionFR2();
                        break;
                    case "FR3":
                        executor.optionFR3();
                        break;

                }

        }while(!command.equals("q") | !command.equals("quit"));

    }
    private static void printMenu(){
        System.out.println("Welcome to Victors Hotel reservation program");
        System.out.println("Options: FR1, FR2, F3");
    }



}
