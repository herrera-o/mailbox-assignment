package dev.herrerao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
