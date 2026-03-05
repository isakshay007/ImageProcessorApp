package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to compress an image by a specified percentage.
 */
public class CompressCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public CompressCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide percentage, source image name, and "
              + "destination image name.");
      return;
    }
    int percentage = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();

    model.compress(percentage, imageName, destImageName);
    view.renderMessage("Compressed image " + imageName + " by " + percentage + "%: "
            + destImageName);
  }
}
