package model.factory;

import model.Image;

import java.io.IOException;

/**
 * Interface for loading images from file paths.
 * Implementations handle different image formats.
 */
public interface ImageLoader {

  /**
   * Loads an image from the specified file path.
   *
   * @param filePath the path to the image file to be loaded.
   * @return an {@link Image} object representing the loaded image.
   * @throws IOException if an error occurs while reading the image file.
   */
  Image load(String filePath) throws IOException;
}
