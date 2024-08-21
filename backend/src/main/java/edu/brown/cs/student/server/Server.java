package edu.brown.cs.student.server;

import static spark.Spark.after;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.squareup.moshi.Moshi;
import spark.QueryParamsMap;
import spark.Spark;

/**
 * Top-level class to run our API server. Contains the main() method which starts Spark and runs the
 * various handlers.
 *
 * <p>We have three endpoints: loadcsv, getcsv, and weather. loadcsv and getcsv utilize a shared
 * state, csvDatabase.
 */
public class Server {

  public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
    FormData object = new FormData();
    Spark.port(9000);
    Firebase firebase = new Firebase();
    firebase.initFirebase();
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    QuestionnaireAPIHandler Qhandler = new QuestionnaireAPIHandler(object, firebase);
    Spark.get("getQuestionairreResponse", Qhandler);

    Spark.get("getCohereResponse", new CohereAPIHandler(object, firebase));
    Spark.get("getMatches",new GetMatchesAPIHandler(firebase));


    /**
     * For testing GetMatchedAPIHandler
     */
    ArrayList<String> formsAvailable = new ArrayList<>();
    formsAvailable.add("users-friend");
    formsAvailable.add("users-date");
    formsAvailable.add("users-study");


    Spark.get("getFilledOutQs", new FilledOutQAPIHandler(firebase, formsAvailable));
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }



}
