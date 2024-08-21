package edu.brown.cs.student.server;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.moshi.Moshi;

/**
 * The Firebase class is responsible for all things, database. It provides functions to read, write, update, and delete
 * users from the database. It also is responsible for calculating the cosine similarity scores for each user.
 */
public class Firebase {
  private FirebaseDatabase firebaseDatabase;
  public Boolean hasBeenInitiated=false;
  List<User> usersToReturn = new LinkedList<>();

  /**
   * apiKey: "AIzaSyCI_PJl30MJRkgTRKj6C6G97u83RmFQKgw",
   * authDomain: "matched-cs320.firebaseapp.com",
   * projectId: "matched-cs320",
   * storageBucket: "matched-cs320.appspot.com",
   * messagingSenderId: "352278494548",
   * appId: "1:352278494548:web:4bc08b9d1dfa39d3909cc1",
   * measurementId: "G-BTXZE45XGZ",
   * databaseURL: "https://matched-cs320-default-rtdb.firebaseio.com/"
   */

  /**
   * This function is responsible for initializing the firebase with the correct credentials; called upon
   * server start
   * @throws IOException
   */
  public void initFirebase() throws IOException {
    FileInputStream serviceAccount =
        new FileInputStream("matched2cs32-firebase-adminsdk-i99s2-522059a18a.json");
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
        .setDatabaseUrl("https://matched2cs32-default-rtdb.firebaseio.com/")
        //.setAPIKey("AIzaSyCI_PJl30MJRkgTRKj6C6G97u83RmFQKgw")
        //.setServiceAccount(new FileInputStream(new File("C:\\Users\\Vicky\\Documents\\NetBeansProjects\\Examples\\src\\com\\javaquery\\google\\firebase\\Firebase-30f95674f4d5.json")))
        .setCredentials(credentials)
        .build();
      FirebaseApp.initializeApp(firebaseOptions);
    firebaseDatabase =
        FirebaseDatabase.getInstance("https://matched2cs32-default-rtdb.firebaseio.com/");
    hasBeenInitiated=true;
  }

  public void unInitFirebase() {
    // Get the Firebase app instance
    FirebaseApp app = FirebaseApp.getInstance();
    // Delete the app instance and disconnect from the Firebase database
    app.delete();
  }

  /**
   * Helper function used when putting new users in the database
   * @param user user to be transformed to JSON
   * @return a JSON representation of the param user
   */
  public Object createNewUser(User user) {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(User.class).toJson(user);
  }

  /**
   * This function allows for reading data from the database
   * @param args an array of Strings that represents the path of the data in the database
   * @return the data held within the key specified in args, as a JSON (does not moshi it) to
   * make the read function more generally useful
   */
  public String readDatabase(String[] args) {
    try {
      String startURL = "https://matched2cs32-default-rtdb.firebaseio.com/";
      URI fullURL = new URI(startURL + endpointBuilder(args) + ".json");
      HttpRequest retrieveData = HttpRequest.newBuilder().uri(fullURL).GET().build();
      HttpResponse<String> dataResponse =
              HttpClient.newBuilder().build().send(retrieveData, HttpResponse.BodyHandlers.ofString());
      return dataResponse.body();
    } catch (Exception e) {
      return new GeneralErrFirebase().serialize();
    }

  }

  /**
   * This function allows for putting new users in the database, replacing an existing key-value pair
   * if it already exists
   * @param args the path to the container in which the desired key-value pair should be stored
   * @param key the email (without @brown.edu)
   * @param value the user data
   * @return strings for the purposes of testing
   */
  public String putDatabase(String[] args, String key, Object value) {
    //Firebase does not allow some chars in keys
    ArrayList<Character> forbiddenChars = new ArrayList<>(List.of('.', '#', '$', '[', ']'));
    for (Character character : forbiddenChars) {
      if (key.contains(character.toString())) {
        System.out.println("Please do not use forbidden chars in the key");
        return new GeneralErrFirebase().serialize();
      }
    }
    try {
      DatabaseReference ref = firebaseDatabase.getReference(endpointBuilder(args) + "/" + key);
      final CountDownLatch latch = new CountDownLatch(1);
      System.out.println("key: " + ref.getKey());
      System.out.println("root: " + ref.getRoot());
      System.out.println("parent: " + ref.getParent());
      ref.setValue(value, (databaseError, databaseReference) -> {
        if (databaseError != null) {
          System.out.println("Data could not be saved " + databaseError.getMessage());
          latch.countDown();
        } else {
          System.out.println("Data saved successfully.");
          latch.countDown();
        }
      });
      latch.await();
      return new GeneralSuccessFirebase().serialize();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return new GeneralErrFirebase().serialize();
    }
  }

