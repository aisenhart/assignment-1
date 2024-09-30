import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogProcessor {
    private  Queue<String> logQueue;
    private  Stack<String> errorStack;

    //counters
    private int infoCount;
    private int warnCount;
    private int errorCount;
    private int memoryWarningCount;

    //Most Recent Errors
    private LinkedList<String> last100Errors;


    public LogProcessor(){
        logQueue = new LinkedList<>();
        errorStack = new Stack<>();

        infoCount = 0;
        warnCount = 0;
        errorCount = 0;
        memoryWarningCount = 0;

        last100Errors = new LinkedList<>();

    }

    public void loadLogs(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                logQueue.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void processLogs(){
        while (!logQueue.isEmpty()) {
            String logEntry = logQueue.poll();
            if (logEntry.contains("ERROR")) {
                errorCount++;
                errorStack.push(logEntry);
                if (last100Errors.size() >= 100) {
                    last100Errors.poll();
                }
                last100Errors.add(logEntry);
            } else if (logEntry.contains("WARN")) {
                warnCount++;
                if (logEntry.contains("Memory")) {
                    memoryWarningCount++;
                }
            }
            else if (logEntry.contains("INFO")){
                infoCount++;
            }

        }

    }

    public static void main(String[] args) {
        LogProcessor processor = new LogProcessor();
        processor.loadLogs("src/log-data.csv");
        processor.processLogs();

        System.out.println("WARNING COUNT: " + processor.warnCount);
        System.out.println("ERROR COUNT: " + processor.errorCount);
        System.out.println("MEMORY WARNING COUNT: " + processor.memoryWarningCount);
        System.out.println("INFO COUNT: " + processor.infoCount);
        System.out.println("Last 100 Errors: " + processor.last100Errors);
    }
}