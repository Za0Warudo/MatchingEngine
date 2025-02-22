package main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Book book = new Book();
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                String input = scanner.nextLine().strip();
                if (System.console() == null) System.out.println(input);
                if (input.equals("exit")) break;
                else if (input.equals("print book")) book.show();
                else if (input.startsWith("limit") || input.startsWith("peg") || input.startsWith("market")) book.add(input);
                else if (input.startsWith("cancel")) book.cancel(input.split(" ")[1]); // cancel id
                else throw new UnsupportedOperationException("Operation not supported");
                System.out.print("> ");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.print("> ");
            }
        }
    }
}
