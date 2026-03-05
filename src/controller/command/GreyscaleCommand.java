package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to convert an image to greyscale using a specified component.
 */
public class GreyscaleCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public GreyscaleCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide component, source image name, and destination " +
              "image name. Optionally, include a mask image name.");
      return;
    }

    String component = tokenizer.nextToken();
    String sourceImageName = tokenizer.nextToken();
    String nextToken = tokenizer.nextToken();
    String destImageName;

    try {
      if (tokenizer.hasMoreTokens() && model.imageExists(nextToken)) {
        String maskImageName = nextToken;
        destImageName = tokenizer.nextToken();

        model.convertToGreyscale(component, sourceImageName, maskImageName, destImageName);
        view.renderMessage("Converted " + sourceImageName + " to greyscale using "
                + component + " with mask " + maskImageName + ": " + destImageName);
      } else {
        destImageName = nextToken;

        model.convertToGreyscale(component, sourceImageName, destImageName);
        view.renderMessage("Converted " + sourceImageName + " to greyscale using "
                + component + ": " + destImageName);
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage("Error processing greyscale operation: " + e.getMessage());
    }
  }
}
