package edu.brown.cs.student.stars.server;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.datasource.ExternalAPIHandler;
import edu.brown.cs.student.APIresponses.CohereResponse;
import edu.brown.cs.student.APIresponses.ErrBadRequestResponse;
import edu.brown.cs.student.server.Firebase;
import edu.brown.cs.student.server.GetMatchesAPIHandler;
import edu.brown.cs.student.server.User;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGetMatchesHandler extends ExternalAPIHandler {
  public static Firebase firebase = new Firebase();


  @BeforeAll
  public static void spark_port_setup() throws IOException {
    System.out.println("port setup called.");
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
    firebase.initFirebase();
  }

  @BeforeEach
  public void setup(){
    System.out.println("setup called.");
    // In fact, restart the entire Spark server for every test!
    Spark.get("/getMatches", new GetMatchesAPIHandler(firebase));
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    System.out.println("teardown called.");
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/getMatches");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }


  @Test
  public void TestValid3Matches() throws IOException, InterruptedException {

    //creating and adding user whitney_hannallah to database
    String CohereResponseJson =
        this.externalPost("https://api.cohere.ai/embed",
            "{\"texts\":[\"" + "I would wake up, go for a long walk around India Point Park. Then I would go to Plant city and Trader Joes. I would end the day watching the sunset and then go to a party." + "\",\""
                + "I would go to the European Alps and go hiking. I would also want to skydive and eat lots of good food." +
                "\",\"" + "I want to have more time to do art. I would love to improve on my painting and drawing skills. I especially want to paint outdoor scenes." + "\",\"" + "I am looking for a new friend to explore Providence with!" + "\"]}");

    Moshi moshi2 = new Moshi.Builder().build();
    CohereResponse CohereReturn =
        moshi2.adapter(CohereResponse.class).fromJson(CohereResponseJson);
    List<List<Float>> embeddings =
        CohereReturn.getEmbeddings();

    User userToDatabase =
        new User("friend", "whitney", "she/her", "2024", "whitney_hannallah@brown.edu",
            embeddings);

    String[] userRoot = {"users-friend-test"};
    //adding user to database
    //firebase.initFirebase();
    this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
        this.firebase.createNewUser(userToDatabase));

    //creating and adding user emily perelman to database
    CohereResponseJson =
        this.externalPost("https://api.cohere.ai/embed",
            "{\"texts\":[\"" + "I would have a lazy saturday in bed. I would probably watch a few movies and go to bed early" + "\",\""
                + "I would go to the beach and do lots of surfing" +
                "\",\"" + "I want to learn to play the trumpet. I used to play in middle school and want to pick it back up again." + "\",\"" + "I am looking for a new friend to watch movies with" + "\"]}");

    Moshi moshi3 = new Moshi.Builder().build();
    CohereReturn =
        moshi3.adapter(CohereResponse.class).fromJson(CohereResponseJson);
    embeddings =
        CohereReturn.getEmbeddings();

    userToDatabase =
        new User("friend", "emily", "she/her", "2025", "emily_perelman@brown.edu",
            embeddings);

    //adding user to database
    //firebase.initFirebase();
    this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
        this.firebase.createNewUser(userToDatabase));

    //creating and adding user samantha_shulman to database
    CohereResponseJson =
        this.externalPost("https://api.cohere.ai/embed",
            "{\"texts\":[\"" + "I would go to Plant city to get coffee. Then I would go for a run during sunset and go out to a party at night. " + "\",\""
                + "I would explore all around Europe because they have great food and I love trying new foods. I also would want to spend time outside and explore the nature there" +
                "\",\"" + "I want to further explore my creative side. I don't do a lot of art now, but I want to try painting, sculpture, and pottery" + "\",\"" + "I am looking for someone new to walk around campus and explore Providence" + "\"]}");

    Moshi moshi4 = new Moshi.Builder().build();
    CohereReturn =
        moshi4.adapter(CohereResponse.class).fromJson(CohereResponseJson);
    embeddings =
        CohereReturn.getEmbeddings();

    userToDatabase =
        new User("friend", "sam", "she/her", "2025", "samantha_shulman@brown.edu",
            embeddings);

    //adding user to database
    //firebase.initFirebase();
    this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
        this.firebase.createNewUser(userToDatabase));

    try
  {
    URL requestURL =
        new URL("http://localhost:" + Spark.port() +
            "/getMatches?user-key=whitney_hannallah&Qtype=users-friend-test");
    //this should be user-key: whitney_hannallah and type users-friend-test but that was giving an infinite loop
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println("got here");

    Moshi moshi5 = new Moshi.Builder().build();
    // success case
    Object response =
        moshi5
            .adapter(Object.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //whitney matches with sam first, because she is most compatible
    assertEquals("[{classYear=2025, email=samantha_shulman@brown.edu",response.toString().substring(0,50));

    requestURL =
        new URL("http://localhost:" + Spark.port() +
            "/getMatches?user-key=samantha_shulman&Qtype=users-friend-test");
    //this should be user-key: whitney_hannallah and type users-friend-test but that was giving an infinite loop
    clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println("got here");

    moshi5 = new Moshi.Builder().build();
    // success case
    response =
        moshi5
            .adapter(Object.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //sam matches with whitney first, because she is most compatible
    assertEquals("[{classYear=2024, email=whitney_hannallah@brown.edu",response.toString().substring(0,51));

    requestURL =
        new URL("http://localhost:" + Spark.port() +
            "/getMatches?user-key=emily_perelman&Qtype=users-friend-test");
    //this should be user-key: whitney_hannallah and type users-friend-test but that was giving an infinite loop
    clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println("got here");

    moshi5 = new Moshi.Builder().build();
    // success case
    response =
        moshi5
            .adapter(Object.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //emily matches with whitney first, because she is most compatible
    assertEquals("[{classYear=2024, email=whitney_hannallah@brown.edu",response.toString().substring(0,51));
    clientConnection.disconnect();
  }
    catch (
  JsonDataException e) {
    URL requestURL =
        new URL("http://localhost:" + Spark.port() + "/getMatches?user-key=whitney_hannallah&Qtype=users-friend-test");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());

    // bad json error case
    ErrBadRequestResponse response = new ErrBadRequestResponse();
    Moshi moshi = new Moshi.Builder().build();
    // assert that expected and actual class types are equal
    assertEquals(
        response.getClass(),
        moshi
            .adapter(ErrBadRequestResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()))
            .getClass());



  }}

  @Test
  public void TestOnlyPerson() throws IOException, InterruptedException {

    //creating and adding user whitney_hannallah to database
    String CohereResponseJson =
        this.externalPost("https://api.cohere.ai/embed",
            "{\"texts\":[\"" + "example. this doesn't matter because wont be compared" + "\",\""
                + "example. this doesn't matter because wont be compared" +
                "\",\"" + "example. this doesn't matter because wont be compared" + "\",\"" + "example. this doesn't matter because wont be compared" + "\"]}");

    Moshi moshi2 = new Moshi.Builder().build();
    CohereResponse CohereReturn =
        moshi2.adapter(CohereResponse.class).fromJson(CohereResponseJson);
    List<List<Float>> embeddings =
        CohereReturn.getEmbeddings();

    User userToDatabase =
        new User("date", "whitney", "she/her", "2024", "whitney_hannallah@brown.edu",
            embeddings);

    String[] userRoot = {"users-date-test"};
    //adding user to database
    //firebase.initFirebase();
    this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
        this.firebase.createNewUser(userToDatabase));

    try
    {
      URL requestURL =
          new URL("http://localhost:" + Spark.port() +
              "/getMatches?user-key=whitney_hannallah&Qtype=users-date-test");
      //this should be user-key: whitney_hannallah and type users-friend-test but that was giving an infinite loop
      HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
      clientConnection.connect();
      assertEquals(200, clientConnection.getResponseCode());
      System.out.println("got here");

      Moshi moshi5 = new Moshi.Builder().build();
      // success case
      Object response =
          moshi5
              .adapter(Object.class)
              .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

      //empty list because only user in section
      assertEquals("[]",response.toString());
      clientConnection.disconnect();
    }
    catch (
        JsonDataException e) {
      URL requestURL =
          new URL("http://localhost:" + Spark.port() + "/getMatches?user-key=whitney_hannallah&Qtype=users-date-test");
      HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
      clientConnection.connect();
      assertEquals(200, clientConnection.getResponseCode());

      // bad json error case
      ErrBadRequestResponse response = new ErrBadRequestResponse();
      Moshi moshi = new Moshi.Builder().build();
      // assert that expected and actual class types are equal
      assertEquals(
          response.getClass(),
          moshi
              .adapter(ErrBadRequestResponse.class)
              .fromJson(new Buffer().readFrom(clientConnection.getInputStream()))
              .getClass());


  }}}


