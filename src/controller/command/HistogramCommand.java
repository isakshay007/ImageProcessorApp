package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to generate a histogram image for a specified source image.
 */
public class HistogramCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public HistogramCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide source image name and destination image name.");
      return;
    }
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.histogram(imageName, destImageName);
    view.renderMessage("Histogram generated: " + destImageName);
  }
}
