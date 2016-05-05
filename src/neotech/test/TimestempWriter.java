package neotech.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import neotech.test.ds.DataSource;

public class TimestempWriter {

    private static final long SECOND = TimeUnit.SECONDS.toMillis(1);

    private final LinkedBlockingQueue<Date> queue = new LinkedBlockingQueue<>();

    private DataSource ds;

    public TimestempWriter(DataSource ds) {
        this.ds = ds;
    }

    private class Tick extends TimerTask {
        @Override
        public void run() {
            queue.add(new Date());
        }
    }

    private void startWriterThread() {
        Timer timer = new Timer();
        timer.schedule(new Tick(), 0, SECOND);
    }

    public void writeToDs() {
        startWriterThread();

        while (true) {
            try {
                Date currentElement = queue.take();
                while (!ds.saveTimestamp(currentElement)) {
                    System.err.println("Failed to write timestemp: " + currentElement);
                    waitForASecond();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitForASecond() {
        try {
            Thread.sleep(SECOND);
        } catch (InterruptedException e) {
        }
    }

}
