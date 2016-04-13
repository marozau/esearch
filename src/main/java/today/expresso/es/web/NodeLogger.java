package today.expresso.es.web;

import org.json.JSONObject;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by im on 4/12/16.
 */
public class NodeLogger {

    private static final Logger logger = LoggerFactory.getLogger(NodeLogger.class);

    public static final String BASE = "node_base";
    public static final String URI = "node_uri";
    public static final String DEPTH = "node_depth";
    public static final String NAME = "node_name";

    private final JSONObject defaultJson;
    public NodeLogger(JSONObject defaultJson) {
        this.defaultJson = defaultJson;
    }

    public void toLog(Node node, String url, int depth) {
        final JSONObject json = new JSONObject();
        node.attributes().asList().stream()
                .filter(attribute1 -> !attribute1.getValue().trim().isEmpty())
                .forEach(attribute -> json.put(attribute.getKey(), attribute.getValue()));
        if (json.length() != 0) {
            json.put(NAME, node.nodeName())
                    .put(URI, url)
                    .put(DEPTH, depth);
            JSONMerger.merge(json, defaultJson, "node_");
            logger.info(json.toString());
        }
    }
}
