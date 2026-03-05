package model.strategy;

import model.Image;

import static model.ImageUtils.clamp;

/**
 * Strategy that applies a sepia tone filter to an image.
 */
public class SepiaOperation implements ImageOperation {

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

        red[i][j] = clamp((int) (0.393 * r + 0.769 * g + 0.189 * b));
        green[i][j] = clamp((int) (0.349 * r + 0.686 * g + 0.168 * b));
        blue[i][j] = clamp((int) (0.272 * r + 0.534 * g + 0.131 * b));
      }
    }

    return new Image(width, height, red, green, blue);
  }
}
