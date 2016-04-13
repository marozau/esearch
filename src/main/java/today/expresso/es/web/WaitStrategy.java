package today.expresso.es.web;

import java.util.Random;

/**
 * Created by im on 4/13/16.
 */
public abstract class WaitStrategy {

    abstract public void call();

    public static class Sleep extends WaitStrategy {
        private final long ms;

        public Sleep(long ms) {
            this.ms = ms;
        }

        public void call() {
            call(ms);
        }

        public static void call(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {
                //don't care
            }
        }
    }

    public static class Usual extends Sleep {

        private final Random random = new Random(7);

        public Usual(long ms) {
            super(ms);
        }

        @Override
        public void call() {
            Sleep.call(random.nextInt(3000));
        }
    }
}
