package edu.brown.cs.student.stars.server;

import edu.brown.cs.student.server.Firebase;
import edu.brown.cs.student.server.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFirebase {
    public static Firebase firebase = new Firebase();
    public String[] root = {"users-date"};
    public List<List<Float>> exampleEmbedding = List.of(List.of(4f, 3f, 2f), List.of(3f, 5f, 6f));
    public User mainUser = new User("user-friend-test", "tommy", "They/Them",
            "2025", "tommy@brown.edu",exampleEmbedding);
    public String mainUserAsJSON = "{\"classYear\":\"2025\",\"email\":\"tommy@brown.edu\",\"embedding\":[[4.0,3.0,2.0],[3.0,5.0,6.0]],\"name\":\"tommy\",\"pronouns\":\"They/Them\",\"questionnaireType\":\"user-friend-test\"}";
    public String[] temp = {root[0], mainUser.getEmailWithoutEdu()};

    @Test
    public void testInitFirebase() throws IOException {
        //filepath must be without "backend" in firebase for it to work.
        firebase.initFirebase();
        assertEquals(firebase.hasBeenInitiated, true);
    }

    @Test
    public void testCreateNewUser() {
        assertEquals(mainUserAsJSON, firebase.createNewUser(mainUser));
    }

    @Test
    public void testUpdateOnNonExistentUser() {
        assertEquals("{\"result\":\"error_bad_request\"}", firebase.updateDatabase(root, "nonexistentusertest", "doesn't matter what's here"));
    }

    @Test
    public void testDeleteOnNonExistentUser() throws URISyntaxException, IOException, InterruptedException {
        assertEquals("{\"result\":\"error_bad_request\"}", firebase.deleteFromDatabase(root, "nonexistentusertest"));
    }

    //to unit test putDatabase and readDatabase individually
    @Test
    public void testReadDatabaseOnManuallyInputtedItem() throws URISyntaxException, IOException, InterruptedException {
        String[] root = {"firebase-test", "manualTestKey"};
        assertEquals("\"manualTestValue\"", firebase.readDatabase(root));
    }

    @Test
    public void testValidPutDatabase() throws URISyntaxException, IOException, InterruptedException {
        assertEquals("{\"result\":\"Success\"}", firebase.putDatabase(root, mainUser.getEmailWithoutEdu(), firebase.createNewUser(mainUser)));
        String mainUserAsJSON = "\"{\\\"classYear\\\":\\\"2025\\\",\\\"email\\\":\\\"tommy@brown.edu\\\",\\\"embedding\\\":[[4.0,3.0,2.0],[3.0,5.0,6.0]],\\\"name\\\":\\\"tommy\\\",\\\"pronouns\\\":\\\"They/Them\\\",\\\"questionnaireType\\\":\\\"user-friend-test\\\"}\"";
        assertEquals(mainUserAsJSON, firebase.readDatabase(temp));
    }

    @Test
    public void testValidUpdate() throws URISyntaxException, IOException, InterruptedException {
        assertEquals("{\"result\":\"Success\"}", firebase.updateDatabase(root, "tommy", "updateValue"));
        assertEquals("\"updateValue\"", firebase.readDatabase(temp));
    }

    @Test
    public void testValidDeleteFromDatabase() throws URISyntaxException, IOException, InterruptedException {
        String[] root2 = {"firebase-test"};
        //firebase.initFirebase();
        firebase.putDatabase(root2, mainUser.getEmailWithoutEdu(), firebase.createNewUser(mainUser));
        assertEquals("{\"result\":\"Success\"}", firebase.deleteFromDatabase(root2, "tommy"));
        String[] temp2 = {root2[0], "tommy"};
        assertEquals("null", firebase.readDatabase(temp2));
    }

    @Test
    public void testInvalidPut() throws IOException {
        //firebase.initFirebase();
        assertEquals("{\"result\":\"error_bad_request\"}", firebase.putDatabase(root, "invalidchar.here", "N/A"));
    }

    @Test
    public void testLoopFunction() {

    }


}
