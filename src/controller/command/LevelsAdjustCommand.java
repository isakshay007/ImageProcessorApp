package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to adjust the levels (black, mid, white points) of an image.
 */
public class LevelsAdjustCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public LevelsAdjustCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 5) {
      view.renderMessage("Error: Provide black, mid, white values, source image name, "
              + "and destination image name.");
      return;
    }
    int black = Integer.parseInt(tokenizer.nextToken());
    int mid = Integer.parseInt(tokenizer.nextToken());
    int white = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();

    model.levelsAdjust(black, mid, white, imageName, destImageName);
    view.renderMessage("Levels adjustment applied to: " + destImageName);
  }
}
