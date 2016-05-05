package neotech.test.ds;

import java.util.Date;
import java.util.List;

public interface DataSource {

    boolean saveTimestamp(Date timestamp);

    List<Date> readTimestamps();

}
