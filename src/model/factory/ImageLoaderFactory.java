package model.factory;

/**
 * Factory that returns the appropriate {@link ImageLoader} based on the file extension.
 */
public class ImageLoaderFactory {

  /**
   * Returns the correct loader for the given file path based on its extension.
   *
   * @param filePath the path to the image file.
   * @return an {@link ImageLoader} capable of loading the specified file format.
   * @throws UnsupportedOperationException if the file format is not supported.
   */
  public static ImageLoader getLoader(String filePath) {
    String lowerPath = filePath.toLowerCase();
    if (lowerPath.endsWith(".ppm")) {
      return new PPMLoader();
    } else if (lowerPath.endsWith(".png") || lowerPath.endsWith(".jpg")
            || lowerPath.endsWith(".jpeg")) {
      return new StandardImageLoader();
    } else {
      throw new UnsupportedOperationException("Only PPM, PNG, JPG, JPEG formats are supported.");
    }
  }
}
