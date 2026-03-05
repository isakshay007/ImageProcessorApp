package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to split an image into its RGB channels.
 */
public class RgbSplitCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public RgbSplitCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error:Provide source image name and destination names for RGB channels.");
      return;
    }
    String imageName = tokenizer.nextToken();
    String redImageName = tokenizer.nextToken();
    String greenImageName = tokenizer.nextToken();
    String blueImageName = tokenizer.nextToken();
    model.rgbSplit(imageName, redImageName, greenImageName, blueImageName);
    view.renderMessage("RGB split done: " + redImageName + ", " + greenImageName + ", "
            + blueImageName);
  }
}
