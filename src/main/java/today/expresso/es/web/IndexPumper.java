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

    private final int initDepth;
    private final String source;

    private final NodeLogger nodeLogger;

    public IndexPumper(String source, int depth) {
        this.source = source;
        this.initDepth = depth;

        this.nodeLogger = new NodeLogger(
                new JSONObject().put("source", source)
        );
    }

    public void parse() throws IOException {
        parse(source, initDepth);
    }

    private void parse(String source, int depth) throws IOException {
        logger.info("{}:{}", getDepth(depth), source);
        final Document doc = Jsoup.connect(source)
                .userAgent(UserAgent.get())
                .timeout(5000)
                .followRedirects(false)
                .get();

        doc.traverse(new NodeVisitor() {

            public static final String HREF_ATTR = "href";

            @Override
            public void head(Node node, int i) {
            }

            @Override
            public void tail(Node node, int i) {
                if (node.attributes().size() == 0)
                    return;
                nodeLogger.toLog(node, source, getDepth(depth));

                final String href = node.attr(HREF_ATTR);
                if (href != null && href.length() > 1 && !except.stream().anyMatch(href::startsWith)) {
                    try {
                        if (depth > 0)
                            parse(source + href, depth - 1);
                    } catch (IOException e) {
                        logger.error(node.toString(), e);
                    }
                }
            }
        });
    }

    private int getDepth(int depth) {
        return initDepth - depth;
    }

    public static void main(String[] args) throws IOException {
        final String url = System.getProperty("index.url");
        final int depth = Integer.valueOf(System.getProperty("index.depth", "0"));
        new IndexPumper(url, depth).parse();
        logger.info("{}:{} - completed", depth, url);
    }
}
