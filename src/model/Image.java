package model;

/**
 * Represents an image with RGB color channels.
 * This class stores the width, height, and pixel data for the red, green, and
 * blue channels of the image.
 */
public class Image {
  private final int width;
  private final int height;
  private final int[][] redChannel;
  private final int[][] greenChannel;
  private final int[][] blueChannel;

  /**
   * Constructs an Image object with the specified width, height, and RGB channels.
   *
   * @param width  the width of the image in pixels.
   * @param height the height of the image in pixels.
   * @param red    a 2D array representing the red channel pixel values.
   * @param green  a 2D array representing the green channel pixel values.
   * @param blue   a 2D array representing the blue channel pixel values.
   */
  public Image(int width, int height, int[][] red, int[][] green, int[][] blue) {
    this.width = width;
    this.height = height;
    this.redChannel = red;
    this.greenChannel = green;
    this.blueChannel = blue;
  }

  /**
   * Returns the width of the image.
   *
   * @return the width of the image in pixels.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the image.
   *
   * @return the height of the image in pixels.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the red channel pixel values of the image.
   *
   * @return a 2D array containing the red channel pixel values.
   */
  public int[][] getRedChannel() {
    return redChannel;
  }

  /**
   * Returns the green channel pixel values of the image.
   *
   * @return a 2D array containing the green channel pixel values.
   */
  public int[][] getGreenChannel() {
    return greenChannel;
  }

  /**
   * Returns the blue channel pixel values of the image.
   *
   * @return a 2D array containing the blue channel pixel values.
   */
  public int[][] getBlueChannel() {
    return blueChannel;
  }
}
