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

                }

        }while(!command.equals("q") | !command.equals("quit"));

    }
    private static void printMenu(){
        System.out.println("Welcome to Victors Hotel reservation program");
        System.out.println("Options: FR1, FR2, FR3, FR4");
    }



}
