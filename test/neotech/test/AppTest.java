package neotech.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class AppTest {

    @Test(expected = NullPointerException.class)
    public void test_print_npe() {
        FakeDs ds = new FakeDs() {

            @Override
            public List<Date> readTimestamps() {
                return null;
            }
        };
        App app = new App(ds);
        String[] args = { "p" };
        app.run(args);
    }

    @Test
    public void test_print_success() {
        FakeDs ds = new FakeDs() {

            @Override
            public List<Date> readTimestamps() {
                return new ArrayList<>();
            }
        };
        App app = new App(ds);
        String[] args = { "p" };
        app.run(args);
    }

    private class TestWasOk extends RuntimeException {

    }

    @Test(expected = TestWasOk.class)
    public void db_not_avalible_for_two_seconds() {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime avalibleAfter = start.plusSeconds(2);
        LocalDateTime checkAfter = start.plusSeconds(3);

        final List<LocalDateTime> expectedDates = new ArrayList<>();

        FakeDs ds = new FakeDs() {

            @Override
            public boolean saveTimestamp(Date t) {
                if (LocalDateTime.now().isBefore(avalibleAfter)) {
                    return false;
                }
                if (LocalDateTime.now().isAfter(checkAfter)) {
                    assertEquals(expectedDates.get(0), start);
                    assertEquals(expectedDates.get(1), start.plusSeconds(1));
                    throw new TestWasOk();
                }
                else {
                    LocalDateTime timestamp = LocalDateTime.ofInstant(t.toInstant(), ZoneId.systemDefault());
                    expectedDates.add(timestamp.truncatedTo(ChronoUnit.SECONDS));
                    return true;
                }
            }

        };
        App app = new App(ds);
        String[] args = {};
        app.run(args);
    }

}
