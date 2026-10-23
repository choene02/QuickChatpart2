package quickchat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class MessageTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private Message msg1;   // Test Data 1: Send
    private Message msg2;   // Test Data 2: Discard

    @Before
    public void setUp() throws Exception {
        Message.resetAll();
        File json = new File(tmp.getRoot(), "storedMessages.json");
        Message.setStorageFile(json.getAbsolutePath());

        msg1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        msg2 = new Message("9876543210", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // ----- Message length -----
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", msg1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(260);
        Message m = new Message("1234567890", 0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                m.checkMessageLength());
    }

    // ----- Recipient -----
    @Test
    public void testRecipientSuccess() {
        assertEquals("Cell phone number successfully captured.",
                msg1.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number and try again.",
                msg2.checkRecipientCell());
    }

    // ----- Hash -----
    @Test
    public void testHashMessage1() {
        assertEquals("00:0:HITONIGHT", msg1.createMessageHash());
    }

    @Test
    public void testHashMessage2() {
        assertEquals("98:1:HIPAYMENT", msg2.createMessageHash());
    }

    // ----- Message ID -----
    @Test
    public void testGeneratedMessageIdIsValid() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(m.checkMessageID());
        assertEquals(10, m.getMessageId().length());
        System.out.println("Message ID generated: " + m.getMessageId());
    }

    @Test
    public void testMessageIdTooLong() {
        Message m = new Message("12345678901", 0, "+27718693002", "Hi there");
        assertFalse(m.checkMessageID());
    }

    // ----- Send / Disregard / Store -----
    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", msg1.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", msg2.sentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        assertEquals("Message successfully stored.", msg1.sentMessage(3));
    }

    // ----- Totals / printing -----
    @Test
    public void testTotalMessagesSent() {
        msg1.sentMessage(1);   // sent
        msg2.sentMessage(2);   // disregarded - must not count
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessagesContainsSentMessage() {
        msg1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains("+27718693002"));
    }

    // ----- JSON file -----
    @Test
    public void testStoredMessageWrittenToJson() throws Exception {
        msg1.sentMessage(3);
        File json = new File(tmp.getRoot(), "storedMessages.json");
        assertTrue(json.exists());
        String content = new String(Files.readAllBytes(json.toPath()),
                StandardCharsets.UTF_8);
        assertTrue(content.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(content.contains("\"recipient\": \"+27718693002\""));
    }
}
