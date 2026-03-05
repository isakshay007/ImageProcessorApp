package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to load an image from a file path.
 */
public class LoadCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public LoadCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide file path and image name for load.");
      return;
    }
    String filePath = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    model.load(filePath, imageName);
    view.renderMessage("Loaded image: " + imageName);
  }
}
