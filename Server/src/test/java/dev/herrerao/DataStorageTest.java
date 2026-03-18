package dev.herrerao;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class DataStorageTest {

    @Test
    void testAddUser() {

        try {
            DataStorage ds = new DataStorage();
            assertEquals(true, ds.addUser("admin", "admin"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testGetUserId() {
        try{
            DataStorage ds = new DataStorage();
            ds.addUser("Tuna", "mypassword123");
            assertEquals(1, ds.getUserID("Tuna"));
        }  catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInsertMessage() {
        try{
            DataStorage ds = new DataStorage();
            assertEquals(true,
                    ds.insertMessage(0, 1, "Test", "This message is a test."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetUserValidCredentials() {
        try {
            DataStorage ds = new DataStorage();
            ds.addUser("testuser", "testpass");
            User user = ds.getUser("testuser", "testpass");
            assertNotNull(user);
            assertEquals("testuser", user.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetUserInvalidCredentials() {
        try {
            DataStorage ds = new DataStorage();
            ds.addUser("testuser2", "correctpass");
            User user = ds.getUser("testuser2", "wrongpass");
            assertNull(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getMessages() {
        try {
            DataStorage ds = new DataStorage();
            HashMap<Integer, ArrayList<Message>> messages = ds.getMessages(1);

            for (Integer msgID : messages.keySet()) {
                System.out.println("dev.herrerao.Message ID: " + msgID);
                for (Message message : messages.get(msgID)) {
                    System.out.println(message);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(true);
    }
}
