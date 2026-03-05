package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Shared command for component visualization (value-component, intensity-component,
 * luma-component).
 * The component name is extracted from the action string.
 */
public class ComponentCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;
  private final String action;

  public ComponentCommand(ImageModel model, ImageView view, String action) {
    this.model = model;
    this.view = view;
    this.action = action;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide source image name and destination image name. "
              + "Optionally, include a mask image name.");
      return;
    }

    String imageName = tokenizer.nextToken();
    String nextToken = tokenizer.nextToken();
    String destImageName;
    String component = action.split("-")[0];

    try {
      if (tokenizer.hasMoreTokens() && model.imageExists(nextToken)) {
        String maskImageName = nextToken;
        destImageName = tokenizer.nextToken();

        model.visualizeComponent(component, imageName, maskImageName, destImageName);
        view.renderMessage("Visualized " + component + " component with mask: " + destImageName);
      } else {
        destImageName = nextToken;

        model.visualizeComponent(component, imageName, destImageName);
        view.renderMessage("Visualized " + component + " component: " + destImageName);
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage("Error processing component visualization: " + e.getMessage());
    }
  }
}
