package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to flip an image horizontally or vertically.
 */
public class FlipCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public FlipCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide direction, source image name and destination image name.");
      return;
    }
    String direction = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.flip(direction, imageName, destImageName);
    view.renderMessage("Flipped image " + direction + ": " + destImageName);
  }
}
