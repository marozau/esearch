package today.expresso.es.web;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by im on 4/10/16.
 */
public class IndexPumper {

    private static final Logger logger = LoggerFactory.getLogger(IndexPumper.class);

    public static final Set<String> except = new HashSet<>(Arrays.asList("http", "/css", "/favicon", "/live-coverage-syndication"));

    public static void main(String[] args) throws IOException {
        final String url = System.getProperty("index.url");
        final int depth = Integer.valueOf(System.getProperty("index.depth", "0"));
        final IndexPumper pumper = new IndexPumper();
        pumper.parse(url, depth);
        logger.info("{}:{} - completed", depth, url);
    }

    public void parse(String url, int depth) throws IOException {
        logger.info("{}:{}", depth, url);
        final Document doc = Jsoup.connect(url)
                .userAgent(UserAgent.get())
                .timeout(5000)
                .followRedirects(false)
                .get();

        doc.traverse(new NodeVisitor() {

            public static final String HREF_ATTR = "href";
            public static final String URI = "node_uri";
            public static final String DEPTH = "node_depth";
            public static final String NAME = "node_name";

            @Override
            public void head(Node node, int i) {
            }

            @Override
            public void tail(Node node, int i) {
                if (node.attributes().size() == 0)
                    return;
                toLog(node);

                final String href = node.attr(HREF_ATTR);
                if (href != null && href.length() > 1 && !except.stream().anyMatch(href::startsWith)) {
                    try {
                        if (depth > 0)
                            parse(url + href, depth - 1);
                    } catch (IOException e) {
                        logger.error(node.toString(), e);
                    }
                }
            }

            private void toLog(Node node) {
                final JSONObject json = new JSONObject();
                node.attributes().asList().stream()
                        .filter(attribute1 -> !attribute1.getValue().trim().isEmpty())
                        .forEach(attribute -> json.put(attribute.getKey(), attribute.getValue()));
                if (json.length() != 0) {
                    json.put(NAME, node.nodeName())
                            .put(URI, node.baseUri())
                            .put(DEPTH, depth);
                    logger.info(json.toString());
                }
            }
        });
    }
}
