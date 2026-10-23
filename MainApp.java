package quickchat;

import java.util.Scanner;

public class MainApp {

    private static final String LINE =
            "====================================================";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println(LINE);
        System.out.println("          WELCOME TO THE CHAT APPLICATION");
        System.out.println(LINE);
        System.out.println("Press ENTER to launch account registration...");
        scanner.nextLine();

        // ===== Part 1: Registration =====
        System.out.println("\n--- Account Registration ---");

        System.out.print("Enter First Name: ");
        String first = scanner.nextLine().trim();

        System.out.print("Enter Last Name: ");
        String last = scanner.nextLine().trim();

        String username = "";
        String password = "";
        String cell = "";

        // Re-asks only the fields that failed validation
        boolean allValid = false;
        while (!allValid) {

            if (!Login.checkUserName(username)) {
                System.out.print("Choose Username (Must contain '_' and be <= 5 chars): ");
                username = scanner.nextLine().trim();
            }
            if (!Login.checkPasswordComplexity(password)) {
                System.out.print("Set Password must be (Min 8 chars, 1 Uppercase, 1 Digit, 1 Special): ");
                password = scanner.nextLine();
            }
            if (!Login.checkCellPhoneNumber(cell)) {
                System.out.print("Enter Cell Phone Number: ");
                cell = scanner.nextLine().trim();
            }

            System.out.println("\n[SYSTEM RESPONSE]:");
            System.out.println(Login.usernameMessage(username));
            System.out.println(Login.passwordMessage(password));
            System.out.println(Login.cellPhoneMessage(cell));

            allValid = Login.checkUserName(username)
                    && Login.checkPasswordComplexity(password)
                    && Login.checkCellPhoneNumber(cell);

            if (!allValid) {
                System.out.println("\nPlease correct the details above and try again.");
            }
        }

        login.registerUser(first, last, username, password, cell);

        // ===== Part 1: Login =====
        System.out.println("\n--- User Login Authorization ---");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String loginUser = scanner.nextLine().trim();

            System.out.print("Password: ");
            String loginPass = scanner.nextLine();

            loggedIn = login.loginUser(loginUser, loginPass);
            System.out.println("\n[SYSTEM RESPONSE]: " + login.returnLoginStatus(loggedIn));
            if (!loggedIn) {
                System.out.println();
            }
        }

        // ===== Part 2: QuickChat (only after a successful login) =====
        ChatMenu.run(scanner);

        scanner.close();
    }
}
