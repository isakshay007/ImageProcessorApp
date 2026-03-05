package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to apply color correction to an image.
 */
public class ColorCorrectCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public ColorCorrectCommand(ImageModel model, ImageView view) {
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
    model.colorCorrect(imageName, destImageName);
    view.renderMessage("Color correction applied to: " + destImageName);
  }
}
