package persistence;

import org.json.JSONObject;

public interface Writable {

    // EFFECTS: returns this as a JSON object
/*
    CREDIT: JsonSerializationDemo - UBC CPSC210
*/
    JSONObject toJson();
}
