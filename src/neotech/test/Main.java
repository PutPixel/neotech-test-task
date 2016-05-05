package neotech.test;

import neotech.test.ds.TimestampDataSource;

public class Main {

    public static void main(String[] args) {
        new App(new TimestampDataSource()).run(args);
    }

}
