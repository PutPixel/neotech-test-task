package neotech.test;

import java.util.Arrays;

import neotech.test.ds.DataSource;

public class App {

    private DataSource ds;

    public App(DataSource ds) {
        this.ds = ds;
    }

    public void run(String[] args) {
        if (args.length == 0) {
            writeModeStart();
        }
        else if (args.length > 0 && args[0].trim().equalsIgnoreCase("-p")) {
            printModeStart();
        }
        else {
            printHelp(args);
        }
    }

    private void printHelp(String[] args) {
        System.out.println("Parametr -p or no parametrs allowed. Passed args are: " + Arrays.toString(args));
    }

    private void printModeStart() {
        ds.readTimestamps().forEach(System.out::println);
    }

    private void writeModeStart() {
        new TimestempWriter(ds).writeToDs();
    }

}
