package model.strategy;

import model.Image;

/**
 * Strategy interface for image transformation operations.
 * Each implementation encapsulates a specific image processing algorithm.
 */
public interface ImageOperation {

  /**
   * Applies the image operation to the given image and returns a new transformed image.
   *
   * @param image the source image to transform.
   * @return a new {@link Image} with the operation applied.
   */
  Image apply(Image image);
}
