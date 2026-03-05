package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command for basic operations (blur, sharpen, sepia) that support optional mask images.
 */
public class BasicOperationCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;
  private final String action;

  public BasicOperationCommand(ImageModel model, ImageView view, String action) {
    this.model = model;
    this.view = view;
    this.action = action;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide at least source image name and destination image name.");
      return;
    }

    String sourceImageName = tokenizer.nextToken();
    String nextToken = tokenizer.nextToken();
    String destImageName;

    try {
      if (tokenizer.hasMoreTokens() && model.imageExists(nextToken)) {
        String maskImageName = nextToken;
        destImageName = tokenizer.nextToken();

        switch (action) {
          case "blur":
            model.blur(sourceImageName, maskImageName, destImageName);
            break;
          case "sharpen":
            model.sharpen(sourceImageName, maskImageName, destImageName);
            break;
          case "sepia":
            model.convertToSepia(sourceImageName, maskImageName, destImageName);
            break;
          default:
            view.renderMessage("Unsupported operation with mask: " + action);
            return;
        }
        view.renderMessage(action + " applied with mask to: " + destImageName);
      } else {
        destImageName = nextToken;

        switch (action) {
          case "blur":
            model.blur(sourceImageName, destImageName);
            break;
          case "sharpen":
            model.sharpen(sourceImageName, destImageName);
            break;
          case "sepia":
            model.convertToSepia(sourceImageName, destImageName);
            break;
          default:
            view.renderMessage("Unsupported operation: " + action);
            return;
        }
        view.renderMessage(action + " applied to: " + destImageName);
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage("Error processing operation: " + e.getMessage());
    }
  }
}
