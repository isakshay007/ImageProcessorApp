package model;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A utility class for parsing and writing PPM (Portable PixMap) image files.
 * It supports reading PPM files in the "P3" format and writing images to PPM format.
 */
public class ImageParser {

  /**
   * Parses a PPM image from the given {@link Scanner} input.
   *
   * <p>
   * This method reads the PPM file in the "P3" format, which includes the width, height,
   * and pixel values for the red, green, and blue channels.
   * </p>
   *
   * @param sc the {@link Scanner} object used to read the PPM file content.
   * @return an {@link Image} object representing the parsed PPM image.
   * @throws IllegalArgumentException if the PPM file does not start with "P3" or if the
   *         file format is incorrect.
   */
  public Image parsePPM(Scanner sc) {
    if (!sc.nextLine().equals("P3")) {
      throw new IllegalArgumentException("PPM file must start with P3");
    }

    // Read width, height, and maximum color value
    int width = sc.nextInt();
    int height = sc.nextInt();
    sc.nextInt(); // Skip max color value (assume it's 255)

    // Initialize RGB arrays
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    // Read pixel values
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        red[i][j] = sc.nextInt();
        green[i][j] = sc.nextInt();
        blue[i][j] = sc.nextInt();
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Writes the given {@link Image} to a PPM file using the "P3" format.
   *
   * <p>
   * This method writes the header for a PPM image (starting with "P3"), followed by the
   * image dimensions, maximum color value, and pixel values for the red, green, and blue channels.
   * </p>
   *
   * @param writer the {@link PrintWriter} object used to write the PPM file content.
   * @param image the {@link Image} object to be written to the file.
   */
  public void writePPM(PrintWriter writer, Image image) {
    try {
      // Write PPM header
      writer.println("P3");
      writer.println(image.getWidth() + " " + image.getHeight());
      writer.println(255); // Maximum color value

      // Write pixel data
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          writer.print(image.getRedChannel()[i][j] + " ");
          writer.print(image.getGreenChannel()[i][j] + " ");
          writer.print(image.getBlueChannel()[i][j] + " ");
        }
        writer.println(); // End of row
      }

      writer.flush(); // Ensure all data is written before closing
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to write PPM file: " + e.getMessage(), e);
    }
  }
}
