package edu.brown.cs.student.server;

import java.util.List;

public class User {

  private String classYear;
  private String email;
  private List<List<Float>> embedding;
  private String name;
  private String pronouns;
  private String questionnaireType;

  public User(String Qtype, String name, String pronouns, String classYear, String e, List<List<Float>> emb){
    this.name = name;
    this.pronouns = pronouns;
    this.classYear = classYear;
    this.embedding = emb;
    this.email = e;
    this.questionnaireType = Qtype;

  }

  public String getName() {
    return this.name;
  }

  public String getQuestionnaireType(){
    return this.questionnaireType;
  }

  public String getPronouns() {
    return this.pronouns;
  }

  public String getClassYear() {
    return this.classYear;
  }

  public List<List<Float>> getEmbedding() {
    return this.embedding;
  }

  public String getEmailWithoutEdu() {
    return this.email.split("@")[0];
  }
}

