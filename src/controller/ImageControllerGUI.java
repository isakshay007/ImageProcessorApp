package controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import model.Image;
import model.ImageModel;
import view.ImageViewGUI;

/**
 * The ImageControllerGUI class manages the interaction between the ImageModel and ImageViewGUI.
 * It handles user actions, invokes model operations, and updates the view accordingly.
 */
public class ImageControllerGUI {
  private final ImageModel model;
  private final ImageViewGUI view;
  private String currentImageName;
  private String originalImageName;
  private String splitImageName;
  private boolean isSplitViewActive = false;

  /**
   * Constructs an ImageControllerGUI with the given model and view.
   *
   * @param model the image processing model
   * @param view  the GUI view for image operations
   */
  public ImageControllerGUI(ImageModel model, ImageViewGUI view) {
    this.model = model;
    this.view = view;
    initializeView();
  }

  /**
   * Initializes the view by setting up listeners and displaying a welcome message.
   */
  private void initializeView() {
    view.showWelcomeMessage();
    view.addLoadListener(this::handleLoad);
    view.addOperationSelectionListener(this::updateInputsForOperation);
    view.addExecuteListener(this::handleOperationExecution);
    view.addSaveListener(this::handleSave);
    view.addToggleListener(this::handleToggleView);
  }

