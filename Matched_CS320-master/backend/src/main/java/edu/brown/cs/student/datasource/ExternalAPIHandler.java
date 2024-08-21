package edu.brown.cs.student.datasource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


/** Deserializes Json Body to String from HTTP request */
public abstract class ExternalAPIHandler {

  /** Constructor */
  public ExternalAPIHandler() {}

  /**
   * Builds a get request with the given URI and the associated response, in order to extract the
   * json body of the response.
   *
   * @param uri - a String of the URI http get request
   * @return the response json body as a String.
   * @throws URISyntaxException if the URI String can't be parsed as a URI
   * @throws IOException if an I/O exception occurs
   * @throws InterruptedException if a thread is interrupted
   */
  public String externalGet(String uri)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
    return response.body();
  }

  public String externalPost(String uri, String textCompare) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .header("accept", "application/json")
        .header("Cohere-Version", "2021-11-08")
        .header("content-type", "application/json")
        .header("authorization", "Bearer S8wmmd6twbjuDOWWCNAhiokQ2dSKtTJFKiz6La09")
        .method("POST", HttpRequest.BodyPublishers.ofString(textCompare))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();

  }
}
