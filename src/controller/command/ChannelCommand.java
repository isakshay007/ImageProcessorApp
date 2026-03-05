package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Shared command for channel visualization (red-component, green-component, blue-component).
 * The channel name is extracted from the action string (e.g., "red" from "red-component").
 */
public class ChannelCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;
  private final String action;

  public ChannelCommand(ImageModel model, ImageView view, String action) {
    this.model = model;
    this.view = view;
    this.action = action;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide source image name and destination image name."
              + " Optionally, include a mask image name.");
      return;
    }

    String imageName = tokenizer.nextToken();
    String nextToken = tokenizer.nextToken();
    String destImageName;

    try {
      if (tokenizer.hasMoreTokens() && model.imageExists(nextToken)) {
        String maskImageName = nextToken;
        destImageName = tokenizer.nextToken();

        model.visualizeChannel(action.split("-")[0], imageName, maskImageName, destImageName);
        view.renderMessage("Visualized " + action.split("-")[0] + " channel with mask: "
                + destImageName);
      } else {
        destImageName = nextToken;

        model.visualizeChannel(action.split("-")[0], imageName, destImageName);
        view.renderMessage("Visualized " + action.split("-")[0] + " channel: "
                + destImageName);
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage("Error processing channel visualization: " + e.getMessage());
    }
  }
}
