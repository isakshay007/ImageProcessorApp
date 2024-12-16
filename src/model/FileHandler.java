package model;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A utility class for handling file operations such as reading and writing files.
 * Provides methods to read from and write to files using {@link Scanner} and {@link PrintWriter}.
 */
public class FileHandler {

  /**
   * Reads the content of a file and returns a {@link Scanner} for reading the file's data.
   *
   * @param filePath the path to the file to be read.
   * @return a {@link Scanner} that can be used to read from the file.
   * @throws IOException if an I/O error occurs while opening the file.
   */
  public Scanner readFile(String filePath) throws IOException {
    return new Scanner(new FileInputStream(filePath));
  }

  /**
   * Opens a file for writing and returns a {@link PrintWriter} for writing data to the file.
   *
   * @param filePath the path to the file to be written to.
   * @return a {@link PrintWriter} that can be used to write to the file.
   * @throws IOException if an I/O error occurs while opening the file.
   */
  public PrintWriter writeFile(String filePath) throws IOException {
    return new PrintWriter(new FileWriter(filePath));
  }
}
