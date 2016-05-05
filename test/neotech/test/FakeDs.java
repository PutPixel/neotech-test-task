package neotech.test;

import java.util.Date;
import java.util.List;

import neotech.test.ds.DataSource;

public class FakeDs implements DataSource {

    @Override
    public boolean saveTimestamp(Date timestamp) {
        return false;
    }

    @Override
    public List<Date> readTimestamps() {
        return null;
    }

}
