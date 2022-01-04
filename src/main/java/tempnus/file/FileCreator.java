package tempnus.file;

import java.io.File;
import java.io.IOException;

public class FileCreator {

    /**
     * Creates the file and its folder if it does not already exist.
     *
     * @param fileDir  Directory of the file.
     * @param filePath Filepath of the file.
     */
    public static void createFileIfNotExist(String fileDir, String filePath) {
        createDir(fileDir);
        createFile(filePath);
    }

    /**
     * Deletes a file and recreates it.
     *
     * @param fileDir  Directory of the file.
     * @param filePath Filepath of the file.
     */
    public static void deleteAndRecreateFile(String fileDir, String filePath) {
        deleteFile(filePath);
        createFileIfNotExist(fileDir, filePath);
    }

    /**
     * Creates a file directory if it does not exist.
     *
     * @param fileDir Directory of the file to be created.
     */
    private static void createDir(String fileDir) {
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Creates a file if it does not exist.
     *
     * @param filePath Filepath of the file to be created.
     */
    private static void createFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Deletes a file if it exists.
     *
     * @param filePath Filepath of the file to be deleted (if any).
     */
    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
