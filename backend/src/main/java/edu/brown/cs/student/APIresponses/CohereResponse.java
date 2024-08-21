package edu.brown.cs.student.APIresponses;

import java.util.List;

public class CohereResponse {

  private String id;
  private List<String> texts;
  private List<List<Float>> embeddings;

  public CohereResponse(String id, List<String> texts, List<List<Float>> embeddings){
    this.id = id;
    this.texts = texts;
    this.embeddings = embeddings;
  }

  public List<List<Float>> getEmbeddings(){
    return embeddings;
  }
}
