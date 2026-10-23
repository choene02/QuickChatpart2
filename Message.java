package quickchat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    public static final int MAX_LENGTH = 250;

    // Everything sent / stored while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static String storageFile = "storedMessages.json";
    private static final Random RANDOM = new Random();

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;

    // Used by the app: ID is auto-generated, number = messages sent so far
    public Message(String recipient, String text) {
        this(generateMessageId(), sentMessages.size(), recipient, text);
    }

    // Used by the unit tests so the ID / number are known in advance
    public Message(String messageId, int messageNumber,
                   String recipient, String text) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // ---------- Static helpers (so the menu can validate before creating) ----------

    // Random ten-digit number (leading zeros allowed, e.g. 0012345678)
    public static String generateMessageId() {
        return String.format("%010d",
                (long) (RANDOM.nextDouble() * 10_000_000_000L));
    }

    public static boolean isValidRecipient(String cell) {
        // Reuses the Part 1 checker (Login.checkCellPhoneNumber)
        return Login.checkCellPhoneNumber(cell);
    }

    // How many characters over the limit (0 or less = fine)
    public static int charactersOver(String text) {
        return text == null ? 0 : text.length() - MAX_LENGTH;
    }

    // ---------- Required methods ----------

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageId != null
                && !messageId.isEmpty()
                && messageId.length() <= 10;
    }

    public String checkRecipientCell() {
        if (isValidRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.";
    }

    public String checkMessageLength() {
        int over = charactersOver(text);
        if (over <= 0) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + over
                + "; please reduce the size.";
    }

    // First two digits of ID : message number : FIRSTWORDLASTWORD (all caps)
    public String createMessageHash() {
        String idPart = (messageId != null && messageId.length() >= 2)
                ? messageId.substring(0, 2) : String.valueOf(messageId);

        String[] words = (text == null ? "" : text.trim()).split("\\s+");
        String first = clean(words[0]);
        String last = clean(words[words.length - 1]);

        return (idPart + ":" + messageNumber + ":" + first + last)
                .toUpperCase();
    }

    private static String clean(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return storeMessage()
                        ? "Message successfully stored."
                        : "Message could not be written to the JSON file.";
            default:
                return "Invalid option. Please choose 1, 2 or 3.";
        }
    }

    // All messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.details()).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Order required by the brief: ID, Hash, Recipient, Message
    public String details() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + text;
    }

    // ---------- JSON storage ----------

    public boolean storeMessage() {
        storedMessages.add(this);

        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageID\": \"").append(esc(m.messageId)).append("\",\n")
                .append("    \"messageHash\": \"").append(esc(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(esc(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(esc(m.text)).append("\"\n")
                .append("  }")
                .append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        json.append("]\n");

        try {
            Files.write(Paths.get(storageFile),
                    json.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String esc(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }

    // ---------- Getters / test support ----------

    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }

    public static void setStorageFile(String path) { storageFile = path; }

    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
    }
}
