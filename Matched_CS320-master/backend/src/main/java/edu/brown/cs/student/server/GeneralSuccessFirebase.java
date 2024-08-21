package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.APIresponses.ErrBadJsonResponse;

public record GeneralSuccessFirebase(String result) {
    public GeneralSuccessFirebase() {
        this("Success");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(GeneralSuccessFirebase.class).toJson(this);
    }
}
