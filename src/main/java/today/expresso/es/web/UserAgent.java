package today.expresso.es.web;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by im on 4/11/16.
 */
public class UserAgent {

    public static final List<String> agents = Arrays.asList(
            "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"",
            "\"Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko\"",
            "\"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)\""
    );

    private static final Random rnd = new Random();

    public static String get() {
        return agents.get(rnd.nextInt(3));
    }

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++)
            System.out.println(UserAgent.get());
    }
}
