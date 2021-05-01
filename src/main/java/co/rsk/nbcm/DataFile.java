package co.rsk.nbcm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class DataFile {

    static FileWriter fw;
    static long blockHeight = -1;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        File log = new File("nbcm-trace.csv");

        try {
            fw = new FileWriter(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBlockHeight(long height) {
        blockHeight = height;
    }

    public static void writeSend(String messageCode, long size) throws IOException {
        writeRecord("O", messageCode, size);
    }

    public static void writeReceived(String messageCode, long size) throws IOException {
        writeRecord("I", messageCode, size);
    }

    private static void writeRecord(String direction, String messageCode, long size) throws IOException {
        final String time = Instant.now().toString();
        final String record = String.format("\"%s\",\"%s\",\"%s\",\"%d\",\"%d\"\n",
                time, direction, messageCode, size, blockHeight );
        fw.write(record);
        blockHeight = -1;
    }
}
