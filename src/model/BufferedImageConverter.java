package model;

import java.awt.image.BufferedImage;

/**
 * Utility class for converting between a custom Image representation and a {@link BufferedImage}.
 * This class provides methods to transform an image in custom format
 *            (with separate RGB channels) to a
 * {@link BufferedImage} and vice versa.
 */
public class BufferedImageConverter {

  /**
   * Converts a {@link BufferedImage} to a custom {@link Image} format, where the RGB
   *          channels are stored separately.
   *
   * @param bufferedImage the {@link BufferedImage} to be converted.
   * @return a custom {@link Image} object with separate red, green, and blue channel data.
   */
  public Image toCustomImage(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = bufferedImage.getRGB(j, i);
        red[i][j] = (rgb >> 16) & 0xFF;
        green[i][j] = (rgb >> 8) & 0xFF;
        blue[i][j] = rgb & 0xFF;
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Converts a custom {@link Image} object to a {@link BufferedImage} representation.
   *
   * @param image the custom {@link Image} object to be converted.
   * @return a {@link BufferedImage} representing the image with RGB data.
   */
  public BufferedImage toBufferedImage(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = image.getRedChannel()[i][j];
        int g = image.getGreenChannel()[i][j];
        int b = image.getBlueChannel()[i][j];
        int rgb = (r << 16) | (g << 8) | b;
        bufferedImage.setRGB(j, i, rgb);
      }
    }

    return bufferedImage;
  }
}
