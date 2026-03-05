package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to brighten or darken an image by a specified amount.
 */
public class BrightenCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public BrightenCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide amount, source image name, and destination image name.");
      return;
    }
    int amount = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.brighten(amount, imageName, destImageName);
    view.renderMessage("Brightened image by " + amount + ": " + destImageName);
  }
}
