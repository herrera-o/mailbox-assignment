package dev.herrerao;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testSendMessage() {
        try{
            DataStorage ds = new DataStorage();
            assertEquals(true,
                    ds.sendMessage("0", "1", "Test", "This message is a test."));
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
                System.out.println("Message ID: " + msgID);
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
