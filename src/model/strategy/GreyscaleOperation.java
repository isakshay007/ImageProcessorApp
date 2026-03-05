package model.strategy;

import model.Image;

/**
 * Strategy that converts an image to greyscale by averaging the RGB channels.
 */
public class GreyscaleOperation implements ImageOperation {

  @Override
  public Image apply(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = image.getRedChannel()[i][j];
        int g = image.getGreenChannel()[i][j];
        int b = image.getBlueChannel()[i][j];
        int grey = (r + g + b) / 3;

        red[i][j] = grey;
        green[i][j] = grey;
        blue[i][j] = grey;
      }
    }

    return new Image(width, height, red, green, blue);
  }
}
