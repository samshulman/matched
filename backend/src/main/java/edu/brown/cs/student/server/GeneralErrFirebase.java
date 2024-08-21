package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.APIresponses.ErrBadJsonResponse;

public record GeneralErrFirebase(String result) {
    public GeneralErrFirebase() {
        this("error_bad_request");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(GeneralErrFirebase.class).toJson(this);
    }
}
