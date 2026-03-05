package model.factory;

import model.FileHandler;
import model.Image;
import model.ImageParser;

import java.io.IOException;

/**
 * Loads images in PPM (P3) format.
 */
public class PPMLoader implements ImageLoader {
  private final FileHandler fileHandler;
  private final ImageParser imageParser;

  public PPMLoader() {
    this.fileHandler = new FileHandler();
    this.imageParser = new ImageParser();
  }

  @Override
  public Image load(String filePath) throws IOException {
    return imageParser.parsePPM(fileHandler.readFile(filePath));
  }
}
