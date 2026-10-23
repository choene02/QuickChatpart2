package quickchat;

import java.util.Scanner;

public class ChatMenu {

    private static final String DASHES =
            "--------------------------------------------------";

    private static int totalAllowed = -1;   // -1 = not asked yet
    private static int entered = 0;         // messages entered so far

    // Runs after a successful login
    public static void run(Scanner scanner) {

        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;
        while (running) {

            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Select menu option (1-3): ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Thanks for using QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static int askNumberOfMessages(Scanner scanner) {
        while (true) {
            System.out.print("How many messages do you wish to enter? ");
            try {
                int n = Integer.parseInt(scanner.nextLine().trim());
                if (n > 0) {
                    return n;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    private static void sendMessages(Scanner scanner) {

        if (totalAllowed == -1) {
            totalAllowed = askNumberOfMessages(scanner);
        }

        if (entered >= totalAllowed) {
            System.out.println("\nYou have already entered all " + totalAllowed
                    + " of your messages.");
            return;
        }

        int storedThisRun = 0;

        while (entered < totalAllowed) {

            System.out.println("\nEntering details for Message Execution Check #"
                    + (entered + 1));

            // Recipient
            String recipient;
            while (true) {
                System.out.print("Enter Recipient Number: ");
                recipient = scanner.nextLine().trim();
                if (Message.isValidRecipient(recipient)) {
                    break;
                }
                System.out.println(Login.cellPhoneMessage(recipient));
            }

            // Message text
            String text;
            while (true) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.print("Enter Message Content: ");
                text = scanner.nextLine();
                int over = Message.charactersOver(text);
                if (over <= 0) {
                    break;
                }
                System.out.println("Message exceeds 250 characters by " + over
                        + "; please reduce the size.");
            }

            Message message = new Message(recipient, text);
            System.out.println("\nMessage ID generated: " + message.getMessageId());
            System.out.println("Message Hash: " + message.getMessageHash());

            int choice = askSendChoice(scanner);
            String result = message.sentMessage(choice);

            System.out.println();
            System.out.println("[CELL CHECKS]: " + message.checkRecipientCell());
            System.out.println("[MESSAGE CHECKS]: " + message.checkMessageLength());
            System.out.println("[STATUS ALERTS]: " + result);

            if (choice == 3) {
                storedThisRun++;
            }

            System.out.println("\n--- DISPLAYING FULL DISPATCH DATA METADATA ---");
            System.out.println("Message ID: Message ID generated: " + message.getMessageId());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getText());
            System.out.println(DASHES);

            // Disregarded message: details are shown first, then the delete prompt
            if (choice == 2) {
                System.out.print("Enter 0 to delete the message (or press ENTER to continue): ");
                if (scanner.nextLine().trim().equals("0")) {
                    System.out.println("[STATUS ALERTS]: Message deleted.");
                }
            }

            entered++;
        }

        System.out.println("\n[LOG SUMMARY]: Total number of messages accumulated: "
                + Message.returnTotalMessages());
        if (storedThisRun > 0) {
            System.out.println("[JSON STORAGE SYNCED]: Records safely stored to local database.");
        }
    }

    private static int askSendChoice(Scanner scanner) {
        while (true) {
            System.out.println("Select Delivery Processing Action Option:");
            System.out.println(" 1 - Send Message");
            System.out.println(" 2 - Disregard Message");
            System.out.println(" 3 - Store Message");
            System.out.print("Choice: ");
            String in = scanner.nextLine().trim();
            if (in.equals("1") || in.equals("2") || in.equals("3")) {
                return Integer.parseInt(in);
            }
            System.out.println("Invalid option. Please choose 1, 2 or 3.");
        }
    }
}