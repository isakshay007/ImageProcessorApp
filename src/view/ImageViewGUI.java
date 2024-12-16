package view;

import model.Image;
import org.jfree.chart.ChartPanel;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JFileChooser;

import javax.swing.border.TitledBorder;

import java.awt.image.BufferedImage;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.Color;


/**
 * The ImageViewGUI class provides a graphical user interface (GUI) for image processing.
 * It allows users to load, view, and save images, and to apply various image operations.
 */

public class ImageViewGUI extends JFrame {
  private JLabel imageLabel;
  private JPanel histogramPanel;
  private JComboBox<String> operationSelector;
  private JPanel operationInputsPanel;
  private JButton loadButton;
  private JButton executeButton;
  private JButton saveButton;
  private JLabel statusLabel;
  private JButton toggleButton;



  /**
   * Constructs an instance of ImageViewGUI, initializing the GUI components.
   */
  public ImageViewGUI() {
    setTitle("Image Processing Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setLayout(new BorderLayout());

    // Welcome and Status Bar
    JPanel statusPanel = new JPanel(new BorderLayout());
    JLabel welcomeLabel = new JLabel("Welcome to the Image Processing Application! " +
            "Load an image to get started.");
    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    statusPanel.add(welcomeLabel, BorderLayout.NORTH);

    statusLabel = new JLabel("Status: Ready");
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    statusPanel.add(statusLabel, BorderLayout.SOUTH);

    add(statusPanel, BorderLayout.NORTH);

    // Main Image Display
    imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
    imageLabel.setBorder(BorderFactory.createTitledBorder("Image View"));
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    add(imageScrollPane, BorderLayout.CENTER);

    // Histogram Panel
    histogramPanel = new JPanel();
    histogramPanel.setPreferredSize(new Dimension(400, 300));
    histogramPanel.setBorder(new TitledBorder("Histogram"));
    histogramPanel.setLayout(new BorderLayout());
    add(histogramPanel, BorderLayout.EAST);

    // Control Panel
    JPanel controlPanel = new JPanel();
    controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
    controlPanel.setLayout(new BorderLayout());
    add(controlPanel, BorderLayout.SOUTH);

    // Load, Save, and Toggle Buttons
    JPanel buttonPanel = new JPanel(new BorderLayout());
    JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
    Dimension buttonSize = new Dimension(150, 40);

    loadButton = new JButton("Load Image");
    saveButton = new JButton("Save Image");
    toggleButton = new JButton("Toggle View");
    toggleButton.setEnabled(false);

    loadButton.setPreferredSize(buttonSize);
    saveButton.setPreferredSize(buttonSize);
    toggleButton.setPreferredSize(buttonSize);

    topButtonPanel.add(loadButton);
    topButtonPanel.add(saveButton);
    topButtonPanel.add(toggleButton);

    // Operation Selector Panel
    JPanel operationSelectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    operationSelectorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
            10));

    // Create and style the label and combo box
    JLabel operationLabel = new JLabel("Select Operation:");
    operationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    operationSelector = new JComboBox<>(new String[]{"Select Operation", "Flip", "Blur", "Sharpen",
                                                     "Greyscale", "Sepia", "Compression",
                                                     "Adjust Levels", "Color Correction",
                                                     "Split View", "Visualization", "Downscale"});
    operationSelector.setFont(new Font("Arial", Font.PLAIN, 14));

    // Set a preferred size for consistency
    Dimension largerSize = new Dimension(200, 40);
    operationSelector.setPreferredSize(largerSize);

    // Add components to the panel
    operationSelectorPanel.add(operationLabel);
    operationSelectorPanel.add(operationSelector);

    // Add panels to buttonPanel
    buttonPanel.add(topButtonPanel, BorderLayout.WEST);
    buttonPanel.add(operationSelectorPanel, BorderLayout.EAST);

    // Dynamic Operation Inputs Panel
    operationInputsPanel = new JPanel(new CardLayout());
    operationInputsPanel.setBorder(BorderFactory.createTitledBorder("Operation Inputs"));
    operationInputsPanel.setPreferredSize(new Dimension(400, 100));

    
    // Execute Button
    executeButton = new JButton("Execute Operation");
    executeButton.setPreferredSize(new Dimension(500, 40));
    JPanel executeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
    executeButtonPanel.add(executeButton);

