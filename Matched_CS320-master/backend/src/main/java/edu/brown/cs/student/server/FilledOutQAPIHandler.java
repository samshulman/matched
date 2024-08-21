package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.datasource.ExternalAPIHandler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FilledOutQAPIHandler extends ExternalAPIHandler implements Route {

    private Firebase firebase;
    private ArrayList<String> formsAvailable;

    public FilledOutQAPIHandler(Firebase firebase, ArrayList<String> formsAvailable) {
        this.firebase = firebase;
        this.formsAvailable = formsAvailable;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        QueryParamsMap qm = request.queryMap();
        String userKey = qm.value("user-key");
        ArrayList<String> filledOutFormsForUser = new ArrayList<>();

        for (String form : this.formsAvailable) {
            String[] temp = {form, userKey};
            if (!Objects.equals(this.firebase.readDatabase(temp), "null")) {
                filledOutFormsForUser.add(form);
                System.out.println("looping here");
            }
        }
        return this.serialize(filledOutFormsForUser);
    }

    private String serialize(List<String> forms) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<String>> jsonAdapter = moshi.adapter(
            Types.newParameterizedType(List.class, String.class));
        return jsonAdapter.toJson(forms);
    }
}
