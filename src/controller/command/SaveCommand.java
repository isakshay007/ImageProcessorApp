package controller.command;

import java.util.StringTokenizer;

import model.ImageModel;
import view.ImageView;

/**
 * Command to save an image to a file path.
 */
public class SaveCommand implements ImageCommand {
  private final ImageModel model;
  private final ImageView view;

  public SaveCommand(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide output path and image name for save.");
      return;
    }
    String outputPath = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    model.save(outputPath, imageName);
    view.renderMessage("Saved image: " + imageName + " to " + outputPath);
  }
}
