package controller.command;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to apply a split-view operation on an image.
 */
public class SplitCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public SplitCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error: Provide operation, source image name, "
              + "destination image name, and split percentage.");
      return;
    }

    String operation = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    int splitPercentage;

    try {
      splitPercentage = Integer.parseInt(tokenizer.nextToken());
    } catch (NumberFormatException e) {
      view.renderMessage("Error: Split percentage must be a valid integer.");
      return;
    }

    Map<String, Object> additionalParams = null;
    if (operation.equalsIgnoreCase("levels")) {
      if (tokenizer.countTokens() < 3) {
        view.renderMessage("Error: Provide black, mid, and white levels for the 'levels'" +
                " operation.");
        return;
      }

      try {
        int black = Integer.parseInt(tokenizer.nextToken());
        int mid = Integer.parseInt(tokenizer.nextToken());
        int white = Integer.parseInt(tokenizer.nextToken());
        additionalParams = new HashMap<>();
        additionalParams.put("black", black);
        additionalParams.put("mid", mid);
        additionalParams.put("white", white);
      } catch (NumberFormatException e) {
        view.renderMessage("Error: Levels parameters must be valid integers.");
        return;
      }
    }

    try {
      model.splitOperation(operation, imageName, destImageName, splitPercentage, additionalParams);
      view.renderMessage(operation + " with split applied to: " + destImageName);
    } catch (IllegalArgumentException e) {
      view.renderMessage("Error: " + e.getMessage());
    }
  }
}