    // Combine Panels
    JPanel combinedButtonPanel = new JPanel(new BorderLayout());
    combinedButtonPanel.add(buttonPanel, BorderLayout.NORTH);
    combinedButtonPanel.add(executeButtonPanel, BorderLayout.SOUTH);

    controlPanel.add(combinedButtonPanel, BorderLayout.NORTH);

    // Add save note
    JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
    JLabel saveNoteLabel = new JLabel("Note: Include the file extension (e.g., .png, .jpg) " +
            "when saving.");
    saveNoteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    saveNoteLabel.setForeground(Color.GRAY);
    bottomRightPanel.add(saveNoteLabel);
    controlPanel.add(bottomRightPanel, BorderLayout.SOUTH);
  }

  /**
   * Sets the image to be displayed in the GUI.
   *
   * @param image The image to be displayed.
   */
  public void setImage(BufferedImage image) {
    imageLabel.setIcon(new ImageIcon(image));
    imageLabel.setText("");
  }

  /**
   * Updates the histogram panel with the provided chart panel.
   *
   * @param chartPanel The ChartPanel containing the histogram to be displayed.
   */
  public void updateHistogramPanel(ChartPanel chartPanel) {
    histogramPanel.removeAll();
    histogramPanel.add(chartPanel, BorderLayout.CENTER);
    histogramPanel.revalidate();
    histogramPanel.repaint();
  }

  /**
   * Displays a status message in the status bar.
   *
   * @param message The status message to be displayed.
   */
  public void showStatus(String message) {
    statusLabel.setText("Status: " + message);
  }

  public String getSelectedOperation() {
    return (String) operationSelector.getSelectedItem();
  }

  /**
   * Retrieves the selected operation from the operation selector dropdown.
   *
   * @return The name of the selected operation.
   */
  public int getCompressionPercentage() {
    JSlider compressionSlider = new JSlider(0, 100, 50);
    compressionSlider.setPaintTicks(true);
    compressionSlider.setPaintLabels(true);
    compressionSlider.setMajorTickSpacing(25);
    compressionSlider.setMinorTickSpacing(5);
    JOptionPane.showMessageDialog(this, compressionSlider,
            "Set Compression Percentage", JOptionPane.PLAIN_MESSAGE);
    return compressionSlider.getValue();
  }

  /**
   * Prompts the user to select a compression percentage using a slider.
   * Displays a dialog with a slider that allows the user to select a value
   * between 0 and 100 (with a default value of 50).
   *
   * @return The selected compression percentage value.
   */
  public String getSplitOperationName() {
    String[] operations = {"Blur", "Sharpen", "Greyscale", "Sepia", "Color Correction",
                           "Adjust Levels"};
    return (String) JOptionPane.showInputDialog(this, "Select the " +
            "operation to apply for Split View:", "Split View Operation",
            JOptionPane.PLAIN_MESSAGE, null, operations, operations[0]);
  }

  /**
   * Prompts the user to select an operation to apply for the Split View operation.
   * A dialog will appear with a list of possible operations.
   *
   * @return The name of the selected operation for Split View.
   */
  public int getSplitPercentage() {
    JSlider splitSlider = new JSlider(0, 100, 50);
    splitSlider.setPaintTicks(true);
    splitSlider.setPaintLabels(true);
    splitSlider.setMajorTickSpacing(25);
    splitSlider.setMinorTickSpacing(5);

    JPanel sliderPanel = new JPanel(new BorderLayout());
    sliderPanel.add(new JLabel("Set Split Percentage:"), BorderLayout.NORTH);
    sliderPanel.add(splitSlider, BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(
            this,
            sliderPanel,
            "Split Percentage",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
      return splitSlider.getValue();
    } else {
      return -1;
    }
  }

  /**
   * Prompts the user to select a flip direction (horizontal or vertical).
   * A dialog will appear with two options for flip direction.
   *
   * @return The selected flip direction (either "Horizontal" or "Vertical").
   */
  public String getFlipDirection() {
    String[] directions = {"Horizontal", "Vertical"};
    return (String) JOptionPane.showInputDialog(
            this,
            "Select Flip Direction:",
            "Flip Direction",
            JOptionPane.PLAIN_MESSAGE,
            null,
            directions,
            directions[0]
    );
  }


  /**
   * Prompts the user to select a visualization type.
   * A dialog will appear with options for visualizing different image channels or properties.
   *
   * @return The selected visualization type (e.g., "Red Channel", "Green Channel", etc.).
   */
  public String getVisualizationType() {
    String[] visualizations = { "Red Channel", "Green Channel", "Blue Channel",
                                "Luma", "Value", "Intensity" };
    return (String) JOptionPane.showInputDialog(this,
            "Select Visualization Type:", "Visualization", JOptionPane.PLAIN_MESSAGE,
            null, visualizations, visualizations[0]);
  }


  /**
   * Prompts the user to adjust levels for an image (black, mid, and white points).
   * A dialog will appear where the user can input values for each point.
   *
   * @return An array containing the black point, mid point, and white point values, or null
   *        if the user cancels.
   */
  public int[] getLevelAdjustments() {
    JTextField blackPointField = new JTextField("0", 5);
    JTextField midPointField = new JTextField("128", 5);
    JTextField whitePointField = new JTextField("255", 5);

    JPanel panel = new JPanel();
    panel.add(new JLabel("Black Point:"));
    panel.add(blackPointField);
    panel.add(new JLabel("Mid Point:"));
    panel.add(midPointField);
    panel.add(new JLabel("White Point:"));
    panel.add(whitePointField);

    int result = JOptionPane.showConfirmDialog(this, panel,
            "Adjust Levels for Split", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      try {
        return new int[]{
                Integer.parseInt(blackPointField.getText()),
                Integer.parseInt(midPointField.getText()),
                Integer.parseInt(whitePointField.getText())
        };
      } catch (NumberFormatException e) {
        showMessage("Invalid input for levels adjustment!", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }


  /**
   * Opens a file chooser dialog to allow the user to select a file and returns the selected
   *         file's absolute path.
   *
   * @return the absolute path of the selected file if a file is chosen, or {@code null} if the
   *          operation is canceled.
   */
  public String promptForFilePath() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Load Image");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getAbsolutePath();
    }
    return null;
  }


  /**
   * Prompts the user to select a location to save an image.
   * A file chooser dialog will appear to allow the user to select where to save the image.
   *
   * @return The absolute file path where the image will be saved, or null if no location was
   *        selected.
   */
  public String promptForSaveFilePath() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Image");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getAbsolutePath();
    }
    return null;
  }

  /**
   * Prompts the user to input a custom image name.
   * A text input dialog will appear asking for the name of the image.
   *
   * @param message The message to display in the input dialog.
   * @return The image name entered by the user, or null if the user cancels.
   */
  public String promptForImageName(String message) {
    return JOptionPane.showInputDialog(this, message, "Input",
            JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * Displays a message to the user in a dialog box.
   *
   * @param message The message to display.
   * @param messageType The type of message (e.g., ERROR_MESSAGE, INFORMATION_MESSAGE).
   */
  public void showMessage(String message, int messageType) {
    JOptionPane.showMessageDialog(this, message, "Message", messageType);
  }

  /**
   * Adds a listener to the load button. This listener will be triggered when the load button
   *        is clicked.
   * The toggle button is disabled when a new image is loaded.
   *
   * @param callback The action to perform when the load button is clicked.
   */
  public void addLoadListener(Runnable callback) {
    loadButton.addActionListener(e -> {
      enableToggleButton(false); // Deactivate toggle button on new image load
      callback.run();
    });
  }

  /**
   * Adds a listener to the operation selection dropdown. This listener is triggered when the user
   *        selects an operation.
   * The toggle button is disabled for operations other than "Split View".
   *
   * @param callback The action to perform when the operation selection changes.
   */
  public void addOperationSelectionListener(Runnable callback) {
    operationSelector.addActionListener(e -> {
      String operation = getSelectedOperation();
      if (!"Split View".equals(operation)) {
        enableToggleButton(false); // Deactivate for other operations
      }
      callback.run();
    });
  }

  /**
   * Adds a listener to the execute button. This listener is triggered when the execute button is
   *        clicked.
   *
   * @param callback The action to perform when the execute button is clicked.
   */
  public void addExecuteListener(Runnable callback) {
    executeButton.addActionListener(e -> callback.run());
  }

  /**
   * Adds a listener to the save button. This listener is triggered when the save button is clicked.
   * The toggle button is disabled when the save action is triggered.
   *
   * @param callback The action to perform when the save button is clicked.
   */
  public void addSaveListener(Runnable callback) {
    saveButton.addActionListener(e -> {
      enableToggleButton(false); // Deactivate toggle button on save
      callback.run();
    });
  }


  /**
   * Enables or disables the toggle button.
   *
   * @param enabled Whether the toggle button should be enabled (true) or disabled (false).
   */
  public void enableToggleButton(boolean enabled) {
    toggleButton.setEnabled(enabled);
  }

  /**
   * Adds a listener to the toggle button. This listener is triggered when the toggle button is
   *        clicked.
   *
   * @param callback The action to perform when the toggle button is clicked.
   */
  public void addToggleListener(Runnable callback) {
    toggleButton.addActionListener(e -> callback.run());
  }

  /**
   * Displays the input fields for the selected operation.
   * Depending on the selected operation, different input panels will be shown.
   * The toggle button is enabled or disabled based on whether the operation is "Split View".
   *
   * @param operation The selected operation for which inputs are to be displayed.
   */
  public void showInputsForOperation(String operation) {
    CardLayout layout = (CardLayout) operationInputsPanel.getLayout();
    if ("Split View".equals(operation)) {
      enableToggleButton(true); // Activate toggle button for Split View
    } else {
      enableToggleButton(false); // Deactivate for other operations
    }
    switch (operation) {
      case "Flip":
      case "Blur":
      case "Sharpen":
      case "Greyscale":
      case "Sepia":
      case "Compression":
      case "Adjust Levels":
      case "Color Correction":
      case "Split View":
      case "Visualization":
        layout.show(operationInputsPanel, operation);
        break;
      default:
        layout.show(operationInputsPanel, "Default");
        break;
    }
  }

  /**
   * Prompts the user to input a dimension (e.g., width or height) as an integer.
   * If the user provides invalid input, an error message is shown.
   *
   * @param message The message to display in the input dialog to guide the user.
   * @return The dimension entered by the user as an integer, or -1 if the input is invalid.
   */
  public int promptForDimension(String message) {
    String input = JOptionPane.showInputDialog(this, message, "Input",
            JOptionPane.PLAIN_MESSAGE);
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      showMessage("Invalid input! Please enter a positive integer.",
              JOptionPane.ERROR_MESSAGE);
      return -1;
    }
  }

  /**
   * Converts an Image object into a BufferedImage.
   * This method iterates over the pixel data of Image object and copies it into a BufferedImage.
   *
   * @param image The Image object to convert.
   * @return A BufferedImage representation of the provided Image object.
   */
  public BufferedImage convertToBufferedImage(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int red = image.getRedChannel()[y][x];
        int green = image.getGreenChannel()[y][x];
        int blue = image.getBlueChannel()[y][x];
        int rgb = (red << 16) | (green << 8) | blue;
        bufferedImage.setRGB(x, y, rgb);
      }
    }
    return bufferedImage;
  }


  /**
   * Converts a BufferedImage into an Image object.
   * This method extracts the RGB values from the BufferedImage and stores them in separate red,
   * green, and blue channel arrays.
   *
   * @param bufferedImage The BufferedImage to convert.
   * @return An Image object created from the BufferedImage.
   */
  public Image convertFromBufferedImage(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = bufferedImage.getRGB(x, y);
        red[y][x] = (rgb >> 16) & 0xFF;
        green[y][x] = (rgb >> 8) & 0xFF;
        blue[y][x] = rgb & 0xFF;
      }
    }

    return new Image(width, height, red, green, blue);
  }

  /**
   * Displays a welcome message to the user when the application is started.
   * The message prompts the user to load an image to begin using the application.
   */
  public void showWelcomeMessage() {
    JOptionPane.showMessageDialog(
            this,
            "Welcome to the Image Processing Application!\n" +
                    "Please load an image to get started.",
            "Welcome",
            JOptionPane.INFORMATION_MESSAGE
    );
  }
}