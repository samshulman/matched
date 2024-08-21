package edu.brown.cs.student.server;

import java.util.ArrayList;
import java.util.List;

public class FormData {
  private String data;
  private Boolean loaded;
  private String Qtype;

  /**
   * Shared state class for loaded form data from the API from front end.
   */
  public FormData()
  {
    this.data = "";
    this.loaded = false;
    this.Qtype = "";
  }

  /**
   * Loads data from the parsed file into the class
   * @param d - a List<List<String>>
   */
  public void loadData(String d)
  {
    this.data = d;
  }

  /**
   * Notes that the 'list' contents are no longer empty
   * @param loaded - boolean noting contents have been loaded into class
   */
  public void dataLoaded(Boolean loaded)
  {
    this.loaded = loaded;
  }

  /**
   * Notes if contents have been loaded
   * @return - boolean of if contents have been loaded
   */
  public boolean getBooleanLoaded()
  {
    return this.loaded;
  }

  /**
   * Called when a class needs the contents of the data
   * @return - a List<List<String>> representing the data
   */
  public String returnData()
  {
    return this.data;
  }

  /**
   * sets the questionnaire type
   * @param q
   */
  public void setQtype(String q){
    this.Qtype=q;
  }

  /**
   * gets the quesionnaire type
   * @return
   */
  public String getQtype(){
    return this.Qtype;
  }
}