  /**
   * This helper function is responsible for building endpoint strings
   * @param args the path to the container
   * @return the formatted endpoint as a string
   */
  private String endpointBuilder(String[] args) {
    StringBuilder endpoint = new StringBuilder();
    for (String eachString : args) {
      endpoint.append(eachString).append("/");
    }
    return endpoint.toString();
  }


  /**
   * This function is similar to 'put' except that it will not overwrite preexisting data with the same
   * key in the same location in the database
   * @param args the path to the container in which the desired key-value pair should be stored
   * @param key the email (without @brown.edu)
   * @param value the user data
   * @return strings for testing purposes
   */
  public String updateDatabase(String[] args, String key, Object value) {
    try {
      String endpoint = endpointBuilder(args);
      DatabaseReference ref = firebaseDatabase.getReference(endpoint);
      final CountDownLatch latch = new CountDownLatch(1);
      System.out.println("key: " + ref.getKey());
      System.out.println("root: " + ref.getRoot());
      System.out.println("parent: " + ref.getParent());
      HashMap<String, Object> map = new HashMap();
      map.put(key, value);
      if (!this.updateHelper(endpoint, key).equals("null")) {
        ref.updateChildren(map, ((databaseError, databaseReference) -> {
          if (databaseError != null) {
            System.out.println("Data could not be saved " + databaseError.getMessage());
            latch.countDown();
          } else {
            System.out.println("Data saved successfully.");
            latch.countDown();
          }
        }));
        latch.await();
        return new GeneralSuccessFirebase().serialize();
      }
      else {
        System.out.println("User, " + key + ", does not exist under that root");
        return new GeneralErrFirebase().serialize();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new GeneralErrFirebase().serialize();
    }
  }

  /**
   * This helper function is used to determine whether data already exists under the key name
   * @param endpoint the path to the container in which the desired key-value pair should be stored, formatted
   * @param key the email (without @brown.edu)
   * @return "null" if it doesn't exist yet, or the contents of the preexisting data with the same key
   */
  private String updateHelper(String endpoint, String key) {
    try {
      String startURL = "https://matched2cs32-default-rtdb.firebaseio.com/" + endpoint + "/" + key;
      URI fullURL = new URI(startURL + ".json");
      HttpRequest retrieveData = HttpRequest.newBuilder().uri(fullURL).GET().build();
      HttpResponse<String> dataResponse =
              HttpClient.newBuilder().build().send(retrieveData, HttpResponse.BodyHandlers.ofString());
      return dataResponse.body();
    } catch (Exception e) {
      return new GeneralErrFirebase().serialize();
    }
  }

  /**
   * This function allows for the removal of users from the database
   * @param args the path to the container in which the desired key (and value) should be deleted
   * @param key the email (without @brown.edu)
   * @return a string for the purposes of testing
   */
  public String deleteFromDatabase(String[] args, String key) {
    if (Objects.equals(updateHelper(endpointBuilder(args), key), "null")) {
      System.out.println("User, " + key + ", does not exist under that root");
      return new GeneralErrFirebase().serialize();
    }
    else {
      try {
        StringBuilder endpoint = new StringBuilder();
        for (String eachString : args) {
          endpoint.append(eachString).append("/");
        }
        DatabaseReference userRef = firebaseDatabase.getReference(endpoint + "/" + key);
        final CountDownLatch latch = new CountDownLatch(1);
        System.out.println("key: " + userRef.getKey());
        System.out.println("root: " + userRef.getRoot());
        System.out.println("parent: " + userRef.getParent());
        userRef.removeValue((databaseError, databaseReference) -> {
          if (databaseError != null) {
            System.out.println("User could not be deleted:  " + databaseError.getMessage());
            latch.countDown();
          } else {
            System.out.println("User deleted successfully.");
            latch.countDown();
          }
        });
        latch.await();
        return new GeneralSuccessFirebase().serialize();
      } catch (InterruptedException e) {
        e.printStackTrace();
        return new GeneralErrFirebase().serialize();
      }
    }
    }

  /**
   * This function compares the main user to all other users in the respective questionnaire category
   * @param root the path to the container in which the desired key-value pair should be stored, formatted
   * @param mainUserKey the key (email without @brown.edu) of the current user logged in
   * @return a List of users that have been found to be similar based on their responses
   * @throws InterruptedException
   */
  public List<User> otherLoop(String root, String mainUserKey) throws InterruptedException {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Get a reference to the "users" location in the database
    DatabaseReference usersRef = database.getReference(root);

    final CountDownLatch latch = new CountDownLatch(1);
    // Query the database for all child nodes under the root node
    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

      @Override
      public void onDataChange(DataSnapshot snapshot) {
        double maxCosSim = -2.0;
        Comparator<Map.Entry<User, Double>> comparator = new Comparator<Map.Entry<User, Double>>() {
          @Override
          public int compare(Map.Entry<User, Double> e1, Map.Entry<User, Double> e2) {
            return e2.getValue().compareTo(e1.getValue());
          }
        };
        // Create a priority queue using the Comparator
        PriorityQueue<Map.Entry<User, Double>> pq = new PriorityQueue<>(comparator);
        HashMap<User, Double> map = new HashMap<>();
        Moshi moshi2 = new Moshi.Builder().build();
        User mainUser = null;

        //find mainUser
        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
          if (Objects.equals(userSnapshot.getKey(), mainUserKey)) {
            try {
              mainUser = moshi2.adapter(User.class).fromJson(userSnapshot.getValue().toString());
              break;
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        // Loop through all child nodes
        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
          // Get the user object from the snapshot
          try {
            User user = null;
            if (Objects.equals(userSnapshot.getKey(), mainUserKey)) {
              continue;
            } else {
              user = moshi2.adapter(User.class).fromJson(userSnapshot.getValue().toString());
            }
            if (mainUser != null && user != null) {
              List<List<Float>> CompEmbedding = user.getEmbedding();
              double cosineSim = cosineSimAverage(mainUser.getEmbedding(), CompEmbedding);
              map.put(user, cosineSim);
              System.out.println("map size" + map.size());
              System.out.println(mainUser.getName());
              System.out.println(user.getName());
              System.out.println(snapshot.getChildrenCount());

            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        System.out.println("got to end of loop");
        if (map.isEmpty()) {
          System.out.println("map is empty");
        }
        pq.addAll(map.entrySet());
        System.out.println(map.size());

        for (int i = 0; i < 3; i++) {
          if (!pq.isEmpty()) {
            Map.Entry<User, Double> entry = pq.poll();
            usersToReturn.add(entry.getKey());
          }
        }


        System.out.println("got to latch countdown");
        latch.countDown();
      }
      @Override
      public void onCancelled(DatabaseError error) {
        System.out.println("got to error");
        return;
      }
    });
    latch.await();
    System.out.println("got out of nest");

    return usersToReturn;
  }

  /**
   * This helper function deals specifically with the embeddings of each user compared to the main user
   * @param mainUser user currently logged in
   * @param compUser every other user in the respective questionnaire location in the database
   * @return a similarity score
   */
  public double cosineSimAverage(List<List<Float>> mainUser, List<List<Float>> compUser) {
    double score = 0.0;
    for (int i = 0; i < mainUser.size(); i++) {
      score += cosineSimilarity(mainUser.get(i), compUser.get(i));
    }
    return score / mainUser.size();
  }

  /**
   * A helper function that calculates the cosine similarity based on each vector
   * @param vec1
   * @param vec2
   * @return a unit vector
   */
    public static double cosineSimilarity(List<Float> vec1, List<Float> vec2) {
      int commonElements = 0;
      double dotProduct = 0;
      double sum1 = 0;
      double sum2 = 0;

      for (int i = 0; i < vec1.size() && i < vec2.size(); i++) {
        dotProduct += vec1.get(i) * vec2.get(i);
        sum1 += vec1.get(i) * vec1.get(i);
        sum2 += vec2.get(i) * vec2.get(i);
        commonElements++;
      }

      for (int i = commonElements; i < vec1.size(); i++) {
        sum1 += vec1.get(i) * vec1.get(i);
      }

      for (int i = commonElements; i < vec2.size(); i++) {
        sum2 += vec2.get(i) * vec2.get(i);
      }

      double magnitude = Math.sqrt(sum1) * Math.sqrt(sum2);

      if (magnitude == 0) {
        return 0;
      }

      return dotProduct / magnitude;
    }

  /**
   * A getter that returns the users calculated to be the most similar to the main user
   * @return
   */
  public List<User> getUsersToReturn(){
    return this.usersToReturn;

  }
}

/**
 * If the size of the data being stored is ever an issue (as we thought it was at one point), this function
 * can be used to store all of it. The loop function would need to be slightly modified to accommodate
 * this.
 */
//  public void putEmbeddingInDatabase(String[] args, String key, String value) throws IOException {
//    int chunkSize = 400;
//    if (value.length() > chunkSize) {
//      System.out.println("bigger than chunk size");
//      Iterable<String> chunks = Splitter.fixedLength(chunkSize).split(value);
//      ArrayList<String> oldArgs = new ArrayList<>(Arrays.asList(args));
//      oldArgs.add("embedding");
//      for (String chunk : chunks) {
//        System.out.println(chunk);
//        putDatabase2(oldArgs, "data", chunk);
//        oldArgs.add("dataNext");
//      }
//    }
//    else {
//      ArrayList<String> newArgs = new ArrayList<>(Arrays.asList(args));
//      putDatabase2(newArgs, "data", value);
//    }
//  }
