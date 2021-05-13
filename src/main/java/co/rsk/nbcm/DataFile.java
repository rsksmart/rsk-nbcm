package co.rsk.nbcm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * Output CSV file.
 */
public class DataFile {
    static FileWriter fw;
    static String messageType;
    static long frameSize = -1;
    static long blockHeight = -1;
    static Instant time = Instant.now();
    static boolean isBest = false;

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

    public static void receiveMessage(long size, String type, long height) throws IOException {
        time = Instant.now();
        messageType = type;
        frameSize = size;
        blockHeight = height;
        // Cannot log a BLOCK_MESSAGE because it is not known whether it is the best block.
        // The 'applyBlockEnd()' method will do.
        if (messageType != "BLOCK_MESSAGE") {
            writeReceived();
        }
    }

    public static void sendMessage(long size, String type, long height) throws IOException {
        time = Instant.now();
        messageType = type;
        frameSize = size;
        blockHeight = height;
        writeSend();
    }

    private static void writeSend() throws IOException {
        writeRecord("O");
    }

    private static void writeReceived() throws IOException {
        writeRecord("I");
    }

    private static void writeRecord(String direction) throws IOException {

        final String record = String.format("\"%s\",\"%s\",\"%s\",%d,%d\n",
                time.toString(), direction, messageType, frameSize, blockHeight);
        fw.write(record);
    }

    public static void applyBlockBegin() {
        isBest = false;
    }

    public static void applyBlockEnd() throws IOException {
        if (isBest) {
            messageType = "BEST_BLOCK_CHANGE";
        }
        writeReceived();
    }

    public static void setIsBestBlock(boolean value) {
        isBest = value;
    }
}
