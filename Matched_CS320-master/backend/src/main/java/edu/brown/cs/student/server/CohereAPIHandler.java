package edu.brown.cs.student.server;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.APIresponses.CohereResponse;
import edu.brown.cs.student.APIresponses.DateQuestionnaireResponse;
import edu.brown.cs.student.APIresponses.ErrBadJsonResponse;
import edu.brown.cs.student.APIresponses.FriendQuestionnaireResponse;
import edu.brown.cs.student.APIresponses.StudyQuestionnaireResponse;
import edu.brown.cs.student.datasource.ExternalAPIHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.List;

public class CohereAPIHandler extends ExternalAPIHandler implements Route {

  private FormData dataObject;
  private String CohereResponseJson;
  Firebase firebase;

  private static Object LOCK = new Object();


  /**
   * Constructor of CohereAPIHandler.
   * takes in the responses of a user
   */
  public CohereAPIHandler(FormData data, Firebase firebase) throws IOException {
    super();
    this.dataObject = data;
    this.firebase = firebase;
    //firebase.unInitFirebase();


  }

  /**
   * Method that handles get request and outputs a serialized response.
   *
   * @param request  - the request to handle
   * @param response - the response to modify
   * @return A serialized success response or error
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    System.out.println("loaded");

      String dataJson = this.dataObject.returnData();
      String Qtype = this.dataObject.getQtype();
      System.out.println(Qtype);

      Moshi moshi = new Moshi.Builder().build();

      if (Qtype.equals("friend")) {
        FriendQuestionnaireResponse t =
            moshi.adapter(FriendQuestionnaireResponse.class).fromJson(dataJson);

        try {
          System.out.println(t.getDreamVac());
          CohereResponseJson =
              this.externalPost("https://api.cohere.ai/embed",
                  "{\"texts\":[\"" + t.getPerfSat() + "\",\"" + t.getDreamVac() + "\",\"" + t.getHobby() + "\",\"" + t.getReasoning() + "\"]}");
          //api call where individual answers are passed in

          Moshi moshi2 = new Moshi.Builder().build();
          CohereResponse CohereReturn =
              moshi2.adapter(CohereResponse.class).fromJson(CohereResponseJson);
          List<List<Float>> embeddings =
              CohereReturn.getEmbeddings(); //vector embedding of semantic meaning of text

          User userToDatabase =
              new User(Qtype, t.getName(), t.getPronouns(), t.getClassYear(), t.getEmail(),
                  embeddings); //should be embeddings

          String[] userRoot = {"users-friend"};

          //adding user to database
          this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
              this.firebase.createNewUser(userToDatabase));


          return new CohereAPIHandler.CohereSuccessResponse(embeddings).serialize();

        } catch (NullPointerException e) {
          return new ErrBadJsonResponse().serialize();
        } catch (Exception e) {
          // add message later
          e.printStackTrace();
          throw e;
        }
      }

    if (Qtype.equals("date")) {
      DateQuestionnaireResponse t =
          moshi.adapter(DateQuestionnaireResponse.class).fromJson(dataJson);

      try {
        System.out.println(t.getPerfDate());
        CohereResponseJson =
            this.externalPost("https://api.cohere.ai/embed",
                "{\"texts\":[\"" + t.getPerfDate() + "\",\"" + t.getPassions() + "\",\"" + t.getExpectations() + "\",\"" + t.getReasoning() + "\"]}");
        //api call where individual answers are passed in

        Moshi moshi2 = new Moshi.Builder().build();
        CohereResponse CohereReturn =
            moshi2.adapter(CohereResponse.class).fromJson(CohereResponseJson);
        List<List<Float>> embeddings =
            CohereReturn.getEmbeddings(); //vector embedding of semantic meaning of text

        User userToDatabase =
            new User(Qtype, t.getName(), t.getPronouns(), t.getClassYear(), t.getEmail(),
                embeddings);

        String[] userRoot = {"users-date"};
        //adding user to database
        this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
            this.firebase.createNewUser(userToDatabase));


        return new CohereAPIHandler.CohereSuccessResponse(embeddings).serialize();

      } catch (NullPointerException e) {
        return new ErrBadJsonResponse().serialize();
      } catch (Exception e) {
        // add message later
        e.printStackTrace();
        throw e;
      }
    }

    if (Qtype.equals("study")) {
      StudyQuestionnaireResponse t =
          moshi.adapter(StudyQuestionnaireResponse.class).fromJson(dataJson);

      try {
        System.out.println(t.getStudyHabs());
        CohereResponseJson =
            this.externalPost("https://api.cohere.ai/embed",
                "{\"texts\":[\"" + t.getStudyHabs() + "\",\"" + t.getClasses() + "\",\"" + t.getStudySpot() + "\",\"" + t.getReasoning() + "\"]}");
        //api call where individual answers are passed in

        Moshi moshi2 = new Moshi.Builder().build();
        CohereResponse CohereReturn =
            moshi2.adapter(CohereResponse.class).fromJson(CohereResponseJson);
        List<List<Float>> embeddings =
            CohereReturn.getEmbeddings(); //vector embedding of semantic meaning of text

        User userToDatabase =
            new User(Qtype, t.getName(), t.getPronouns(), t.getClassYear(), t.getEmail(),
                embeddings);

        String[] userRoot = {"users-study"};
        //adding user to database
        this.firebase.putDatabase(userRoot, userToDatabase.getEmailWithoutEdu(),
            this.firebase.createNewUser(userToDatabase));

        return new CohereAPIHandler.CohereSuccessResponse(embeddings).serialize();

      } catch (NullPointerException e) {
        return new ErrBadJsonResponse().serialize();
      } catch (Exception e) {
        // add message later
        e.printStackTrace();
        throw e;
      }
    }
    else {
      return "invalid Query param"; //this case should never be reached
    }
  }

  /**
   * Response object to send, containing success notification and data
   */
  public record CohereSuccessResponse(String result, List<List<Float>> embeddings) {
    public CohereSuccessResponse(List<List<Float>> embeddings) {
      this("success", embeddings);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // dependency injection to allow unit testing with mocks
        return CohereAPIUtilities.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }
}
