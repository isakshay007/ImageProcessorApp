package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to downscale an image to new dimensions.
 */
public class DownscaleCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public DownscaleCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error: Provide new width, new height, source image name, and "
              + "destination image name.");
      return;
    }
    int newWidth = Integer.parseInt(tokenizer.nextToken());
    int newHeight = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.downscaleImage(newWidth, newHeight, imageName, destImageName);
    view.renderMessage("Downscaled image " + imageName + " to " + newWidth + "x"
            + newHeight + ": " + destImageName);
  }
}
