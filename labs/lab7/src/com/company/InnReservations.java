package com.company;

import com.company.executors.*;

import java.util.Scanner;

/**
 * Main program executes commands
 * command == q | quit  : quits the program
 *
 */
public class InnReservations {

    public static void main(String[] args) {
        String command;

        do {
            printMenu();
                switch (command = new Scanner(System.in).next()) {
                    case "FR1":
                        new FR1Executor().execute();
                        break;
                    case "FR2":
                        new FR2Executor().execute();
                        break;
                    case "FR3":
                        new FR3Executor().execute();
                        break;
                    case "FR4":
                        new FR4Executor().execute();
                        break;
                    case "FR5":
                        new FR5Executor().execute();
                        break;
                    case "FR6":
                        new FR6Executor().execute();

                }

        }while(!command.equals("q") && !command.equals("quit"));

    }
    private static void printMenu(){
        System.out.println("\nWelcome to Victors Hotel reservation program");
        System.out.println("Options: FR1, FR2, FR3, FR4, FR5, FR6 (q/quit - to exit)");
    }



}
