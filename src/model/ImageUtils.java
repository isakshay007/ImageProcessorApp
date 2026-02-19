package model;

/**
 * A utility class providing helper methods for image processing tasks.
 * This class includes common utility functions that can be used across
 * various image manipulation operations.
 */
public class ImageUtils {

  /**
   * Clamps a given value to be within the range of 0 to 255.
   *
   * <p>
   * This method ensures that a value does not exceed the valid range for pixel values in an image.
   * If the input value is less than 0, it returns 0. If it is greater than 255, it returns 255.
   * </p>
   *
   * @param value the value to be clamped.
   * @return the clamped value, guaranteed to be between 0 and 255.
   */
  public static int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }

}
