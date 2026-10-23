package quickchat;

public class Login {

    // Details stored when the user registers
    private String firstName = "";
    private String lastName = "";
    private String username = "";
    private String password = "";
    private String cellNumber = "";
    private boolean registered = false;

    // ---------- Validation (pure checks) ----------

    // Must contain an underscore and be no more than five characters long
    public static boolean checkUserName(String username) {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    // At least 8 characters, a capital letter, a number and a special character
    public static boolean checkPasswordComplexity(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    // International code (+27) followed by 9 digits, e.g. +27838968976
    // REFERENCE: replace this line with the source you used for the regular
    // expression (the brief requires you to attribute it).
    public static boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null
                && cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    // ---------- Messages for each check ----------

    public static String usernameMessage(String username) {
        return checkUserName(username)
                ? "Username successfully captured."
                : "Username is not correctly formatted; please ensure that "
                  + "your username contains an underscore and is no more "
                  + "than five characters in length.";
    }

    public static String passwordMessage(String password) {
        return checkPasswordComplexity(password)
                ? "Password successfully captured."
                : "Password is not correctly formatted; please ensure that "
                  + "the password contains at least eight characters, a "
                  + "capital letter, a number, and a special character.";
    }

    public static String cellPhoneMessage(String cell) {
        return checkCellPhoneNumber(cell)
                ? "Cell number successfully captured."
                : "Cell number is incorrectly formatted or does not contain "
                  + "an international code; please correct the number and "
                  + "try again.";
    }

    // ---------- Registration ----------

    // Returns the first problem found, or the success message
    public String registerUser(String first, String last, String user,
                               String pass, String cell) {

        if (!checkUserName(user)) {
            return usernameMessage(user);
        }
        if (!checkPasswordComplexity(pass)) {
            return passwordMessage(pass);
        }
        if (!checkCellPhoneNumber(cell)) {
            return cellPhoneMessage(cell);
        }

        firstName = first;
        lastName = last;
        username = user;
        password = pass;
        cellNumber = cell;
        registered = true;

        return "User has been registered successfully.";
    }

    // ---------- Login ----------

    // True only if the details match those stored at registration
    public boolean loginUser(String loginUsername, String loginPassword) {
        return registered
                && loginUsername != null
                && loginPassword != null
                && loginUsername.equals(username)
                && loginPassword.equals(password);
    }

    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public boolean isRegistered() {
        return registered;
    }
}
