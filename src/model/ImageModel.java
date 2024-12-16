package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * An interface for an image processing model that defines the operations
 * available for manipulating images.
 */
public interface ImageModel {

  /**
   * Loads an image from the specified file path and assigns it a unique name in the model.
   *
   * @param filePath  the file path of the image to load.
   * @param imageName the unique name to assign to the loaded image.
   * @throws IOException if an error occurs while reading the file.
   */
  void load(String filePath, String imageName) throws IOException;

  /**
   * Saves the image with the specified name to the given file path.
   *
   * @param filePath  the file path where the image will be saved.
   * @param imageName the name of the image to save.
   * @throws IOException if an error occurs while writing the file.
   */
  void save(String filePath, String imageName) throws IOException;

  /**
   * Creates a visualization of a specific color channel (e.g., red, green, blue) of the image
   * and saves it under a new name.
   *
   * @param channel       the color channel to visualize (e.g., "red", "green", "blue").
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting visualization image.
   */
  void visualizeChannel(String channel, String imageName, String destImageName);

  /**
   * Creates a visualization of a specific color channel (e.g., red, green, blue) of the image
   * using a mask and saves the result.
   *
   * @param channel       the color channel to visualize (e.g., "red", "green", "blue").
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image to apply during the operation.
   * @param destImageName the name to assign to the resulting visualization image.
   */
  void visualizeChannel(String channel, String imageName, String maskImageName,
                        String destImageName);

  /**
   * Creates a visualization of a specific component of the image (e.g., intensity, luma)
   * and saves it under a new name.
   *
   * @param component     the component to visualize (e.g., "intensity", "value").
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting visualization image.
   */
  void visualizeComponent(String component, String imageName, String destImageName);

  /**
   * Creates a visualization of a specific component of the image using a mask and saves the result.
   *
   * @param component     the component to visualize (e.g., "intensity", "value").
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image.
   * @param destImageName the name to assign to the resulting visualization image.
   */
  void visualizeComponent(String component, String imageName, String maskImageName,
                          String destImageName);

  /**
   * Flips the image horizontally or vertically and saves the result.
   *
   * @param direction     the direction to flip the image ("horizontal" or "vertical").
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting flipped image.
   */
  void flip(String direction, String imageName, String destImageName);

  /**
   * Adjusts the brightness of the image by the specified amount and saves the result.
   *
   * @param amount        the amount to adjust the brightness (positive for brighter,
   *                      negative for darker).
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting image.
   */
  void brighten(int amount, String imageName, String destImageName);

  /**
   * Applies a blur effect to the image and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting blurred image.
   */
  void blur(String imageName, String destImageName);

  /**
   * Applies a blur effect to the image using a mask and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image.
   * @param destImageName the name to assign to the resulting blurred image.
   */
  void blur(String imageName, String maskImageName, String destImageName);

  /**
   * Applies a sharpening effect to the image and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting sharpened image.
   */
  void sharpen(String imageName, String destImageName);

  /**
   * Applies a sharpening effect to the image using a mask and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image.
   * @param destImageName the name to assign to the resulting sharpened image.
   */
  void sharpen(String imageName, String maskImageName, String destImageName);

  /**
   * Converts the image to sepia tone and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting sepia-toned image.
   */
  void convertToSepia(String imageName, String destImageName);

  /**
   * Converts the image to sepia tone using a mask and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image.
   * @param destImageName the name to assign to the resulting sepia-toned image.
   */
  void convertToSepia(String imageName, String maskImageName, String destImageName);

  /**
   * Splits the image into its RGB channels, creating separate images for red, green, and blue
   * components, and saves each with the specified names.
   *
   * @param imageName      the name of the source image.
   * @param redImageName   the name to assign to the resulting red channel image.
   * @param greenImageName the name to assign to the resulting green channel image.
   * @param blueImageName  the name to assign to the resulting blue channel image.
   */
  void rgbSplit(String imageName, String redImageName, String greenImageName, String blueImageName);


