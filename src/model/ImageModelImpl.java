package model;

import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static model.ImageUtils.clamp;


/**
 * The {@code ImageModelImpl} class implements the {@code ImageModel} interface and provides
 * functionality for loading, saving, and manipulating images.
 * Images are stored in a map with their name as the key.
 */
public class ImageModelImpl implements ImageModel {
  private final Map<String, Image> images;
  private final Map<String, BufferedImage> processedImages;
  private final ImageIOHandler ioHandler;

  /**
   * Constructs an {@code ImageModelImpl} with an empty collection of images.
   */
  public ImageModelImpl() {
    this.images = new HashMap<>();
    this.processedImages = new HashMap<>();
    this.ioHandler = new ImageIOHandler();
  }

  @Override
  public Image getImage(String imageName) {
    Image image = images.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }
    return image;
  }

  @Override
  public void load(String filePath, String imageName) {
    try {
      Image loadedImage = ioHandler.loadImage(filePath);
      images.put(imageName, loadedImage);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load image from file path: " + filePath, e);
    }
  }

  @Override
  public void save(String filePath, String imageName) {
    try {
      if (processedImages.containsKey(imageName)) {
        BufferedImage processedImage = processedImages.get(imageName);
        ioHandler.saveProcessedImage(filePath, processedImage);
        processedImages.remove(imageName);
      } else if (images.containsKey(imageName)) {
        Image image = images.get(imageName);
        ioHandler.saveImage(filePath, image);
      } else {
        throw new IllegalArgumentException("No image found with the name: " + imageName);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to save image to file path: " + filePath, e);
    }
  }

  @Override
  public void saveProcessedImage(String filePath, BufferedImage processedImage) {
    try {
      ioHandler.saveProcessedImage(filePath, processedImage);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to save processed image: " + e.getMessage(), e);
    }
  }

  @Override
  public BufferedImage getProcessedImage(String imageName) {
    BufferedImage processedImage = processedImages.get(imageName);
    if (processedImage == null) {
      throw new IllegalArgumentException("Processed image not found: " + imageName);
    }
    return processedImage;
  }

  @Override
  public boolean isProcessedImage(String imageName) {
    return processedImages.containsKey(imageName);
  }

  @Override
  public void visualizeChannel(String channel, String imageName, String destImageName) {
    processComponentImage(channel, imageName, destImageName);
  }

  @Override
  public void visualizeChannel(String channel, String imageName, String maskImageName,
                               String destImageName) {
    executeOperationWithMask("component", null, channel, imageName, maskImageName,
            destImageName);
  }

  @Override
  public void visualizeComponent(String component, String imageName, String destImageName) {
    processComponentImage(component, imageName, destImageName);
  }

  @Override
  public void visualizeComponent(String component, String imageName, String maskImageName,
                                 String destImageName) {
    executeOperationWithMask("component", null, component, imageName, maskImageName,
            destImageName);
  }

  /**
   * Processes an image to extract a specific color component (red, green, blue, luma, intensity,
   *      or value)
   * and returns a new image with the processed component applied to all channels.
   *
   * @param component the component to extract and apply to the image (e.g., "red", "green", "blue",
   *                  "value", "intensity", "luma").
   * @param image the image object to process.
   * @return a new {@link Image} object with the processed component.
   * @throws IllegalArgumentException if an unknown component is specified.
   */
  private Image processComponentImage(String component, Image image) {
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

        switch (component.toLowerCase()) {
          case "value":
            int maxValue = Math.max(r, Math.max(g, b));
            setAllChannels(red, green, blue, i, j, maxValue);
            break;
          case "intensity":
            int avgValue = (r + g + b) / 3;
            setAllChannels(red, green, blue, i, j, avgValue);
            break;
          case "luma":
            int lumaValue = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
            setAllChannels(red, green, blue, i, j, lumaValue);
            break;
          case "red":
            setSingleChannel(red, green, blue, i, j, r, 0, 0);
            break;
          case "green":
            setSingleChannel(red, green, blue, i, j, 0, g, 0);
            break;
          case "blue":
            setSingleChannel(red, green, blue, i, j, 0, 0, b);
            break;
          default:
            throw new IllegalArgumentException("Unknown component or channel: " + component);
        }
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Processes an image identified by its name to extract a specific color component (red, green,
   *            blue, luma, intensity, or value)
   * and stores the processed image with the specified destination name.
   *
   * @param component the component to extract and apply to the image (e.g., "red", "green",
   *                  "blue", "value", "intensity", "luma").   *
   * @param imageName the name of the image to process.
   * @param destImageName the name to assign to the resulting processed image.
   * @throws IllegalArgumentException if the image with the specified name is not found or
   *                   if an unknown component is specified.
   */
  private void processComponentImage(String component, String imageName, String destImageName) {
    Image image = images.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }

    Image processedImage = processComponentImage(component, image);
    images.put(destImageName, processedImage);
  }

  /**
   * Sets the same value for all color channels (red, green, and blue) at the specified pixel
   * location.
   *
   * @param red the 2D array representing the red channel of the image.
   * @param green the 2D array representing the green channel of the image.
   * @param blue the 2D array representing the blue channel of the image.
   * @param i the row index of the pixel to modify.
   * @param j the column index of the pixel to modify.
   * @param value the value to set for all channels at the specified pixel.
   */
  private void setAllChannels(int[][] red, int[][] green, int[][] blue, int i, int j, int value) {
    red[i][j] = green[i][j] = blue[i][j] = value;
  }

  /**
   * Sets specific values for the red, green, and blue color channels at the specified pixel
   * location.
   *
   * @param red the 2D array representing the red channel of the image.
   * @param green the 2D array representing the green channel of the image.
   * @param blue the 2D array representing the blue channel of the image.
   * @param i the row index of the pixel to modify.
   * @param j the column index of the pixel to modify.
   * @param redValue the value to set for the red channel at the specified pixel.
   * @param greenValue the value to set for the green channel at the specified pixel.
   * @param blueValue the value to set for the blue channel at the specified pixel.
   */
  private void setSingleChannel(int[][] red, int[][] green, int[][] blue, int i, int j,
                                int redValue, int greenValue, int blueValue) {
    red[i][j] = redValue;
    green[i][j] = greenValue;
    blue[i][j] = blueValue;
  }

  @Override
  public void flip(String direction, String imageName, String destImageName) {
    flipImage(direction, imageName, destImageName);
  }

  /**
   * Flips an image either horizontally or vertically and stores the resulting flipped image
   * with the specified destination name.
   *
   * @param direction the direction to flip the image. It can be either "horizontal" or "vertical".
   * @param imageName the name of the image to flip.
   * @param destImageName the name to assign to the flipped image.
   * @throws IllegalArgumentException if the provided direction is not "horizontal" or "vertical".
   */
  private void flipImage(String direction, String imageName, String destImageName) {
    Image image = images.get(imageName);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (direction.equalsIgnoreCase("horizontal")) {
          red[i][j] = image.getRedChannel()[i][width - 1 - j];
          green[i][j] = image.getGreenChannel()[i][width - 1 - j];
          blue[i][j] = image.getBlueChannel()[i][width - 1 - j];
        } else if (direction.equalsIgnoreCase("vertical")) {
          red[i][j] = image.getRedChannel()[height - 1 - i][j];
          green[i][j] = image.getGreenChannel()[height - 1 - i][j];
          blue[i][j] = image.getBlueChannel()[height - 1 - i][j];
        } else {
          throw new IllegalArgumentException("Invalid flip direction: " + direction);
        }
      }
    }

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  @Override
  public void brighten(int amount, String imageName, String destImageName) {
    brightenImage(amount, imageName, destImageName);
  }

  /**
   * Brightens an image by increasing the intensity of its color channels (red, green, blue)
   * by the specified amount and stores the resulting image with the specified destination name.
   * The pixel values are clamped to ensure they stay within the valid range (0 to 255).
   *
   * @param amount the amount by which to brighten the image. A positive value increases brightness,
   *               and a negative value decreases brightness.
   * @param imageName the name of the image to brighten.
   * @param destImageName the name to assign to the brightened image.
   */
  private void brightenImage(int amount, String imageName, String destImageName) {
    Image image = images.get(imageName);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        red[i][j] = clamp(image.getRedChannel()[i][j] + amount);
        green[i][j] = clamp(image.getGreenChannel()[i][j] + amount);
        blue[i][j] = clamp(image.getBlueChannel()[i][j] + amount);
      }
    }

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  @Override
  public void blur(String imageName, String destImageName) {
    applyKernel(new float[][]{{1 / 16f, 1 / 8f, 1 / 16f},
                              {1 / 8f, 1 / 4f, 1 / 8f},
                              {1 / 16f, 1 / 8f, 1 / 16f}}, imageName, destImageName);
  }

  @Override
  public void blur(String imageName, String maskImageName, String destImageName) {
    float[][] kernel = {{1 / 16f, 1 / 8f, 1 / 16f},
                        {1 / 8f, 1 / 4f, 1 / 8f},
                        {1 / 16f, 1 / 8f, 1 / 16f}};
    executeOperationWithMask("blur", kernel, null, imageName, maskImageName,
            destImageName);
  }

  @Override
  public void sharpen(String imageName, String destImageName) {
    applyKernel(new float[][]{{-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
                              {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
                              {-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f},
                              {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
                              {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}},
            imageName, destImageName);
  }

  @Override
  public void sharpen(String imageName, String maskImageName, String destImageName) {
    float[][] kernel = {{-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
                        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
                        {-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f},
                        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
                        {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}};
    executeOperationWithMask("sharpen", kernel, null, imageName, maskImageName,
            destImageName);
  }

  @Override
  public void convertToSepia(String imageName, String destImageName) {
    applySepiaFilter(imageName, destImageName);
  }

  @Override
  public void convertToSepia(String imageName, String maskImageName, String destImageName) {
    executeOperationWithMask("sepia", null, null, imageName,
            maskImageName, destImageName);
  }

  /**
   * Applies a sepia filter to an image and stores the resulting image with the specified
   * destination name.
   * The sepia filter is applied by adjusting the red, green, and blue color channels of each pixel
   * based on a weighted average of the original values to create a warm, brownish tone.
   * The pixel values are clamped to ensure they stay within the valid range (0 to 255).
   *
   * @param imageName the name of the image to which the sepia filter should be applied.
   * @param destImageName the name to assign to the image after the sepia filter has been applied.
   */
  private void applySepiaFilter(String imageName, String destImageName) {
    Image image = images.get(imageName);
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

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  /**
   * Applies a sepia filter to the provided {@link Image} object and returns the resulting
   * sepia-filtered image.
   * The sepia filter is applied by adjusting the red, green, and blue color channels of each pixel
   * based on a weighted average of the original values to create a warm, brownish tone.
   * The pixel values are clamped to ensure they stay within the valid range (0 to 255).
   *
   * @param image the image to which the sepia filter should be applied.
   * @return a new {@link Image} object with the sepia filter applied.
   */
  private Image applySepiaFilter(Image image) {
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

  @Override
  public void rgbSplit(String imageName, String redImageName, String greenImageName,
                       String blueImageName) {
    splitIntoRGBChannels(imageName, redImageName, greenImageName, blueImageName);
  }

  private void splitIntoRGBChannels(String imageName, String redImageName, String greenImageName,
                                    String blueImageName) {
    Image image = images.get(imageName);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] redChannel = image.getRedChannel();
    int[][] greenChannel = image.getGreenChannel();
    int[][] blueChannel = image.getBlueChannel();

    images.put(redImageName, new Image(width, height, redChannel, new int[height][width],
            new int[height][width]));
    images.put(greenImageName, new Image(width, height, new int[height][width], greenChannel,
            new int[height][width]));
    images.put(blueImageName, new Image(width, height, new int[height][width],
            new int[height][width], blueChannel));
  }

  @Override
  public void convertToGreyscale(String component, String imageName, String destImageName) {
    processComponentImage(component, imageName, destImageName);
  }

  /**
   * Converts the provided image to greyscale by averaging the red, green, and blue color channels
   * for each pixel. The resulting greyscale value is applied to all three color channels
   * (red, green, blue)
   * to produce a monochromatic image.
   *
   * @param image the {@link Image} object to convert to greyscale.
   * @return a new {@link Image} object where all pixels have been converted to greyscale.
   */
  private Image convertToGreyscale(Image image) {
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

  @Override
  public void convertToGreyscale(String component, String imageName, String maskImageName,
                                 String destImageName) {
    executeOperationWithMask("component", null, component, imageName, maskImageName,
            destImageName);
  }

  @Override
  public void rgbCombine(String destImageName, String redImageName, String greenImageName,
                         String blueImageName) {
    combineRGBChannels(destImageName, redImageName, greenImageName, blueImageName);
  }

  /**
   * Combines the individual RGB channels (red, green, and blue) from three separate images
   * into a single image. Each image represents one color channel, and the channels are combined
   * in the same spatial arrangement to create a full-color image.
   *
   * @param destImageName the name of the resulting image that will contain the combined RGB
   *                      channels.
   * @param redImageName the name of the image containing the red channel.
   * @param greenImageName the name of the image containing the green channel.
   * @param blueImageName the name of the image containing the blue channel.
   */
  private void combineRGBChannels(String destImageName, String redImageName, String greenImageName,
                                  String blueImageName) {
    Image redImage = images.get(redImageName);
    Image greenImage = images.get(greenImageName);
    Image blueImage = images.get(blueImageName);

    int width = redImage.getWidth();
    int height = redImage.getHeight();

    images.put(destImageName, new Image(width, height, redImage.getRedChannel(),
            greenImage.getGreenChannel(), blueImage.getBlueChannel()));
  }


  /**
   * Applies a convolution kernel to an image to modify its pixel values based on the kernel.
   * The kernel is applied to each pixel and its neighbors in the image, with the result being
   * stored in the new image.
   * <p>
   * This method processes the image by iterating over each pixel, applying the kernel, and
   * clamping the results to valid color values.
   * </p>
   *
   * @param kernel the convolution kernel to apply to the image.
   * @param image the {@link Image} object to which the kernel will be applied.
   * @return a new {@link Image} object resulting from the convolution operation.
   */
  private Image applyKernel(float[][] kernel, Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    int kernelSize = kernel.length;
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
            redSum += kernel[ki][kj] * image.getRedChannel()[pixelRow][pixelCol];
            greenSum += kernel[ki][kj] * image.getGreenChannel()[pixelRow][pixelCol];
            blueSum += kernel[ki][kj] * image.getBlueChannel()[pixelRow][pixelCol];
          }
        }

        red[i][j] = clamp((int) redSum);
        green[i][j] = clamp((int) greenSum);
        blue[i][j] = clamp((int) blueSum);
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Applies a convolution kernel to an image, specified by its name, and stores the resulting
   *         image.
   * The kernel is applied to each pixel and its neighbors in the image, with the result being
   * stored in the new image.
   * <p>
   * This method modifies the image stored with the given destination name by applying the kernel
   * to the image retrieved by the source name.
   * </p>
   *
   * @param kernel the convolution kernel to apply to the image.
   * @param imageName the name of the image to apply the kernel to.
   * @param destImageName the name of the resulting image that will store the kernel-applied image.
   */
  private void applyKernel(float[][] kernel, String imageName, String destImageName) {
    Image image = images.get(imageName);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];
    int kernelRadius = kernel.length / 2;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        float[] newColor = {0f, 0f, 0f};
        for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
          for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
            int pixelY = Math.min(Math.max(y + ky, 0), height - 1);
            int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
            newColor[0] += image.getRedChannel()[pixelY][pixelX] * kernel[ky + kernelRadius]
                    [kx + kernelRadius];
            newColor[1] += image.getGreenChannel()[pixelY][pixelX] * kernel[ky + kernelRadius]
                    [kx + kernelRadius];
            newColor[2] += image.getBlueChannel()[pixelY][pixelX] * kernel[ky + kernelRadius]
                    [kx + kernelRadius];
          }
        }
        red[y][x] = clamp(Math.round(newColor[0]));
        green[y][x] = clamp(Math.round(newColor[1]));
        blue[y][x] = clamp(Math.round(newColor[2]));
      }
    }

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  @Override
  public void histogram(String imageName, String destImageName) {
    // Check if the image exists in processedImages or original images
    BufferedImageConverter converter = new BufferedImageConverter();
    Image image = processedImages.containsKey(imageName) ?
            converter.toCustomImage(processedImages.get(imageName)) : getImage(imageName);


    if (image == null) {
      throw new IllegalArgumentException("Image not found in both processedImages and " +
              "original imageMap");
    }

    int[][] histograms = calculateHistograms(image);

    BufferedImage histogramImage = createHistogramImage(histograms);
    processedImages.put(destImageName, histogramImage);
  }

  /**
   * Calculates the histograms for the red, green, and blue color channels of an image.
   * The histograms are arrays of size 256, where each index corresponds to the intensity value
   * of the respective color channel. The value at each index represents the frequency of that
   * intensity value in the respective color channel.
   *
   * @param image the {@link Image} object for which the histograms are calculated.
   * @return a 2D array containing the histograms for the red, green, and blue channels.
   *         The first array represents the red channel, the second represents the green channel,
   *         and the third represents the blue channel.
   */
  private int[][] calculateHistograms(Image image) {
    int[] redHist = new int[256];
    int[] greenHist = new int[256];
    int[] blueHist = new int[256];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        redHist[image.getRedChannel()[i][j]]++;
        greenHist[image.getGreenChannel()[i][j]]++;
        blueHist[image.getBlueChannel()[i][j]]++;
      }
    }
    return new int[][]{redHist, greenHist, blueHist};
  }

  /**
   * Draws a line graph of a histogram on the provided graphics context.
   * The line graph represents the frequency of pixel intensities across all bins (0 to 255).
   * The graph is drawn in the specified color, with the y-axis representing intensity frequency
   * and the x-axis representing the intensity levels.
   *
   * @param g the {@link Graphics2D} object used to draw the line graph.
   * @param histogram the histogram data representing frequency of intensity levels.
   * @param color the color in which the histogram graph is drawn.
   */
  private void drawLineGraph(Graphics2D g, int[] histogram, Color color) {
    g.setColor(color);
    int max = Arrays.stream(histogram).max().orElse(1);

    int prevX = 0;
    int prevY = 256;

    for (int x = 0; x < histogram.length; x++) {
      int height = (int) (((double) histogram[x] / max) * 256);
      int y = 256 - height; // Invert height for correct top-down drawing

      if (x > 0) {
        g.drawLine(prevX, prevY, x, y);
      }

      prevX = x;
      prevY = y;
    }
  }

  /**
   * Creates a {@link BufferedImage} that visually represents the histograms for the red, green,
   * and blue channels of an image as line graphs. The image will have a size of 256x256 pixels,
   * with each color channel's histogram displayed in a corresponding color.
   * <p>
   * The background is white, and grid lines are drawn for visual reference.
   * </p>
   *
   * @param histograms a 2D array where each row contains the histogram data for one of the
   *                   color channels (red, green, blue). The first row is for red, the second
   *                   for green, and the third for blue.
   * @return a {@link BufferedImage} object containing the visual representation of the histograms.
   */
  private BufferedImage createHistogramImage(int[][] histograms) {
    BufferedImage histogramImage = new BufferedImage(256, 256,
            BufferedImage.TYPE_INT_RGB);
    Graphics2D g = (Graphics2D) histogramImage.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, 256, 256);


    g.setColor(Color.LIGHT_GRAY);
    for (int i = 0; i <= 256; i += 64) {
      g.drawLine(i, 0, i, 256);
      g.drawLine(0, 256 - i, 256, 256 - i);
    }


    drawLineGraph(g, histograms[0], Color.RED);
    drawLineGraph(g, histograms[1], Color.GREEN);
    drawLineGraph(g, histograms[2], Color.BLUE);

    return histogramImage;
  }

  @Override
  public void colorCorrect(String imageName, String destImageName) {
    Image image = getImage(imageName);
    int[][] histograms = calculateHistograms(image);
    int[] peaks = findPeaks(histograms);
    int averagePeak = (peaks[0] + peaks[1] + peaks[2]) / 3;

    Image correctedImage = adjustImageColors(image, peaks, averagePeak);

    BufferedImageConverter converter = new BufferedImageConverter();
    processedImages.put(destImageName, converter.toBufferedImage(correctedImage));
  }

  /**
   * Performs color correction on an image by adjusting the color channels based on the histogram
   * peaks.
   * The method calculates the histograms of the image, finds the peaks of the red, green, and blue
   * channels,
   * and adjusts the pixel values in each channel to balance the image colors towards the average
   * peak.
   *
   * @param image the {@link Image} object to be color corrected.
   * @return a new {@link Image} object with the color-corrected pixel values.
   */
  private Image colorCorrectImage(Image image) {

    int[][] histograms = calculateHistograms(image);
    int[] peaks = findPeaks(histograms);
    int averagePeak = (peaks[0] + peaks[1] + peaks[2]) / 3;


    return adjustImageColors(image, peaks, averagePeak);
  }

  /**
   * Adjusts the color channels of an image based on the peak values and the average peak.
   * Each color channel (red, green, blue) is adjusted by adding the difference between the
   * average peak and the respective channel peak to each pixel value.
   *
   * @param image the {@link Image} object whose color channels are to be adjusted.
   * @param peaks an array containing the peaks of the red, green, and blue channels.
   * @param averagePeak the average of the peaks for the red, green, and blue channels.
   * @return a new {@link Image} object with adjusted color channels.
   */
  private Image adjustImageColors(Image image, int[] peaks, int averagePeak) {
    int[][] red = adjustChannel(image.getRedChannel(), peaks[0], averagePeak);
    int[][] green = adjustChannel(image.getGreenChannel(), peaks[1], averagePeak);
    int[][] blue = adjustChannel(image.getBlueChannel(), peaks[2], averagePeak);

    return new Image(image.getWidth(), image.getHeight(), red, green, blue);
  }

  /**
   * Adjusts a single color channel by shifting its pixel values towards the average peak.
   * The adjustment is done by adding the difference between the average peak and the channel peak
   * to each pixel value, and then clamping the result to the valid intensity range (0-255).
   *
   * @param channel the 2D array representing the color channel to be adjusted.
   * @param peak the peak value of the channel.
   * @param averagePeak the average peak value of the three color channels.
   * @return a 2D array representing the adjusted color channel.
   */
  private int[][] adjustChannel(int[][] channel, int peak, int averagePeak) {
    int[][] adjustedChannel = new int[channel.length][channel[0].length];
    for (int i = 0; i < channel.length; i++) {
      for (int j = 0; j < channel[i].length; j++) {
        adjustedChannel[i][j] = clamp(channel[i][j] + (averagePeak - peak));
      }
    }
    return adjustedChannel;
  }

  /**
   * Finds the peak values of the red, green, and blue color channels by analyzing their histograms.
   * The peak value is the intensity value that has the highest frequency in each channel.
   *
   * @param histograms a 2D array containing the histograms of the red, green, and blue color
   *                   channels.
   * @return an array containing the peak values for the red, green, and blue channels.
   */
  private int[] findPeaks(int[][] histograms) {
    return new int[]{findPeak(histograms[0]), findPeak(histograms[1]), findPeak(histograms[2])};
  }

  /**
   * Finds the peak value in a given histogram by identifying the intensity value with the highest
   * frequency.
   * The method considers intensity values between 10 and 245 to avoid edge effects in the
   * histogram.
   *
   * @param histogram the histogram of a single color channel.
   * @return the intensity value (index) that represents the peak of the histogram.
   */
  private int findPeak(int[] histogram) {
    int peak = 0;
    for (int i = 10; i < 245; i++) {
      if (histogram[i] > histogram[peak]) {
        peak = i;
      }
    }
    return peak;
  }

  @Override
  public void levelsAdjust(int black, int mid, int white, String imageName, String destImageName) {
    // Validate input points
    if (black < 0 || black > 255 || mid < 0 || mid > 255 || white < 0 || white > 255) {
      throw new IllegalArgumentException("Black, mid, and white points must be in range [0, 255].");
    }
    if (!(black < mid && mid < white)) {
      throw new IllegalArgumentException("Black point must be less than mid point, " +
              "and mid point must be less than white point.");
    }

    // Retrieve the original image
    Image image = getImage(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }

    // Adjust levels for the image
    Image adjustedImage = adjustLevels(image, black, mid, white);

    // Convert to BufferedImage and store the adjusted image
    BufferedImageConverter converter = new BufferedImageConverter();
    processedImages.put(destImageName, converter.toBufferedImage(adjustedImage));
  }

  /**
   * Adjusts the levels (black, mid, white) for each color channel in the given image.
   *
   * @param image the {@link Image} object to adjust.
   * @param black the black point (darkest value).
   * @param mid   the mid point (neutral value).
   * @param white the white point (brightest value).
   * @return a new {@link Image} with adjusted levels for each color channel.
   */
  private Image adjustLevels(Image image, int black, int mid, int white) {
    int[][] adjustedRed = adjustLevelForChannel(image.getRedChannel(), black, mid, white);
    int[][] adjustedGreen = adjustLevelForChannel(image.getGreenChannel(), black, mid, white);
    int[][] adjustedBlue = adjustLevelForChannel(image.getBlueChannel(), black, mid, white);

    return new Image(image.getWidth(), image.getHeight(), adjustedRed, adjustedGreen, adjustedBlue);
  }

  /**
   * Adjusts a single color channel using levels adjustment logic.
   *
   * @param channel the color channel to adjust.
   * @param black   the black point (darkest value).
   * @param mid     the mid point (neutral value).
   * @param white   the white point (brightest value).
   * @return the adjusted channel.
   */
  private int[][] adjustLevelForChannel(int[][] channel, int black, int mid, int white) {
    int[][] adjustedChannel = new int[channel.length][channel[0].length];
    for (int i = 0; i < channel.length; i++) {
      for (int j = 0; j < channel[i].length; j++) {
        adjustedChannel[i][j] = adjustLevel(channel[i][j], black, mid, white);
      }
    }
    return adjustedChannel;
  }

  /**
   * Adjusts a single pixel value based on the provided black, mid, and white points.
   *
   * @param pixelValue the pixel value to adjust.
   * @param black      the black point (darkest value).
   * @param mid        the mid point (neutral value).
   * @param white      the white point (brightest value).
   * @return the adjusted pixel value, clamped to the range [0, 255].
   */
  private int adjustLevel(int pixelValue, int black, int mid, int white) {
    // Compute quadratic coefficients for the black-to-mid range
    double aValue = 128.0 / ((mid - black) * (mid - black));
    double bValue = -2.0 * aValue * black;
    double cValue = aValue * black * black;

    // Debugging: Verify the quadratic curve
    if (Math.abs(aValue * black * black + bValue * black + cValue) > 1e-6 ||
            Math.abs(aValue * mid * mid + bValue * mid + cValue - 128) > 1e-6) {
      throw new IllegalStateException(
              "Curve fitting failed: f(b) = 0, f(m) = 128 not satisfied. Check coefficients.");
    }

    if (pixelValue <= black) {
      return 0; // Clamp dark values to 0
    } else if (pixelValue >= white) {
      return 255; // Clamp bright values to 255
    } else if (pixelValue <= mid) {
      // Apply quadratic transformation for the shadow-to-mid range
      return clamp((int) Math.round(aValue * pixelValue * pixelValue + bValue * pixelValue + cValue));
    } else {
      // Apply linear transformation for the mid-to-highlight range
      double linearSlope = 127.0 / (white - mid);
      return clamp((int) Math.round(128 + linearSlope * (pixelValue - mid)));
    }
  }


  @Override
  public void splitOperation(String operation, String imageName, String destImageName,
                             Integer splitPercentage, Map<String, Object> additionalParams) {
    if (splitPercentage == null || splitPercentage < 0 || splitPercentage > 100) {
      throw new IllegalArgumentException("Split percentage must be between 0 and 100.");
    }

    int splitIndex = (getImage(imageName).getWidth() * splitPercentage) / 100;

    switch (operation.toLowerCase()) {
      case "blur":
        splitKernelOperation(imageName, destImageName, splitIndex, new float[][]{
                {1 / 16f, 1 / 8f, 1 / 16f}, {1 / 8f, 1 / 4f, 1 / 8f}, {1 / 16f, 1 / 8f, 1 / 16f}});
        break;
      case "sharpen":
        splitKernelOperation(imageName, destImageName, splitIndex, new float[][]{
                {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
                {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f}, {-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f},
                {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
                {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}});
        break;
      case "sepia":
        splitSepia(imageName, destImageName, splitIndex);
        break;
      case "greyscale":
        splitGreyscale(imageName, destImageName, splitIndex);
        break;
      case "colorcorrect":
        splitColorCorrection(imageName, destImageName, splitIndex);
        break;
      case "levels":
        if (additionalParams == null || !additionalParams.containsKey("black") ||
                !additionalParams.containsKey("mid") || !additionalParams.containsKey("white")) {
          throw new IllegalArgumentException("Missing parameters for levels operation.");
        }

        int black = (int) additionalParams.get("black");
        int mid = (int) additionalParams.get("mid");
        int white = (int) additionalParams.get("white");

        splitLevelsAdjust(imageName, destImageName, splitIndex, black, mid, white);
        break;
      default:
        throw new IllegalArgumentException("Unknown operation: " + operation);
    }
  }

  /**
   * Applies a kernel to an image, and then splits the result horizontally at the specified index.
   * The pixels to the left of the split index are modified using the transformed image (after
   * applying the kernel),
   * and the pixels to the right of the split index remain unchanged from the original image.
   *
   * @param imageName the name of the image to be processed.
   * @param destImageName the name of the resulting image with the split applied.
   * @param splitIndex the index at which the image is split.
   * @param kernel the kernel to apply to the image.
   */
  private void splitKernelOperation(String imageName, String destImageName,
                                    int splitIndex, float[][] kernel) {
    Image image = images.get(imageName);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    Image transformed = applyKernel(kernel, image);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitIndex) {
          red[i][j] = transformed.getRedChannel()[i][j];
          green[i][j] = transformed.getGreenChannel()[i][j];
          blue[i][j] = transformed.getBlueChannel()[i][j];
        } else {
          red[i][j] = image.getRedChannel()[i][j];
          green[i][j] = image.getGreenChannel()[i][j];
          blue[i][j] = image.getBlueChannel()[i][j];
        }
      }
    }

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  /**
   * Applies a sepia filter to an image, and then splits the result horizontally at the specified
   * index.
   * The pixels to the left of the split index are modified using the sepia-filtered image,
   * and the pixels to the right of the split index remain unchanged from the original image.
   *
   * @param imageName the name of the image to be processed.
   * @param destImageName the name of the resulting image with the split applied.
   * @param splitIndex the index at which the image is split.
   */
  private void splitSepia(String imageName, String destImageName, int splitIndex) {
    Image image = images.get(imageName);
    Image sepiaImage = applySepiaFilter(image);

    int[][] red = new int[image.getHeight()][image.getWidth()];
    int[][] green = new int[image.getHeight()][image.getWidth()];
    int[][] blue = new int[image.getHeight()][image.getWidth()];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        if (j < splitIndex) {
          red[i][j] = sepiaImage.getRedChannel()[i][j];
          green[i][j] = sepiaImage.getGreenChannel()[i][j];
          blue[i][j] = sepiaImage.getBlueChannel()[i][j];
        } else {
          red[i][j] = image.getRedChannel()[i][j];
          green[i][j] = image.getGreenChannel()[i][j];
          blue[i][j] = image.getBlueChannel()[i][j];
        }
      }
    }

    images.put(destImageName, new Image(image.getWidth(), image.getHeight(), red, green, blue));
  }

  /**
   * Converts an image to greyscale, and then splits the result horizontally at the specified index.
   * The pixels to the left of the split index are modified using the greyscale image,
   * and the pixels to the right of the split index remain unchanged from the original image.
   *
   * @param imageName the name of the image to be processed.
   * @param destImageName the name of the resulting image with the split applied.
   * @param splitIndex the index at which the image is split.
   */
  private void splitGreyscale(String imageName, String destImageName, int splitIndex) {
    Image image = images.get(imageName);
    Image greyscaleImage = convertToGreyscale(image);

    int[][] red = new int[image.getHeight()][image.getWidth()];
    int[][] green = new int[image.getHeight()][image.getWidth()];
    int[][] blue = new int[image.getHeight()][image.getWidth()];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        if (j < splitIndex) {
          red[i][j] = greyscaleImage.getRedChannel()[i][j];
          green[i][j] = greyscaleImage.getGreenChannel()[i][j];
          blue[i][j] = greyscaleImage.getBlueChannel()[i][j];
        } else {
          red[i][j] = image.getRedChannel()[i][j];
          green[i][j] = image.getGreenChannel()[i][j];
          blue[i][j] = image.getBlueChannel()[i][j];
        }
      }
    }

    images.put(destImageName, new Image(image.getWidth(), image.getHeight(), red, green, blue));
  }

  /**
   * Applies color correction to an image, and then splits the result horizontally at the specified
   * index.
   * The pixels to the left of the split index are modified using the color-corrected image,
   * and the pixels to the right of the split index remain unchanged from the original image.
   *
   * @param imageName the name of the image to be processed.
   * @param destImageName the name of the resulting image with the split applied.
   * @param splitIndex the index at which the image is split.
   */
  private void splitColorCorrection(String imageName, String destImageName, int splitIndex) {
    Image image = images.get(imageName);
    Image correctedImage = colorCorrectImage(image);

    int[][] red = new int[image.getHeight()][image.getWidth()];
    int[][] green = new int[image.getHeight()][image.getWidth()];
    int[][] blue = new int[image.getHeight()][image.getWidth()];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        if (j < splitIndex) {
          red[i][j] = correctedImage.getRedChannel()[i][j];
          green[i][j] = correctedImage.getGreenChannel()[i][j];
          blue[i][j] = correctedImage.getBlueChannel()[i][j];
        } else {
          red[i][j] = image.getRedChannel()[i][j];
          green[i][j] = image.getGreenChannel()[i][j];
          blue[i][j] = image.getBlueChannel()[i][j];
        }
      }
    }


    images.put(destImageName, new Image(image.getWidth(), image.getHeight(), red, green, blue));
  }

  /**
   * Adjusts the levels of an image up to a specified vertical split index and
   * combines the adjusted and unadjusted sections into a new image.
   *
   * @param imageName     the name of the source image to adjust.
   * @param destImageName the name to assign to the resulting adjusted image.
   * @param splitIndex    the column index dividing adjusted and unadjusted regions.
   * @param black         the black level for adjustment.
   * @param mid           the mid-tone level for adjustment.
   * @param white         the white level for adjustment.
   */
  private void splitLevelsAdjust(String imageName, String destImageName, int splitIndex, int black,
                                 int mid, int white) {
    Image image = images.get(imageName);
    Image adjustedImage = adjustLevels(image, black, mid, white);

    int[][] red = new int[image.getHeight()][image.getWidth()];
    int[][] green = new int[image.getHeight()][image.getWidth()];
    int[][] blue = new int[image.getHeight()][image.getWidth()];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        if (j < splitIndex) {
          red[i][j] = adjustedImage.getRedChannel()[i][j];
          green[i][j] = adjustedImage.getGreenChannel()[i][j];
          blue[i][j] = adjustedImage.getBlueChannel()[i][j];
        } else {
          red[i][j] = image.getRedChannel()[i][j];
          green[i][j] = image.getGreenChannel()[i][j];
          blue[i][j] = image.getBlueChannel()[i][j];
        }
      }
    }

    images.put(destImageName, new Image(image.getWidth(), image.getHeight(), red, green, blue));
  }

  @Override
  public void compress(double percentage, String imageName, String destImageName) {
    // Retrieve the image
    Image image = images.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("Compression percentage must be between 0 and 100.");
    }

    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();


    int[][][] paddedPixelData = padImage(image);


    int[][][] transformedData = haarTransform(paddedPixelData);


    applyCompression(transformedData, percentage);


    int[][][] compressedData = inverseHaarTransform(transformedData);


    int[][][] unpaddedData = unpadImage(compressedData, originalHeight, originalWidth);


    saveCompressedImage(destImageName, originalHeight, originalWidth, unpaddedData);
  }


  /**
   * Saves the compressed image by storing the pixel data in the specified image object.
   * The compressed data is first clamped to ensure pixel values are within valid color range
   * (0-255), then assigned to the red, green, and blue channels.
   *
   * @param destImageName the name of the destination image where the result is stored.
   * @param height the height of the image.
   * @param width the width of the image.
   * @param compressedData the compressed pixel data (3D array where each pixel contains RGB
   *                       values).
   */
  private void saveCompressedImage(String destImageName, int height, int width,
                                   int[][][] compressedData) {
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        red[y][x] = clamp(compressedData[y][x][0]);
        green[y][x] = clamp(compressedData[y][x][1]);
        blue[y][x] = clamp(compressedData[y][x][2]);
      }
    }

    images.put(destImageName, new Image(width, height, red, green, blue));
  }

  /**
   * Pads the image to ensure both dimensions (width and height) are even numbers.
   * If either dimension is odd, an extra row or column is added.
   * This is necessary for applying Haar transform, which requires even dimensions.
   *
   * @param image the image to be padded.
   * @return a 3D array representing the padded image data, where each pixel contains RGB values.
   */
  private int[][][] padImage(Image image) {
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    int paddedWidth = (originalWidth % 2 == 0) ? originalWidth : originalWidth + 1;
    int paddedHeight = (originalHeight % 2 == 0) ? originalHeight : originalHeight + 1;

    int[][][] paddedData = new int[paddedHeight][paddedWidth][3];
    for (int y = 0; y < originalHeight; y++) {
      for (int x = 0; x < originalWidth; x++) {
        paddedData[y][x][0] = image.getRedChannel()[y][x];
        paddedData[y][x][1] = image.getGreenChannel()[y][x];
        paddedData[y][x][2] = image.getBlueChannel()[y][x];
      }
    }
    return paddedData;
  }

  /**
   * Pads the image to ensure both dimensions (width and height) are even numbers.
   * If either dimension is odd, an extra row or column is added.
   * This is necessary for applying Haar transform, which requires even dimensions.
   *
   * @param data the image to be padded.
   * @return a 3D array representing the padded image data, where each pixel contains RGB values.
   */
  private int[][][] haarTransform(int[][][] data) {
    int height = data.length;
    int width = data[0].length;
    int[][][] transformed = new int[height][width][3];

    for (int channel = 0; channel < 3; channel++) {
      for (int y = 0; y < height - 1; y += 2) {
        for (int x = 0; x < width - 1; x += 2) {
          applyHaarToBlock(data, transformed, y, x, channel);
        }
      }
    }
    return transformed;
  }

  /**
   * Applies the Haar wavelet transform to a single 2x2 block of pixels for a specific color
   * channel.
   * The Haar transform computes four values for each block: average and differences along
   * horizontal and vertical axes.
   *
   * @param data the image data to be transformed.
   * @param transformed the resulting transformed data.
   * @param y the y-coordinate (row) of the top-left pixel of the block.
   * @param x the x-coordinate (column) of the top-left pixel of the block.
   * @param channel the color channel (0 = Red, 1 = Green, 2 = Blue) to which the transform is
   *                applied.
   */
  private void applyHaarToBlock(int[][][] data, int[][][] transformed, int y, int x,
                                int channel) {
    int a = data[y][x][channel];
    int b = data[y][x + 1][channel];
    int c = data[y + 1][x][channel];
    int d = data[y + 1][x + 1][channel];

    transformed[y / 2][x / 2][channel] = (a + b + c + d) / 4;
    transformed[y / 2][data[0].length / 2 + x / 2][channel] = (a + b - c - d) / 4;
    transformed[data.length / 2 + y / 2][x / 2][channel] = (a - b + c - d) / 4;
    transformed[data.length / 2 + y / 2][data[0].length / 2 + x / 2][channel] = (a - b - c + d) / 4;
  }

  /**
   * Applies a compression algorithm to the image data by zeroing out values below a certain
   * threshold.
   * The threshold is dynamically adjusted based on the percentage of compression.
   *
   * @param data the image data to be compressed (3D array representing RGB channels).
   * @param percentage the desired level of compression (0-100), where 0 is no compression and 100
   *                   is maximum compression.
   */
  private void applyCompression(int[][][] data, double percentage) {

    int baseThreshold = 50;
    int maxThreshold = 250;
    double compressionFactor = 0.7;
    int adjustedPercentage = (int) (percentage * compressionFactor);
    int threshold = baseThreshold + (int)
            ((maxThreshold - baseThreshold) * (adjustedPercentage / 100.0));


    for (int[][] channel : data) {
      for (int[] row : channel) {
        for (int i = 0; i < row.length; i++) {
          if (Math.abs(row[i]) < threshold) {
            row[i] = 0;
          }
        }
      }
    }
  }

  /**
   * Applies the inverse Haar wavelet transform to the image data to restore the original pixel
   * values.
   * The inverse transform works on 2x2 blocks of pixel values for each color channel (RGB).
   *
   * @param data the transformed image data to be restored.
   * @return a 3D array of restored pixel values after applying the inverse Haar wavelet transform.
   */
  private int[][][] inverseHaarTransform(int[][][] data) {
    int height = data.length;
    int width = data[0].length;
    int[][][] restored = new int[height][width][3];

    for (int channel = 0; channel < 3; channel++) {
      for (int y = 0; y < height / 2; y++) {
        for (int x = 0; x < width / 2; x++) {
          restoreBlock(data, restored, y, x, channel);
        }
      }
    }
    return restored;
  }

  /**
   * Restores a 2x2 block of pixel values using the inverse Haar wavelet transform for a specific
   * color channel.
   * The inverse transform computes four values for each block based on the transformed data.
   *
   * @param data the transformed image data to be restored.
   * @param restored the resulting restored data.
   * @param y the y-coordinate (row) of the top-left pixel of the block.
   * @param x the x-coordinate (column) of the top-left pixel of the block.
   * @param channel the color channel (0 = Red, 1 = Green, 2 = Blue) to which the inverse transform
   *                is applied.
   */
  private void restoreBlock(int[][][] data, int[][][] restored, int y, int x, int channel) {
    int a = data[y][x][channel];
    int b = data[y][data[0].length / 2 + x][channel];
    int c = data[data.length / 2 + y][x][channel];
    int d = data[data.length / 2 + y][data[0].length / 2 + x][channel];

    restored[2 * y][2 * x][channel] = a + b + c + d;
    restored[2 * y][2 * x + 1][channel] = a + b - c - d;
    restored[2 * y + 1][2 * x][channel] = a - b + c - d;
    restored[2 * y + 1][2 * x + 1][channel] = a - b - c + d;
  }

  /**
   * Removes padding from the image data to restore the original dimensions.
   * The image is restored to its original height and width by trimming the padded rows and columns.
   *
   * @param data the padded image data (3D array).
   * @param originalHeight the original height of the image before padding.
   * @param originalWidth the original width of the image before padding.
   * @return a 3D array representing the unpadded image data.
   */
  private int[][][] unpadImage(int[][][] data, int originalHeight, int originalWidth) {
    int[][][] unpaddedData = new int[originalHeight][originalWidth][3];
    for (int y = 0; y < originalHeight; y++) {
      for (int x = 0; x < originalWidth; x++) {
        unpaddedData[y][x][0] = data[y][x][0];
        unpaddedData[y][x][1] = data[y][x][1];
        unpaddedData[y][x][2] = data[y][x][2];
      }
    }
    return unpaddedData;
  }


  @Override
  public void downscaleImage(int newWidth, int newHeight, String imageName, String destImageName) {
    // Retrieve the source image
    Image sourceImage = images.get(imageName);

    if (sourceImage == null) {
      throw new IllegalArgumentException("Source image not found: " + imageName);
    }

    // Validate dimensions
    validateDownscaleDimensions(newWidth, newHeight, sourceImage);

    // Calculate scaling factors
    double xScale = calculateScalingFactor(sourceImage.getWidth(), newWidth);
    double yScale = calculateScalingFactor(sourceImage.getHeight(), newHeight);

    // Downscale each channel
    int[][] newRed = downscaleChannel(sourceImage.getRedChannel(), newWidth, newHeight,
            xScale, yScale);
    int[][] newGreen = downscaleChannel(sourceImage.getGreenChannel(), newWidth, newHeight,
            xScale, yScale);
    int[][] newBlue = downscaleChannel(sourceImage.getBlueChannel(), newWidth, newHeight,
            xScale, yScale);

    // Store the downscaled image
    images.put(destImageName, new Image(newWidth, newHeight, newRed, newGreen, newBlue));
  }

  /**
   * Validates the dimensions for downscaling the image.
   * Ensures that the new width and height are greater than 0, and smaller than or equal
   * to the original image dimensions.
   *
   * @param newWidth the new width of the image after downscaling.
   * @param newHeight the new height of the image after downscaling.
   * @param sourceImage the source image to be downscaled.
   * @throws IllegalArgumentException if the new dimensions are invalid (negative, zero, or larger
   *         than the original size).
   */
  private void validateDownscaleDimensions(int newWidth, int newHeight, Image sourceImage) {
    if (newWidth <= 0 || newHeight <= 0 || newWidth > sourceImage.getWidth() ||
            newHeight > sourceImage.getHeight()) {
      throw new IllegalArgumentException("Invalid dimensions for downscaling.");
    }
  }

  /**
   * Calculates the scaling factor for downscaling an image from the original size to a new size.
   *
   * @param originalSize the original dimension (width or height) of the image.
   * @param newSize the new dimension (width or height) after downscaling.
   * @return the scaling factor to be applied to the image.
   */
  private double calculateScalingFactor(int originalSize, int newSize) {
    return (double) originalSize / newSize;
  }

  /**
   * Downscales a single color channel (Red, Green, or Blue) of the image using bilinear
   * interpolation.
   *
   * @param channel the channel (2D array) to be downscaled.
   * @param newWidth the new width of the downscaled image.
   * @param newHeight the new height of the downscaled image.
   * @param xScale the scaling factor for the x dimension (width).
   * @param yScale the scaling factor for the y dimension (height).
   * @return a 2D array representing the downscaled color channel.
   */
  private int[][] downscaleChannel(int[][] channel, int newWidth, int newHeight,
                                   double xScale, double yScale) {
    int[][] downscaledChannel = new int[newHeight][newWidth];

    for (int y = 0; y < newHeight; y++) {
      for (int x = 0; x < newWidth; x++) {
        downscaledChannel[y][x] = calculateDownscaledPixel(channel, x, y, xScale, yScale);
      }
    }

    return downscaledChannel;
  }

  /**
   * Computes the pixel value at a specified position in a downscaled image channel
   * using bilinear interpolation.
   *
   * @param channel the 2D array of the original image channel (e.g., red, green, blue).
   * @param x       the x-coordinate in the downscaled image.
   * @param y       the y-coordinate in the downscaled image.
   * @param xScale  the scale factor along the x-axis.
   * @param yScale  the scale factor along the y-axis.
   * @return the interpolated pixel value at the specified position.
   */
  private int calculateDownscaledPixel(int[][] channel, int x, int y, double xScale,
                                       double yScale) {
    double originalX = x * xScale;
    double originalY = y * yScale;

    int x1 = (int) Math.floor(originalX);
    int x2 = Math.min(x1 + 1, channel[0].length - 1);
    int y1 = (int) Math.floor(originalY);
    int y2 = Math.min(y1 + 1, channel.length - 1);

    double xFraction = originalX - x1;
    double yFraction = originalY - y1;


    return interpolatePixel(channel, x1, x2, y1, y2, xFraction, yFraction);
  }

  /**
   * Interpolates a pixel's value using bilinear interpolation between the four nearest neighbors.
   *
   * @param channel the color channel (2D array) of the image.
   * @param x1 the x-coordinate of the top-left neighboring pixel.
   * @param x2 the x-coordinate of the top-right neighboring pixel.
   * @param y1 the y-coordinate of the top-left neighboring pixel.
   * @param y2 the y-coordinate of the bottom-right neighboring pixel.
   * @param xFraction the fractional distance of the pixel along the x-axis.
   * @param yFraction the fractional distance of the pixel along the y-axis.
   * @return the interpolated pixel value.
   */
  private int interpolatePixel(int[][] channel, int x1, int x2, int y1, int y2, double xFraction,
                               double yFraction) {
    int topLeft = channel[y1][x1];
    int topRight = channel[y1][x2];
    int bottomLeft = channel[y2][x1];
    int bottomRight = channel[y2][x2];

    int top = (int) ((1 - xFraction) * topLeft + xFraction * topRight);
    int bottom = (int) ((1 - xFraction) * bottomLeft + xFraction * bottomRight);
    return (int) ((1 - yFraction) * top + yFraction * bottom);
  }


  /**
   * Applies an operation to an image using a mask image. The mask determines which pixels are
   * affected.
   * If the mask value at a given pixel is zero, the operation is applied; otherwise, the original
   * pixel value is kept.
   *
   * @param sourceImage the source image to which the operation is applied.
   * @param maskImage the mask image that determines which pixels are modified.
   * @param operation the operation to be applied (e.g., blur, sharpen, sepia).
   * @param kernel the kernel used for the operation (if applicable, e.g., for blur or sharpen).
   * @param component the component to operate on (e.g., 'red', 'green', 'blue', 'value',
   *                  'intensity').
   * @return a new image with the operation applied.
   */
  private Image applyWithMask(Image sourceImage, Image maskImage, String operation,
                              float[][] kernel, String component) {
    int width = sourceImage.getWidth();
    int height = sourceImage.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int maskValue = maskImage != null ? maskImage.getRedChannel()[i][j] : 0;
        int[] newPixel;

        if (maskValue == 0) {
          newPixel = executeOperation(operation, kernel, component, i, j, sourceImage);
        } else {
          newPixel = new int[]{sourceImage.getRedChannel()[i][j],
                  sourceImage.getGreenChannel()[i][j], sourceImage.getBlueChannel()[i][j]};
        }

        red[i][j] = newPixel[0];
        green[i][j] = newPixel[1];
        blue[i][j] = newPixel[2];
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Executes the specified operation on a given pixel of the image.
   * Supported operations include blur, sharpen, sepia, and component-based transformations (e.g.,
   * red, green, blue).
   *
   * @param operation the operation to be applied (e.g., "blur", "sharpen", "sepia", "component").
   * @param kernel the kernel used for blur or sharpen operations (if applicable).
   * @param component the color component to operate on (if applicable, e.g., "value", "intensity").
   * @param row the row index of the pixel in the image.
   * @param col the column index of the pixel in the image.
   * @param sourceImage the source image on which the operation is performed.
   * @return an array representing the new RGB values of the pixel after applying the operation.
   * @throws IllegalArgumentException if the operation is unknown.
   */
  private int[] executeOperation(String operation, float[][] kernel, String component, int row,
                                 int col, Image sourceImage) {
    switch (operation.toLowerCase()) {
      case "blur":
      case "sharpen":
        return applyKernelToPixel(row, col, kernel, sourceImage);
      case "sepia":
        return applySepiaToPixel(row, col, sourceImage);
      case "component":
        return applyComponentToPixel(row, col, sourceImage, component);
      default:
        throw new IllegalArgumentException("Unknown operation: " + operation);
    }
  }

  /**
   * Applies the sepia effect to a pixel. The sepia effect is a color filter that gives an image a
   * warm, brownish tone.
   *
   * @param row the row index of the pixel in the image.
   * @param col the column index of the pixel in the image.
   * @param sourceImage the source image on which the sepia effect is applied.
   * @return an array containing the new RGB values of the pixel after applying the sepia effect.
   */
  private int[] applySepiaToPixel(int row, int col, Image sourceImage) {
    int r = sourceImage.getRedChannel()[row][col];
    int g = sourceImage.getGreenChannel()[row][col];
    int b = sourceImage.getBlueChannel()[row][col];

    int newRed = clamp((int) (0.393 * r + 0.769 * g + 0.189 * b));
    int newGreen = clamp((int) (0.349 * r + 0.686 * g + 0.168 * b));
    int newBlue = clamp((int) (0.272 * r + 0.534 * g + 0.131 * b));

    return new int[]{newRed, newGreen, newBlue};
  }

  /**
   * Applies a transformation to a pixel based on the specified color component.
   * Components can be 'value', 'intensity', 'luma', or any individual color channel ('red',
   * 'green', 'blue').
   *
   * @param row the row index of the pixel in the image.
   * @param col the column index of the pixel in the image.
   * @param image the image on which the component transformation is applied.
   * @param component the component to apply (e.g., 'value', 'intensity', 'luma', 'red', 'green',
   *                  'blue').
   * @return an array containing the RGB values of the transformed pixel.
   * @throws IllegalArgumentException if the component is unknown.
   */
  private int[] applyComponentToPixel(int row, int col, Image image, String component) {
    int r = image.getRedChannel()[row][col];
    int g = image.getGreenChannel()[row][col];
    int b = image.getBlueChannel()[row][col];

    switch (component.toLowerCase()) {
      case "value":
        int maxValue = Math.max(r, Math.max(g, b));
        return new int[]{maxValue, maxValue, maxValue};
      case "intensity":
        int avgValue = (r + g + b) / 3;
        return new int[]{avgValue, avgValue, avgValue};
      case "luma":
        int lumaValue = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
        return new int[]{lumaValue, lumaValue, lumaValue};
      case "red":
        return new int[]{r, 0, 0};
      case "green":
        return new int[]{0, g, 0};
      case "blue":
        return new int[]{0, 0, b};
      default:
        throw new IllegalArgumentException("Unknown component: " + component);
    }
  }

  /**
   * Applies a convolution kernel to a pixel in the image, taking into account its neighboring
   * pixels.
   * The kernel is used to perform operations like blurring or sharpening.
   *
   * @param row the row index of the pixel in the image.
   * @param col the column index of the pixel in the image.
   * @param kernel the convolution kernel applied to the pixel and its neighbors.
   * @param image the image on which the kernel is applied.
   * @return an array containing the new RGB values of the pixel after the kernel operation.
   */
  private int[] applyKernelToPixel(int row, int col, float[][] kernel, Image image) {
    int kernelSize = kernel.length;
    int offset = kernelSize / 2;

    float redSum = 0;
    float greenSum = 0;
    float blueSum = 0;

    for (int ki = 0; ki < kernelSize; ki++) {
      for (int kj = 0; kj < kernelSize; kj++) {
        int pixelRow = Math.min(Math.max(row + ki - offset, 0), image.getHeight() - 1);
        int pixelCol = Math.min(Math.max(col + kj - offset, 0), image.getWidth() - 1);

        redSum += kernel[ki][kj] * image.getRedChannel()[pixelRow][pixelCol];
        greenSum += kernel[ki][kj] * image.getGreenChannel()[pixelRow][pixelCol];
        blueSum += kernel[ki][kj] * image.getBlueChannel()[pixelRow][pixelCol];
      }
    }

    return new int[]{clamp(Math.round(redSum)), clamp(Math.round(greenSum)),
            clamp(Math.round(blueSum))};
  }

  @Override
  public boolean imageExists(String imageName) {
    return images.containsKey(imageName);
  }

  /**
   * Executes a specified operation on an image with an optional mask, and stores the result in a
   * destination image.
   * The operation can be applied to the source image, while the mask determines which pixels are
   * modified.
   * If the mask is provided, the operation is only applied to pixels where the mask value is
   * 'non-zero.
   *
   * @param operation the operation to be applied to the source image (e.g., "blur", "sharpen",
   *                  "sepia", "component").
   * @param kernel the kernel to be used for operations like blur or sharpen (if applicable).
   * @param component the color component to operate on (e.g., "value", "intensity", "luma", "red",
   *                  "green", "blue").
   * @param sourceImageName the name of the source image in the image collection.
   * @param maskName the name of the mask image in the image collection (can be null if no mask is
   *                 used).
   * @param destImageName the name of the destination image to store the result of the operation.
   * @throws IllegalArgumentException if the source image or mask image is not found.
   */
  public void executeOperationWithMask(String operation, float[][] kernel, String component,
                                        String sourceImageName, String maskName,
                                        String destImageName) {
    Image sourceImage = images.get(sourceImageName);
    Image maskImage = null;
    if (maskName != null) {
      maskImage = images.get(maskName);
      if (maskImage == null) {
        throw new IllegalArgumentException("Mask image not found: " + maskName);
      }
    }

    if (sourceImage == null) {
      throw new IllegalArgumentException("Source image not found: " + sourceImageName);
    }

    Image result = applyWithMask(sourceImage, maskImage, operation, kernel, component);
    images.put(destImageName, result);
  }
}
