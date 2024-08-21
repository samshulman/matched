package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.APIresponses.CohereResponse;

import java.io.IOException;

public class CohereAPIUtilities {

  /**
   * Converts the valid geojson data string to a FeatureCollection object
   *
   * @param Json - the json to deserialize
   * @return a FeatureCollection
   * @throws IOException if there is an I/O Exception
   */
  public static CohereResponse fromJson(String Json) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(CohereResponse.class).fromJson(Json);
  }


  public static String toJson(CohereAPIHandler.CohereSuccessResponse CohereSuccessResponse) {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<CohereAPIHandler.CohereSuccessResponse> adapter =
        moshi.adapter(CohereAPIHandler.CohereSuccessResponse.class);
    return adapter.toJson(CohereSuccessResponse);
  }
}
