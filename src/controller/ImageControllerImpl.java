package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import controller.command.BasicOperationCommand;
import controller.command.BrightenCommand;
import controller.command.ChannelCommand;
import controller.command.ColorCorrectCommand;
import controller.command.CompressCommand;
import controller.command.ComponentCommand;
import controller.command.DownscaleCommand;
import controller.command.FlipCommand;
import controller.command.GreyscaleCommand;
import controller.command.HistogramCommand;
import controller.command.ImageCommand;
import controller.command.LevelsAdjustCommand;
import controller.command.LoadCommand;
import controller.command.RgbCombineCommand;
import controller.command.RgbSplitCommand;
import controller.command.RunScriptCommand;
import controller.command.SaveCommand;
import controller.command.SplitCommand;

import model.ImageModel;
import view.ImageView;

/**
 * Implementation of the ImageController interface that handles image processing commands.
 * This class interacts with the model to perform various image operations and communicates
 * results to the view.
 */
public class ImageControllerImpl implements ImageController {
  private final ImageModel model;
  private final ImageView view;
  private final Map<String, ImageCommand> commands;

  /**
   * Constructs an ImageControllerImpl with the specified model and view.
   *
   * @param model the image model that performs the actual image processing.
   * @param view  the view that displays messages to the user.
   */
  public ImageControllerImpl(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
    this.commands = new HashMap<>();
    initCommands();
  }

  private void initCommands() {
    commands.put("load", new LoadCommand(model, view));
    commands.put("save", new SaveCommand(model, view));
    commands.put("red-component", new ChannelCommand(model, view, "red-component"));
    commands.put("green-component", new ChannelCommand(model, view, "green-component"));
    commands.put("blue-component", new ChannelCommand(model, view, "blue-component"));
    commands.put("value-component", new ComponentCommand(model, view, "value-component"));
    commands.put("intensity-component", new ComponentCommand(model, view,
            "intensity-component"));
    commands.put("luma-component", new ComponentCommand(model, view, "luma-component"));
    commands.put("flip", new FlipCommand(model, view));
    commands.put("brighten", new BrightenCommand(model, view));
    commands.put("blur", new BasicOperationCommand(model, view, "blur"));
    commands.put("sharpen", new BasicOperationCommand(model, view, "sharpen"));
    commands.put("sepia", new BasicOperationCommand(model, view, "sepia"));
    commands.put("greyscale", new GreyscaleCommand(model, view));
    commands.put("rgb-split", new RgbSplitCommand(model, view));
    commands.put("rgb-combine", new RgbCombineCommand(model, view));
    commands.put("histogram", new HistogramCommand(model, view));
    commands.put("color-correct", new ColorCorrectCommand(model, view));
    commands.put("levels-adjust", new LevelsAdjustCommand(model, view));
    commands.put("split", new SplitCommand(model, view));
    commands.put("compress", new CompressCommand(model, view));
    commands.put("downscale", new DownscaleCommand(model, view));
    commands.put("run", new RunScriptCommand(this, view));
  }

  /**
   * Processes a command string that specifies an operation to be performed on an image.
   *
   * @param command the command string that specifies the image operation to execute.
   */
  @Override
  public void processCommand(String command) {
    StringTokenizer tokenizer = new StringTokenizer(command);
    if (!tokenizer.hasMoreTokens()) {
      view.renderMessage("No command entered.");
      return;
    }
    String action = tokenizer.nextToken().toLowerCase();

    try {
      ImageCommand cmd = commands.get(action);
      if (cmd != null) {
        cmd.execute(tokenizer);
      } else {
        view.renderMessage("Unknown command: " + action);
      }
    } catch (Exception e) {
      view.renderMessage("Error executing command: " + action + " - " + e.getMessage());
    }
  }

  /**
   * Runs a script file containing a series of commands for image processing.
   *
   * @param scriptFile the path to the script file to be executed.
   */
  @Override
  public void runScript(String scriptFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty() && !line.startsWith("#")) {
          view.renderMessage("Executing command from script: " + line);
          processCommand(line);
        }
      }
      view.renderMessage("Script executed successfully.");
    } catch (IOException e) {
      view.renderMessage("Error reading script file: " + e.getMessage());
    }
  }
}
