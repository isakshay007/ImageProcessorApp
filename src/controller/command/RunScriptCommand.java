package controller.command;

import java.util.StringTokenizer;

import controller.ImageController;
import view.ImageView;

/**
 * Command to run a script file containing image processing commands.
 * Requires a reference to the controller to delegate script execution.
 */
public class RunScriptCommand implements ImageCommand {
  private final ImageController controller;
  private final ImageView view;

  public RunScriptCommand(ImageController controller, ImageView view) {
    this.controller = controller;
    this.view = view;
  }

  @Override
  public void execute(StringTokenizer tokenizer) throws Exception {
    if (!tokenizer.hasMoreTokens()) {
      view.renderMessage("Error: Provide the script file path for the run command.");
      return;
    }
    String scriptFile = tokenizer.nextToken();
    controller.runScript(scriptFile);
  }
}
