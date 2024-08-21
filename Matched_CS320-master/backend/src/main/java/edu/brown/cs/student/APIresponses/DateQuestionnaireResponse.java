package edu.brown.cs.student.APIresponses;

/**
 * class that stores a questionnaireResponse for a new friend
 */
public class DateQuestionnaireResponse{

  private String name;
  private String pronouns;
  private String classYear;
  private String email;
  private String perfDate;
  private String expectations;
  private String passions;
  private String reasoning;

  public DateQuestionnaireResponse(){

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

  public String getPerfDate() {
    return this.perfDate;
  }

  public String getPassions() {
    return this.passions;
  }

  public String getExpectations() {
    return this.expectations;
  }

  public String getReasoning() {
    return this.reasoning;
  }
}