  /**
   * Combines separate red, green, and blue channel images into a single image.
   *
   * @param destImageName  the name for the resulting combined image.
   * @param redImageName   the name of the red channel image.
   * @param greenImageName the name of the green channel image.
   * @param blueImageName  the name of the blue channel image.
   */
  void rgbCombine(String destImageName, String redImageName, String greenImageName,
                  String blueImageName);

  /**
   * Converts the image to greyscale based on the specified component (e.g., intensity or luma).
   *
   * @param component     the greyscale component to use (e.g., "luma", "intensity").
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting greyscale image.
   */
  void convertToGreyscale(String component, String imageName, String destImageName);

  /**
   * Converts the image to greyscale based on the specified component (e.g., intensity, luma)
   * using a mask and saves the result.
   *
   * @param component     the greyscale component to use (e.g., "luma", "intensity").
   * @param imageName     the name of the source image.
   * @param maskImageName the name of the mask image to apply during the operation.
   * @param destImageName the name to assign to the resulting greyscale image.
   */
  void convertToGreyscale(String component, String imageName, String maskImageName,
                          String destImageName);

  /**
   * Generates a histogram for the image and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting histogram image.
   */
  void histogram(String imageName, String destImageName);

  /**
   * Applies color correction to the image and saves the result.
   *
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting color-corrected image.
   */
  void colorCorrect(String imageName, String destImageName);

  /**
   * Adjusts the levels of the image using specified black, midtone, and white points.
   *
   * @param black         the black point adjustment value.
   * @param mid           the midtone adjustment value.
   * @param white         the white point adjustment value.
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting adjusted image.
   */
  void levelsAdjust(int black, int mid, int white, String imageName, String destImageName);

  /**
   * Applies a split operation to the image, splitting it by a given percentage and additional
   *         parameters.
   *
   * @param operation        the split operation to perform.
   * @param imageName        the name of the source image.
   * @param destImageName    the name to assign to the resulting image.
   * @param splitPercentage  the percentage of the split operation.
   * @param additionalParams additional parameters required for the operation.
   */
  void splitOperation(String operation, String imageName, String destImageName,
                      Integer splitPercentage, Map<String, Object> additionalParams);

  /**
   * Compresses the image by a given percentage and saves the result.
   *
   * @param percent       the compression percentage (0-100).
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting compressed image.
   */
  void compress(double percent, String imageName, String destImageName);

  /**
   * Checks if the specified image is a processed image in the model.
   *
   * @param imageName the name of the image to check.
   * @return true if the image is processed, false otherwise.
   */
  boolean isProcessedImage(String imageName);

  /**
   * Saves a processed image to the specified file path.
   *
   * @param filePath       the file path to save the image.
   * @param processedImage the processed image to save.
   */
  void saveProcessedImage(String filePath, BufferedImage processedImage);

  /**
   * Downscales the image to a specified width and height and saves the result.
   *
   * @param newWidth      the new width of the image.
   * @param newHeight     the new height of the image.
   * @param imageName     the name of the source image.
   * @param destImageName the name to assign to the resulting downscaled image.
   */
  void downscaleImage(int newWidth, int newHeight, String imageName, String destImageName);

  /**
   * Checks if an image with the specified name exists in the model.
   *
   * @param imageName the name of the image to check.
   * @return true if the image exists in the model, false otherwise.
   */
  boolean imageExists(String imageName);

  /**
   * Retrieves the original image with the specified name from the model.
   *
   * @param imageName the name of the image to retrieve.
   * @return the original image as an {@code Image} object.
   */
  Image getImage(String imageName);

  /**
   * Retrieves a processed version of the image with the specified name.
   *
   * @param imageName the name of the image to retrieve.
   * @return the processed image as a {@code BufferedImage}.
   */
  BufferedImage getProcessedImage(String imageName);
}

