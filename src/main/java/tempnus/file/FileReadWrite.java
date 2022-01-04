package tempnus.file;

import tempnus.logger.AppLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class FileReadWrite {

    /**
     * Appends to a .txt file line by line.
     * IMPORTANT: Ensure that the file already exists in the specified filepath.
     *
     * @param lines    Lines to be appended to the file.
     * @param filePath File path of the files to append the lines to.
     */
    public static void writeToFile(ArrayList<String> lines, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            AppLogger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Reads a .txt file line by line.
     *
     * @param filePath File path of the files to read from.
     * @return String ArrayList containing the lines of the file.
     */
    public static ArrayList<String> readFromFile(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            fileReader.close();
        } catch (IOException e) {
            AppLogger.log(Level.SEVERE, e.getMessage());
        }
        return lines;
    }

}
