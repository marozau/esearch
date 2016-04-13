package today.expresso.es.web;

import org.json.JSONObject;

/**
 * Created by im on 4/12/16.
 */
public class JSONMerger {

    public static JSONObject merge(JSONObject to, JSONObject from, String prefix) {
        from.keySet().forEach(s -> to.put(prefix + s, from.get(s)));
        return to;
    }
}
