package model.strategy;

import model.Image;

import static model.ImageUtils.clamp;

/**
 * Strategy that applies a sharpening filter to an image using a 5x5 kernel.
 */
public class SharpenOperation implements ImageOperation {

  private static final float[][] KERNEL = {
          {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
          {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
          {-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f},
          {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
          {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}
  };

  /**
   * Returns the sharpen kernel for use in masked operations.
   *
   * @return the 5x5 sharpen kernel.
   */
  public static float[][] getKernel() {
    return KERNEL;
  }

  @Override
  public Image apply(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    int kernelSize = KERNEL.length;
    int offset = kernelSize / 2;

    for (int i = offset; i < height - offset; i++) {
      for (int j = offset; j < width - offset; j++) {
        float redSum = 0;
        float greenSum = 0;
        float blueSum = 0;
        for (int ki = 0; ki < kernelSize; ki++) {
          for (int kj = 0; kj < kernelSize; kj++) {
            int pixelRow = i + ki - offset;
            int pixelCol = j + kj - offset;
            redSum += KERNEL[ki][kj] * image.getRedChannel()[pixelRow][pixelCol];
            greenSum += KERNEL[ki][kj] * image.getGreenChannel()[pixelRow][pixelCol];
            blueSum += KERNEL[ki][kj] * image.getBlueChannel()[pixelRow][pixelCol];
          }
        }
        red[i][j] = clamp((int) redSum);
        green[i][j] = clamp((int) greenSum);
        blue[i][j] = clamp((int) blueSum);
      }
    }

    return new Image(width, height, red, green, blue);
  }
}
