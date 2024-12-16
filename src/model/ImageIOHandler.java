package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

/**
 * A utility class for loading and saving images in various formats (PPM, PNG, JPG, JPEG).
 * It handles both custom image formats and standard formats by converting them to and from
 * a custom image representation.
 */
public class ImageIOHandler {
  private final FileHandler fileHandler;
  private final ImageParser imageParser;
  private final BufferedImageConverter bufferedImageConverter;

  /**
   * Constructs an {@link ImageIOHandler} with default instances of {@link FileHandler},
   * {@link ImageParser}, and {@link BufferedImageConverter}.
   */
  public ImageIOHandler() {
    this.fileHandler = new FileHandler();
    this.imageParser = new ImageParser();
    this.bufferedImageConverter = new BufferedImageConverter();
  }

  /**
   * Loads an image from the specified file path.
   *
   * <p>
   * This method determines the format based on the file extension and delegates the loading
   * to either PPM-specific or standard image loading methods.
   * </p>
   *
   * @param filePath the path to the image file to be loaded.
   * @return an {@link Image} object representing the loaded image.
   * @throws IOException if an error occurs while reading the image file.
   * @throws UnsupportedOperationException if the file format is not supported (not PPM, PNG,
   *        JPG, or JPEG).
   */
  public Image loadImage(String filePath) throws IOException {
    if (filePath.toLowerCase().endsWith(".ppm")) {
      // Load PPM image
      return loadPPMImage(filePath);
    } else if (filePath.toLowerCase().endsWith(".png") || filePath.toLowerCase().endsWith(".jpg")
            || filePath.toLowerCase().endsWith(".jpeg")) {
      // Load standard image
      return loadStandardImage(filePath);
    } else {
      throw new UnsupportedOperationException("Only PPM, PNG, JPG, JPEG formats are supported.");
    }
  }

  /**
   * Loads a PPM image from the specified file path.
   *
   * @param filePath the path to the PPM image file.
   * @return an {@link Image} object representing the loaded PPM image.
   * @throws IOException if an error occurs while reading the file.
   */
  private Image loadPPMImage(String filePath) throws IOException {
    return imageParser.parsePPM(fileHandler.readFile(filePath));
  }

  /**
   * Loads a standard image (PNG, JPG, JPEG) from the specified file path.
   *
   * @param filePath the path to the standard image file.
   * @return an {@link Image} object representing the loaded image.
   * @throws IOException if an error occurs while reading the image file.
   */
  private Image loadStandardImage(String filePath) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(new File(filePath));
    if (bufferedImage == null) {
      throw new IOException("Unsupported image format.");
    }
    return bufferedImageConverter.toCustomImage(bufferedImage);
  }

  /**
   * Saves the given image to the specified file path.
   *
   * <p>
   * This method checks the file extension to determine whether the image should be saved as a
   * PPM file or converted to a standard format (PNG, JPG, JPEG).
   * </p>
   *
   * @param filePath the path to the file where the image should be saved.
   * @param image the {@link Image} object to be saved.
   * @throws IOException if an error occurs while writing the image to the file.
   */
  public void saveImage(String filePath, Image image) throws IOException {
    if (filePath.toLowerCase().endsWith(".ppm")) {
      saveAsPPM(filePath, image);
    } else {
      saveProcessedImage(filePath, bufferedImageConverter.toBufferedImage(image));
    }
  }

  /**
   * Saves a processed image to the specified file path in PNG, JPG, or JPEG format.
   *
   * @param filePath the path to the file where the image should be saved.
   * @param bufferedImage the {@link BufferedImage} to be saved.
   * @throws IOException if an error occurs while writing the image to the file.
   */
  public void saveProcessedImage(String filePath, BufferedImage bufferedImage) throws IOException {
    String format = getFileFormat(filePath);
    ImageIO.write(bufferedImage, format, new File(filePath));
  }

  /**
   * Saves an image in the PPM format to the specified file path.
   *
   * @param filePath the path to the file where the PPM image should be saved.
   * @param image the {@link Image} object to be saved as PPM.
   * @throws IOException if an error occurs while writing the image to the file.
   */
  private void saveAsPPM(String filePath, Image image) throws IOException {
    try (PrintWriter writer = fileHandler.writeFile(filePath)) {
      imageParser.writePPM(writer, image);
    } catch (IOException e) {
      throw new IOException("Error writing PPM file: " + filePath, e);
    }
  }

  /**
   * Determines the image format based on the file extension.
   *
   * @param filePath the path to the image file.
   * @return the format of the image (either "png", "jpg", or "jpeg").
   * @throws UnsupportedOperationException if the format is not supported (not PNG, JPG, or JPEG).
   */
  private String getFileFormat(String filePath) {
    if (filePath.toLowerCase().endsWith(".png")) {
      return "png";
    } else if (filePath.toLowerCase().endsWith(".jpg")
            || filePath.toLowerCase().endsWith(".jpeg")) {
      return "jpg";
    } else {
      throw new UnsupportedOperationException("Only PNG and JPG formats are supported.");
    }
  }
}
