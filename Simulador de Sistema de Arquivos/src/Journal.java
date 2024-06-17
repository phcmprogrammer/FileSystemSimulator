import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Journal {
    private BufferedWriter writer;

    public Journal(String fileName) {
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String operation, String... params) {
        try {
            writer.write(operation + " " + String.join(" ", params));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}