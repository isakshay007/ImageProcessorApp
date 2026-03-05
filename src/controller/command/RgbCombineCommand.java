package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to combine RGB channels into a single image.
 */
public class RgbCombineCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public RgbCombineCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error:Provide destination image name and source names for RGB channels.");
      return;
    }
    String destImageName = tokenizer.nextToken();
    String redImageName = tokenizer.nextToken();
    String greenImageName = tokenizer.nextToken();
    String blueImageName = tokenizer.nextToken();
    model.rgbCombine(destImageName, redImageName, greenImageName, blueImageName);
    view.renderMessage("RGB combine done: " + destImageName);
  }
}
