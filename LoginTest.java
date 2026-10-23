package quickchat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    private Login login;

    @Before
    public void setUp() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // ----- assertEquals: username -----
    @Test
    public void testUsernameCorrectlyFormatted() {
        assertEquals("Username successfully captured.",
                Login.usernameMessage("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more than "
                + "five characters in length.",
                Login.usernameMessage("kyle!!!!!!!"));
    }

    // ----- assertEquals: password -----
    @Test
    public void testPasswordMeetsComplexity() {
        assertEquals("Password successfully captured.",
                Login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        assertEquals("Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a capital "
                + "letter, a number, and a special character.",
                Login.passwordMessage("password"));
    }

    // ----- assertEquals: cell phone -----
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.",
                Login.cellPhoneMessage("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        assertEquals("Cell number is incorrectly formatted or does not contain "
                + "an international code; please correct the number and try again.",
                Login.cellPhoneMessage("08966553"));
    }

    // ----- assertEquals: login messages -----
    @Test
    public void testLoginSuccessMessage() {
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(true));
    }

    @Test
    public void testLoginFailureMessage() {
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }

    // ----- assertTrue / assertFalse -----
    @Test
    public void testLoginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
    }

    @Test
    public void testUsernameCorrectlyFormattedTrue() {
        assertTrue(Login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormattedFalse() {
        assertFalse(Login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordComplexityTrue() {
        assertTrue(Login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordComplexityFalse() {
        assertFalse(Login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneTrue() {
        assertTrue(Login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneFalse() {
        assertFalse(Login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUserSuccess() {
        assertEquals("User has been registered successfully.",
                new Login().registerUser("A", "B", "ab_cd", "Password1!", "+27821234567"));
    }

    @Test
    public void testRegisterUserBadUsername() {
        assertTrue(new Login().registerUser("A", "B", "abcdef", "Password1!", "+27821234567")
                .startsWith("Username is not correctly formatted"));
    }
}
