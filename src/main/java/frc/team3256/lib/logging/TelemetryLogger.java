package frc.team3256.lib.logging;
import java.io.*;

public class TelemetryLogger {

    private File loggingFile;
    private FileWriter writer;

    public TelemetryLogger(String path){

        try {
            loggingFile = new File(path);
            writer = new FileWriter(loggingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToLog(String data, double timestamp){

        try{
            writer.write(String.format(Double.toString(timestamp)+" : "+data+"%n"));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        TelemetryLogger logger = new TelemetryLogger("C:\\Users\\WB17\\Desktop\\test.log");
        logger.writeToLog("TEST", 0.1);
        logger.writeToLog("TEST AGAIN", 0.3);
        logger.close();
    }
}
