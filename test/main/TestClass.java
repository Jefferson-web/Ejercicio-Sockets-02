package main;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jefferson
 */
public class TestClass {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el usuario");

        String input = sc.nextLine();

        Pattern pattern = Pattern.compile("^LOGIN\\s{1}[a-zA-Z0-9]+$");
        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

    }

}
