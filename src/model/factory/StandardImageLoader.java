package model.factory;

import model.BufferedImageConverter;
import model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Loads images in standard formats (PNG, JPG, JPEG) using ImageIO.
 */
public class StandardImageLoader implements ImageLoader {
  private final BufferedImageConverter bufferedImageConverter;

  public StandardImageLoader() {
    this.bufferedImageConverter = new BufferedImageConverter();
  }

  @Override
  public Image load(String filePath) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(new File(filePath));
    if (bufferedImage == null) {
      throw new IOException("Unsupported image format.");
    }
    return bufferedImageConverter.toCustomImage(bufferedImage);
  }
}
