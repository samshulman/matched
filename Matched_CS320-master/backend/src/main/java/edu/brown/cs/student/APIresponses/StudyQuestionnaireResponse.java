package edu.brown.cs.student.APIresponses;

/**
 * class that stores a questionnaireResponse for a new friend
 */
public class StudyQuestionnaireResponse{

  private String name;
  private String pronouns;
  private String classYear;
  private String email;
  private String studyHabs;
  private String classes;
  private String studySpot;
  private String reasoning;

  public StudyQuestionnaireResponse(){

  }

  public String getName() {
    return this.name;
  }

  public String getPronouns() {
    return this.pronouns;
  }

  public String getClassYear() {
    return this.classYear;
  }

  public String getEmail() {
    return this.email;
  }

  public String getStudyHabs() {
    return this.studyHabs;
  }

  public String getClasses() {
    return this.classes;
  }

  public String getStudySpot() {
    return this.studySpot;
  }

  public String getReasoning() {
    return this.reasoning;
  }
}