  /**
   * Handles the loading of an image file into the model and updates the view with the loaded image.
   */
  private void handleLoad() {
    String filePath = view.promptForFilePath();
    if (filePath == null || filePath.isEmpty()) {
      view.showStatus("Image loading canceled.");
      return;
    }

    String imageName = view.promptForImageName("Enter a name for the loaded image:");
    if (imageName == null || imageName.isEmpty()) {
      view.showStatus("Image name is required!");
      return;
    }

    try {
      model.load(filePath, imageName);
      currentImageName = imageName;
      originalImageName = imageName;
      BufferedImage image = view.convertToBufferedImage(model.getImage(imageName));
      view.setImage(image);
      updateHistogram(imageName);
      view.showStatus("Image loaded successfully! Start applying operations.");
    } catch (IOException e) {
      view.showMessage("Failed to load image: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Handles saving the current image to a file.
   */
  private void handleSave() {
    if (currentImageName == null) {
      view.showMessage("No image loaded. Please load an image first.",
              JOptionPane.ERROR_MESSAGE);
      return;
    }

    String filePath = view.promptForSaveFilePath();
    if (filePath == null || filePath.isEmpty()) {
      view.showStatus("Image saving canceled.");
      return;
    }

    try {
      if (model.isProcessedImage(currentImageName)) {
        BufferedImage processedImage = model.getProcessedImage(currentImageName);
        model.saveProcessedImage(filePath, processedImage);
      } else {
        model.save(filePath, currentImageName);
      }
      view.showMessage("Image saved successfully!", JOptionPane.INFORMATION_MESSAGE);
      view.showStatus("Image saved successfully at: " + filePath);
    } catch (IOException e) {
      view.showMessage("Failed to save image: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Updates the view to display inputs required for the selected operation.
   */
  private void updateInputsForOperation() {
    String selectedOperation = view.getSelectedOperation();
    view.showInputsForOperation(selectedOperation);
    view.showStatus("Selected operation: " + selectedOperation);
  }

  /**
   * Handles the execution of image operations selected by the user.
   */
  private void handleOperationExecution() {
    if (currentImageName == null) {
      view.showMessage("No image loaded. Please load an image first.",
              JOptionPane.ERROR_MESSAGE);
      return;
    }

    String destImageName = view.promptForImageName("Enter a name for the resulting image:");
    if (destImageName == null || destImageName.isEmpty()) {
      view.showStatus("Destination image name is required!");
      return;
    }

    try {
      String selectedOperation = view.getSelectedOperation();
      switch (selectedOperation) {
        case "Flip":
          handleFlip(destImageName);
          break;
        case "Blur":
          model.blur(currentImageName, destImageName);
          break;
        case "Sharpen":
          model.sharpen(currentImageName, destImageName);
          break;
        case "Greyscale":
          model.convertToGreyscale("luma", currentImageName, destImageName);
          break;
        case "Sepia":
          model.convertToSepia(currentImageName, destImageName);
          break;
        case "Compression":
          int compressionPercent = view.getCompressionPercentage();
          model.compress(compressionPercent, currentImageName, destImageName);
          break;
        case "Adjust Levels":
          int[] levels = view.getLevelAdjustments();
          model.levelsAdjust(levels[0], levels[1], levels[2], currentImageName, destImageName);
          break;
        case "Color Correction":
          model.colorCorrect(currentImageName, destImageName);
          break;
        case "Split View":
          handleSplitView(destImageName);
          break;
        case "Visualization":
          handleVisualization(destImageName);
          break;
        case "Downscale":
          int newWidth = view.promptForDimension("Enter the new width:");
          if (newWidth <= 0) {
            view.showMessage("Invalid width entered. Operation aborted.",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }

          int newHeight = view.promptForDimension("Enter the new height:");
          if (newHeight <= 0) {
            view.showMessage("Invalid height entered. Operation aborted.",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }

          Image sourceImage = model.getImage(currentImageName);
          int originalWidth = sourceImage.getWidth();
          int originalHeight = sourceImage.getHeight();


          if (newWidth > originalWidth || newHeight > originalHeight) {
            view.showMessage("Invalid dimensions! New width and height must not " +
                    "exceed the original size.", JOptionPane.ERROR_MESSAGE);
            return;
          }


          model.downscaleImage(newWidth, newHeight, currentImageName, destImageName);
          break;

        default:
          view.showStatus("Unknown operation selected.");
          return;
      }

      updateViewWithResult(destImageName);
      view.showStatus(selectedOperation + " completed successfully!");
    } catch (IllegalArgumentException e) {
      view.showMessage("Operation failed: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Handles the Split View operation, applying the selected operation partially based on the
   * given percentage.
   *
   * @param destImageName the name for the resulting image from the Split View operation
   */
  private void handleSplitView(String destImageName) {
    String splitOperation = view.getSplitOperationName();
    if (splitOperation == null || splitOperation.isEmpty()) {
      view.showStatus("Split View operation canceled.");
      return;
    }

    int splitPercentage = view.getSplitPercentage();
    if (splitPercentage < 0 || splitPercentage > 100) {
      view.showMessage("Invalid split percentage! It must be between 0 and 100.",
              JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {

      switch (splitOperation.toLowerCase()) {
        case "blur":
          model.splitOperation("blur", currentImageName, destImageName,
                  splitPercentage, null);
          break;
        case "sharpen":
          model.splitOperation("sharpen", currentImageName, destImageName,
                  splitPercentage, null);
          break;
        case "greyscale":
          model.splitOperation("greyscale", currentImageName, destImageName,
                  splitPercentage, null);
          break;
        case "sepia":
          model.splitOperation("sepia", currentImageName, destImageName,
                  splitPercentage, null);
          break;
        case "color correction":
          model.splitOperation("colorcorrect", currentImageName, destImageName,
                  splitPercentage, null);
          break;
        case "adjust levels":
          int[] levels = view.getLevelAdjustments();
          if (levels == null || levels.length != 3) {
            view.showStatus("Invalid levels input for Adjust Levels in Split View.");
            return;
          }

          Map<String, Object> additionalParams = new HashMap<>();
          additionalParams.put("black", levels[0]);
          additionalParams.put("mid", levels[1]);
          additionalParams.put("white", levels[2]);

          model.splitOperation("levels", currentImageName, destImageName,
                  splitPercentage, additionalParams);
          break;
        default:
          view.showStatus("Unknown split operation: " + splitOperation);
          return;
      }


      splitImageName = destImageName;


      updateViewWithResult(destImageName);

      isSplitViewActive = true;
      view.enableToggleButton(true);
      view.showStatus("Split View operation completed successfully!");
    } catch (IllegalArgumentException e) {
      view.showMessage("Split View operation failed: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Toggles between the original image and the split view image.
   */
  private void handleToggleView() {
    if (originalImageName == null || splitImageName == null) {
      view.showMessage("No image to toggle. Please load an image and perform a split " +
              "operation first.", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      BufferedImage imageToShow;

      isSplitViewActive = !isSplitViewActive;

      String imageNameToShow = isSplitViewActive ? splitImageName : originalImageName;

      if (model.isProcessedImage(imageNameToShow)) {
        imageToShow = model.getProcessedImage(imageNameToShow);
      } else {
        Image image = model.getImage(imageNameToShow);
        if (image == null) {
          throw new IllegalArgumentException("Image not found: " + imageNameToShow);
        }
        imageToShow = view.convertToBufferedImage(image);
      }

      view.setImage(imageToShow);

      view.showStatus(isSplitViewActive ? "Switched to Split View." : "Switched to Original View.");
    } catch (Exception e) {
      view.showMessage("Failed to toggle view: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   * Updates the view with the result image and its corresponding histogram.
   *
   * @param destImageName the name of the destination image to display and update.
   */
  private void updateViewWithResult(String destImageName) {
    try {
      BufferedImage resultImage;

      if (model.isProcessedImage(destImageName)) {
        resultImage = model.getProcessedImage(destImageName);
      } else {
        Image image = model.getImage(destImageName);
        resultImage = view.convertToBufferedImage(image);
      }

      view.setImage(resultImage);
      updateHistogram(destImageName);
      currentImageName = destImageName;
    } catch (Exception e) {
      view.showMessage("Failed to update view: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   * Handles the flip operation on the current image.
   *
   * @param destImageName the name of the destination image after the flip operation.
   */
  private void handleFlip(String destImageName) {
    String direction = view.getFlipDirection();
    if (direction == null || direction.isEmpty()) {
      view.showStatus("Flip direction is required!");
      return;
    }
    model.flip(direction, currentImageName, destImageName);
  }


  /**
   * Handles the visualization of specific components or channels of the image.
   *
   * @param destImageName the name of the destination image after visualization.
   */
  private void handleVisualization(String destImageName) {
    String visualizationType = view.getVisualizationType();
    if (visualizationType == null || visualizationType.isEmpty()) {
      view.showStatus("Visualization type selection canceled.");
      return;
    }

    try {
      switch (visualizationType.toLowerCase()) {
        case "red channel":
          model.visualizeChannel("red", currentImageName, destImageName);
          break;
        case "green channel":
          model.visualizeChannel("green", currentImageName, destImageName);
          break;
        case "blue channel":
          model.visualizeChannel("blue", currentImageName, destImageName);
          break;
        case "luma":
          model.visualizeComponent("luma", currentImageName, destImageName);
          break;
        case "value":
          model.visualizeComponent("value", currentImageName, destImageName);
          break;
        case "intensity":
          model.visualizeComponent("intensity", currentImageName, destImageName);
          break;
        default:
          view.showStatus("Unknown visualization type: " + visualizationType);
          return;
      }

      updateViewWithResult(destImageName);
      view.showStatus("Visualization (" + visualizationType + ") completed successfully!");
    } catch (IllegalArgumentException e) {
      view.showMessage("Visualization failed: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Updates the histogram for the specified image.
   *
   * @param imageName the name of the image for which the histogram is to be updated.
   */
  private void updateHistogram(String imageName) {
    try {

      Image image;
      if (model.isProcessedImage(imageName)) {
        BufferedImage processedImage = model.getProcessedImage(imageName);
        image = view.convertFromBufferedImage(processedImage);
      } else {
        image = model.getImage(imageName);
      }

      int[][] redChannel = image.getRedChannel();
      int[][] greenChannel = image.getGreenChannel();
      int[][] blueChannel = image.getBlueChannel();

      XYSeries redSeries = new XYSeries("Red");
      XYSeries blueSeries = new XYSeries("Blue");
      XYSeries greenSeries = new XYSeries("Green");

      int[] redHist = calculateHistogram(redChannel);
      int[] greenHist = calculateHistogram(greenChannel);
      int[] blueHist = calculateHistogram(blueChannel);

      for (int i = 0; i < 256; i++) {
        redSeries.add(i, redHist[i]);
        blueSeries.add(i, blueHist[i]);
        greenSeries.add(i, greenHist[i]);
      }

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(redSeries);
      dataset.addSeries(blueSeries);
      dataset.addSeries(greenSeries);

      JFreeChart chart = ChartFactory.createXYLineChart("Histogram", "Intensity",
              "Frequency", dataset);


      chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
      chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
      chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);

      ChartPanel chartPanel = new ChartPanel(chart);
      view.updateHistogramPanel(chartPanel);
    } catch (Exception e) {
      view.showMessage("Failed to update histogram: " + e.getMessage(),
              JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   * Calculates the histogram of pixel intensity values for a given channel.
   *
   * @param channel a 2D array representing the pixel intensities of a color channel.
   * @return an array where each index represents the frequency of a specific intensity value.
   */
  private int[] calculateHistogram(int[][] channel) {
    int[] histogram = new int[256];
    for (int[] row : channel) {
      for (int value : row) {
        histogram[value]++;
      }
    }
    return histogram;
  }
}