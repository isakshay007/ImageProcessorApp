package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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

  /**
   * Constructs an ImageControllerImpl with the specified model and view.
   *
   * @param model the image model that performs the actual image processing.
   * @param view  the view that displays messages to the user.
   */
  public ImageControllerImpl(ImageModel model, ImageView view) {
    this.model = model;
    this.view = view;
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
      switch (action) {
        case "load":
          handleLoadImage(tokenizer);
          break;
        case "save":
          handleSaveImage(tokenizer);
          break;
        case "red-component":
        case "green-component":
        case "blue-component":
          handleVisualizeChannel(action, tokenizer);
          break;
        case "value-component":
        case "intensity-component":
        case "luma-component":
          handleVisualizeComponent(action, tokenizer);
          break;
        case "flip":
          handleFlipImage(tokenizer);
          break;
        case "brighten":
          handleBrightenImage(tokenizer);
          break;
        case "blur":
          handleBasicOperation("blur", tokenizer);
          break;
        case "sharpen":
          handleBasicOperation("sharpen", tokenizer);
          break;
        case "sepia":
          handleBasicOperation("sepia", tokenizer);
          break;
        case "greyscale":
          handleGreyscaleImage(tokenizer);
          break;
        case "rgb-split":
          handleRgbSplitImage(tokenizer);
          break;
        case "rgb-combine":
          handleRgbCombineImage(tokenizer);
          break;
        case "histogram":
          handleHistogramImage(tokenizer);
          break;
        case "color-correct":
          handleColorCorrection(tokenizer);
          break;
        case "levels-adjust":
          handleLevelsAdjust(tokenizer);
          break;
        case "split":
          handleSplitOperation(tokenizer);
          break;
        case "compress":
          handleCompressOperation(tokenizer);
          break;
        case "downscale":
          handleDownscaleOperation(tokenizer);
          break;
        case "run":
          handleRunScript(tokenizer);
          break;
        default:
          view.renderMessage("Unknown command: " + action);
      }
    } catch (Exception e) {
      view.renderMessage("Error executing command: " + action + " - " + e.getMessage());
    }
  }

  /**
   * Handles loading an image from a specified file path and associates it with a given name.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   * @throws IOException if an error occurs while loading the image.
   */
  private void handleLoadImage(StringTokenizer tokenizer) throws IOException {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide file path and image name for load.");
      return;
    }
    String filePath = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    model.load(filePath, imageName);
    view.renderMessage("Loaded image: " + imageName);
  }

  /**
   * Handles the downscaling operation for an image.
   *
   * <p>
   * This method parses the parameters for downscaling from the provided tokenizer,
   * validates the input, and delegates the downscale operation to the model.
   * After the operation, it renders a success message in the view.
   * </p>
   *
   * @param tokenizer a {@link StringTokenizer} containing the arguments for the operation.
   *                  Expected tokens are:
   *                  <ul>
   *                      <li>new width (integer)</li>
   *                      <li>new height (integer)</li>
   *                      <li>source image name (string)</li>
   *                      <li>destination image name (string)</li>
   *                  </ul>
   *
   * @throws NumberFormatException if the width or height provided is not a valid integer.
   * @throws IllegalArgumentException if any other error occurs during the downscale process.
   */
  private void handleDownscaleOperation(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error: Provide new width, new height, source image name, and "
              + "destination image name.");
      return;
    }
    int newWidth = Integer.parseInt(tokenizer.nextToken());
    int newHeight = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.downscaleImage(newWidth, newHeight, imageName, destImageName);
    view.renderMessage("Downscaled image " + imageName + " to " + newWidth + "x"
            + newHeight + ": " + destImageName);
  }

  /**
   * Handles saving an image to a specified output path with a given name.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   * @throws IOException if an error occurs while saving the image.
   */
  private void handleSaveImage(StringTokenizer tokenizer) throws IOException {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide output path and image name for save.");
      return;
    }
    String outputPath = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    model.save(outputPath, imageName);
    view.renderMessage("Saved image: " + imageName + " to " + outputPath);
  }


  /**
   * Handles the downscaling operation for an image.
   *
   * <p>
   * This method parses the parameters for downscaling from the provided tokenizer,
   * validates the input, and delegates the downscale operation to the model.
   * After the operation, it renders a success message in the view.
   * </p>
   *
   * @param tokenizer a {@link StringTokenizer} containing the arguments for the operation.
   *                  Expected tokens are:
   *                  <ul>
   *                      <li>new width (integer)</li>
   *                      <li>new height (integer)</li>
   *                      <li>source image name (string)</li>
   *                      <li>destination image name (string)</li>
   *                  </ul>
   * @throws NumberFormatException    if the width or height provided is not a valid integer.
   * @throws IllegalArgumentException if any other error occurs during the downscale process.
   */
  private void handleVisualizeChannel(String action, StringTokenizer tokenizer) {
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

  /**
   * Handles the visualization of a specific component of an image.
   *
   * @param action    the action indicating which component to visualize.
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleVisualizeComponent(String action, StringTokenizer tokenizer) {
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

  /**
   * Handles flipping an image in the specified direction.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleFlipImage(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide direction, source image name and destination image name.");
      return;
    }
    String direction = tokenizer.nextToken();
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.flip(direction, imageName, destImageName);
    view.renderMessage("Flipped image " + direction + ": " + destImageName);
  }

  /**
   * Handles brightening an image by a specified amount.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleBrightenImage(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide amount, source image name, and destination image name.");
      return;
    }
    int amount = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.brighten(amount, imageName, destImageName);
    view.renderMessage("Brightened image by " + amount + ": " + destImageName);
  }

  /**
   * Handles basic operations like blur, sharpen, and sepia on an image.
   *
   * @param action    the action indicating which basic operation to perform.
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleBasicOperation(String action, StringTokenizer tokenizer) {
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


  /**
   * Handles converting an image to greyscale using a specified component.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleGreyscaleImage(StringTokenizer tokenizer) {
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

  /**
   * Handles splitting an RGB image into its individual channels.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleRgbSplitImage(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error:Provide source image name and destination names for RGB channels.");
      return;
    }
    String imageName = tokenizer.nextToken();
    String redImageName = tokenizer.nextToken();
    String greenImageName = tokenizer.nextToken();
    String blueImageName = tokenizer.nextToken();
    model.rgbSplit(imageName, redImageName, greenImageName, blueImageName);
    view.renderMessage("RGB split done: " + redImageName + ", " + greenImageName + ", "
            + blueImageName);
  }

  /**
   * Handles combining RGB channels into a single image.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleRgbCombineImage(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 4) {
      view.renderMessage("Error:Provide destination image name and source names for RGB channels.");
      return;
    }
    String destImageName = tokenizer.nextToken();
    String redImageName = tokenizer.nextToken();
    String greenImageName = tokenizer.nextToken();
    String blueImageName = tokenizer.nextToken();
    model.rgbCombine(destImageName, redImageName, greenImageName, blueImageName);
    view.renderMessage("RGB combine done: " + destImageName);
  }


  /**
   * Handles running a script file that contains a series of commands for image processing.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleRunScript(StringTokenizer tokenizer) {
    if (!tokenizer.hasMoreTokens()) {
      view.renderMessage("Error: Provide the script file path for the run command.");
      return;
    }
    String scriptFile = tokenizer.nextToken();
    runScript(scriptFile);
  }

  /**
   * Handles generating a histogram image for a specified source image.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleHistogramImage(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide source image name and destination image name.");
      return;
    }
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.histogram(imageName, destImageName);
    view.renderMessage("Histogram generated: " + destImageName);
  }

  /**
   * Handles color-correcting an image by aligning the meaningful peaks of its histogram.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleColorCorrection(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 2) {
      view.renderMessage("Error: Provide source image name and destination image name.");
      return;
    }
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();
    model.colorCorrect(imageName, destImageName);
    view.renderMessage("Color correction applied to: " + destImageName);
  }

  /**
   * Handles the levels adjustment command by adjusting the black, mid, and white points.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */
  private void handleLevelsAdjust(StringTokenizer tokenizer) {
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


  /**
   * Handles the split operation for various image manipulations.
   *
   * @param tokenizer the StringTokenizer containing the command parameters.
   */

  private void handleSplitOperation(StringTokenizer tokenizer) {
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

  /**
   * Handles the compression operation for an image by reducing its size by a specified percentage.
   *
   * @param tokenizer the {@link StringTokenizer} containing the command parameters.
   *                  Expected tokens are:
   *                  <ul>
   *                      <li>compression percentage (integer)</li>
   *                      <li>source image name (string)</li>
   *                      <li>destination image name (string)</li>
   *                  </ul>
   * @throws NumberFormatException if the percentage is not a valid integer.
   */
  private void handleCompressOperation(StringTokenizer tokenizer) {
    if (tokenizer.countTokens() < 3) {
      view.renderMessage("Error: Provide percentage, source image name, and "
              + "destination image name.");
      return;
    }
    int percentage = Integer.parseInt(tokenizer.nextToken());
    String imageName = tokenizer.nextToken();
    String destImageName = tokenizer.nextToken();


    model.compress(percentage, imageName, destImageName);
    view.renderMessage("Compressed image " + imageName + " by " + percentage + "%: "
            + destImageName);
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
