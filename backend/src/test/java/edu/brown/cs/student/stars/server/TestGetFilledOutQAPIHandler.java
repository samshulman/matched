package edu.brown.cs.student.stars.server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.datasource.ExternalAPIHandler;
import edu.brown.cs.student.server.FilledOutQAPIHandler;
import edu.brown.cs.student.server.Firebase;
import edu.brown.cs.student.server.GetMatchesAPIHandler;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGetFilledOutQAPIHandler extends ExternalAPIHandler {
  public static Firebase firebase = new Firebase();
  public static ArrayList<String> formsAvailable = new ArrayList<>();


  @BeforeAll
  public static void spark_port_setup() throws IOException {
    System.out.println("port setup called.");
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
    firebase.initFirebase();
     // temporary fix
    formsAvailable.add("users-friend-test");
    formsAvailable.add("users-date-test");
    formsAvailable.add("users-study-test");
  }

  @BeforeEach
  public void setup() {
    System.out.println("setup called.");

    // In fact, restart the entire Spark server for every test!
    Spark.get("/getFilledOutQs", new FilledOutQAPIHandler(firebase,formsAvailable));
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    System.out.println("teardown called.");
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/getFilledOutQs");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  @Test
  public void testOneFilledOut() throws MalformedURLException {
    try {
      URL requestURL =
          new URL("http://localhost:" + Spark.port() +
              "/getFilledOutQs?user-key=whitney_hannallah");
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

      assertEquals("[users-friend-test, users-date-test]", response.toString());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTwoFilledOut() throws MalformedURLException {
    try {
      URL requestURL =
          new URL("http://localhost:" + Spark.port() +
              "/getFilledOutQs?user-key=samantha_shulman");
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

      assertEquals("[users-friend-test]", response.toString());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNoneFilledOut() throws MalformedURLException {
    try {
      URL requestURL =
          new URL("http://localhost:" + Spark.port() +
              "/getFilledOutQs?user-key=none");
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

      assertEquals("[]", response.toString());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  }
