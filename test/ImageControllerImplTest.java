import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import controller.ImageControllerImpl;
import model.ImageModel;
import model.ImageModelImpl;
import view.ImageViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ImageControllerImpl} class.
 */
public class ImageControllerImplTest {

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  private ImageControllerImpl controller;

  /**
   * Sets up the test environment by redirecting standard output to capture console output
   * for validation.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));  // Capture console output

    // Initialize model and view locally
    ImageModel model = new ImageModelImpl();  // Initialize model locally
    ImageViewImpl view = new ImageViewImpl();  // Initialize view locally
    controller = new ImageControllerImpl(model, view);  // Initialize controller
  }

  /**
   * Tests loading and saving PPM, PNG, and JPG formats in one test.
   */
  @Test
  public void testLoadAndSaveForAllFormats() {
    // Test PPM
    String loadPPMCommand = "load resources/test_image/download.ppm download-ppm";
    controller.processCommand(loadPPMCommand);
    String savePPMCommand = "save resources/save_image/download-save.ppm download-ppm";
    controller.processCommand(savePPMCommand);

    // Test PNG
    String loadPNGCommand = "load resources/test_image/download.png download-png";
    controller.processCommand(loadPNGCommand);
    String savePNGCommand = "save resources/save_image/download-save.png download-png";
    controller.processCommand(savePNGCommand);

    // Test JPG
    String loadJPGCommand = "load resources/test_image/download.jpg download-jpg";
    controller.processCommand(loadJPGCommand);
    String saveJPGCommand = "save resources/save_image/download-save.jpg download-jpg";
    controller.processCommand(saveJPGCommand);

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Saved image: download-ppm to resources/save_image/download-save.ppm\n"
            + "Loaded image: download-png\n"
            + "Saved image: download-png to resources/save_image/download-save.png\n"
            + "Loaded image: download-jpg\n"
            + "Saved image: download-jpg to resources/save_image/download-save.jpg";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'brighten' operation across all formats.
   */
  @Test
  public void testBrightenImageCommand() {
    // Brighten operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("brighten 25 download-ppm download-ppm-brightened");

    // Brighten operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("brighten 25 download-png download-png-brightened");

    // Brighten operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("brighten 25 download-jpg download-jpg-brightened");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Brightened image by 25: download-ppm-brightened\n"
            + "Loaded image: download-png\n" + "Brightened image by 25: download-png-brightened\n"
            + "Loaded image: download-jpg\n" + "Brightened image by 25: download-jpg-brightened";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'flip horizontal' operation across all formats.
   */
  @Test
  public void testFlipImageHorizontalCommand() {
    // Flip horizontal on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("flip horizontal download-ppm download-ppm-flipped-horizontal");

    // Flip horizontal on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("flip horizontal download-png download-png-flipped-horizontal");

    // Flip horizontal on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("flip horizontal download-jpg download-jpg-flipped-horizontal");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Flipped image horizontal: download-ppm-flipped-horizontal\n"
            + "Loaded image: download-png\n"
            + "Flipped image horizontal: download-png-flipped-horizontal\n"
            + "Loaded image: download-jpg\n"
            + "Flipped image horizontal: download-jpg-flipped-horizontal";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'flip vertical' operation across all formats.
   */
  @Test
  public void testFlipImageVerticalCommand() {
    // Flip vertical on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("flip vertical download-ppm download-ppm-flipped-vertical");

    // Flip vertical on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("flip vertical download-png download-png-flipped-vertical");

    // Flip vertical on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("flip vertical download-jpg download-jpg-flipped-vertical");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Flipped image vertical: download-ppm-flipped-vertical\n"
            + "Loaded image: download-png\n"
            + "Flipped image vertical: download-png-flipped-vertical\n"
            + "Loaded image: download-jpg\n"
            + "Flipped image vertical: download-jpg-flipped-vertical";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'red-component' operation across all formats.
   */
  @Test
  public void testRedChannelCommand() {
    // Red component operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("red-component download-ppm download-ppm-red-component");

    // Red component operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("red-component download-png download-png-red-component");

    // Red component operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("red-component download-jpg download-jpg-red-component");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Visualized red channel: download-ppm-red-component\n"
            + "Loaded image: download-png\n"
            + "Visualized red channel: download-png-red-component\n"
            + "Loaded image: download-jpg\n"
            + "Visualized red channel: download-jpg-red-component";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'blue-component' operation across all formats.
   */
  @Test
  public void testBlueChannelCommand() {
    // Blue component operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("blue-component download-ppm download-ppm-blue-component");

    // Blue component operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("blue-component download-png download-png-blue-component");

    // Blue component operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("blue-component download-jpg download-jpg-blue-component");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Visualized blue channel: download-ppm-blue-component\n"
            + "Loaded image: download-png\n"
            + "Visualized blue channel: download-png-blue-component\n"
            + "Loaded image: download-jpg\n"
            + "Visualized blue channel: download-jpg-blue-component";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'green-component' operation across all formats.
   */

  @Test
  public void testGreenChannelCommand() {
    // Green component operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("green-component download-ppm download-ppm-green-component");

    // Green component operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("green-component download-png download-png-green-component");

    // Green component operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("green-component download-jpg download-jpg-green-component");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Visualized green channel: download-ppm-green-component\n"
            + "Loaded image: download-png\n"
            + "Visualized green channel: download-png-green-component\n"
            + "Loaded image: download-jpg\n"
            + "Visualized green channel: download-jpg-green-component";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'luma-component' operation across all formats.
   */
  @Test
  public void testLumaComponentCommand() {
    // Luma component operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("luma-component download-ppm download-ppm-luma-component");

    // Luma component operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("luma-component download-png download-png-luma-component");

    // Luma component operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("luma-component download-jpg download-jpg-luma-component");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Visualized luma component: download-ppm-luma-component\n"
            + "Loaded image: download-png\n"
            + "Visualized luma component: download-png-luma-component\n"
            + "Loaded image: download-jpg\n"
            + "Visualized luma component: download-jpg-luma-component";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'intensity-component' operation across all formats.
   */
  @Test
  public void testIntensityComponentCommand() {
    // Intensity component operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("intensity-component download-ppm download-ppm-intensity-component");

    // Intensity component operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("intensity-component download-png download-png-intensity-component");

    // Intensity component operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("intensity-component download-jpg download-jpg-intensity-component");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "Visualized intensity component: download-ppm-intensity-component\n"
            + "Loaded image: download-png\n"
            + "Visualized intensity component: download-png-intensity-component\n"
            + "Loaded image: download-jpg\n"
            + "Visualized intensity component: download-jpg-intensity-component";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }


  @Test
  public void testRgbSplitCommand() {
    // Test RGB Split for PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("rgb-split download-ppm download-ppm-red "
            + "download-ppm-green download-ppm-blue");

    // Test RGB Split for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("rgb-split download-png download-png-red "
            + "download-png-green download-png-blue");

    // Test RGB Split for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("rgb-split download-jpg download-jpg-red "
            + "download-jpg-green download-jpg-blue");

    String expectedOutput = "Loaded image: download-ppm\n" + "RGB split done: download-ppm-red, "
            + "download-ppm-green, download-ppm-blue\n" + "Loaded image: download-png\n"
            + "RGB split done: download-png-red, download-png-green, download-png-blue\n"
            + "Loaded image: download-jpg\n"
            + "RGB split done: download-jpg-red, download-jpg-green, download-jpg-blue";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'rgb-combine' operation across all formats.
   */
  @Test
  public void testRgbSplitAndCombineCommand() {
    // Test RGB Split and Combine for PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("rgb-split download-ppm download-ppm-red download-ppm-green "
            + "download-ppm-blue");
    controller.processCommand("rgb-combine download-ppm-combined download-ppm-red "
            + "download-ppm-green download-ppm-blue");

    // Test RGB Split and Combine for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("rgb-split download-png download-png-red download-png-green "
            + "download-png-blue");
    controller.processCommand("rgb-combine download-png-combined download-png-red "
            + "download-png-green download-png-blue");

    // Test RGB Split and Combine for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("rgb-split download-jpg download-jpg-red download-jpg-green "
            + "download-jpg-blue");
    controller.processCommand("rgb-combine download-jpg-combined download-jpg-red "
            + "download-jpg-green download-jpg-blue");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "RGB split done: download-ppm-red, download-ppm-green, download-ppm-blue\n"
            + "RGB combine done: download-ppm-combined\n" + "Loaded image: download-png\n"
            + "RGB split done: download-png-red, download-png-green, download-png-blue\n"
            + "RGB combine done: download-png-combined\n" + "Loaded image: download-jpg\n"
            + "RGB split done: download-jpg-red, download-jpg-green, download-jpg-blue\n"
            + "RGB combine done: download-jpg-combined";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'compress' operation for PNG and JPG formats with a 50% compression.
   */
  @Test
  public void testCompressCommandAtDifferentLevels() {
    // Compression at 10% for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("compress 10 download-png download-png-compressed-10");

    // Compression at 10% for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("compress 10 download-jpg download-jpg-compressed-10");

    // Compression at 50% for PNG
    controller.processCommand("compress 50 download-png download-png-compressed-50");

    // Compression at 50% for JPG
    controller.processCommand("compress 50 download-jpg download-jpg-compressed-50");

    // Compression at 95% for PNG
    controller.processCommand("compress 95 download-png download-png-compressed-95");

    // Compression at 95% for JPG
    controller.processCommand("compress 95 download-jpg download-jpg-compressed-95");

    String expectedOutput = "Loaded image: download-png\n"
            + "Compressed image download-png by 10%: download-png-compressed-10\n"
            + "Loaded image: download-jpg\n"
            + "Compressed image download-jpg by 10%: download-jpg-compressed-10\n"
            + "Compressed image download-png by 50%: download-png-compressed-50\n"
            + "Compressed image download-jpg by 50%: download-jpg-compressed-50\n"
            + "Compressed image download-png by 95%: download-png-compressed-95\n"
            + "Compressed image download-jpg by 95%: download-jpg-compressed-95";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }


  /**
   * Tests the 'levels-adjust' operation for PNG and JPG formats.
   */
  @Test
  public void testLevelsAdjustCommand() {
    // Levels adjustment for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("levels-adjust 0 128 255 download-png download-png-levels-adjusted");

    // Levels adjustment for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("levels-adjust 0 128 255 download-jpg download-jpg-levels-adjusted");

    String expectedOutput = "Loaded image: download-png\n"
            + "Levels adjustment applied to: download-png-levels-adjusted\n"
            + "Loaded image: download-jpg\n"
            + "Levels adjustment applied to: download-jpg-levels-adjusted";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'color-correct' operation for PNG and JPG formats.
   */
  @Test
  public void testColorCorrectCommand() {
    // Color correction for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("color-correct download-png download-png-color-corrected");

    // Color correction for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("color-correct download-jpg download-jpg-color-corrected");

    String expectedOutput = "Loaded image: download-png\n"
            + "Color correction applied to: download-png-color-corrected\n"
            + "Loaded image: download-jpg\n"
            + "Color correction applied to: download-jpg-color-corrected";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'histogram' operation for PNG and JPG formats.
   */
  @Test
  public void testHistogramCommand() {
    // Histogram for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("histogram download-png download-png-histogram");

    // Histogram for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("histogram download-jpg download-jpg-histogram");

    String expectedOutput = "Loaded image: download-png\n"
            + "Histogram generated: download-png-histogram\n"
            + "Loaded image: download-jpg\n"
            + "Histogram generated: download-jpg-histogram";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'split' operation for all supported manipulations (blur, sharpen, sepia, greyscale,
   * color correction, and levels adjustment) for PNG and JPG formats with a 50% split.
   */
  @Test
  public void testSplitOperations() {
    String[] operations = {"blur", "sharpen", "sepia", "greyscale", "colorcorrect", "levels"};
    String[] operationNames = {"blur", "sharpen", "sepia", "greyscale", "colorcorrect", "levels"};
    String[] extraParams = {"", "", "", "", "", " 0 128 255"};

    for (int i = 0; i < operations.length; i++) {
      String operation = operations[i];
      String operationName = operationNames[i];
      String additionalParams = extraParams[i];

      // Split operation for PNG
      controller.processCommand("load resources/test_image/download.png download-png");
      controller.processCommand("split " + operation + " download-png download-png-"
              + operation + "-split 50" + additionalParams);

      // Split operation for JPG
      controller.processCommand("load resources/test_image/download.jpg download-jpg");
      controller.processCommand("split " + operation + " download-jpg download-jpg-"
              + operation + "-split 50" + additionalParams);

      String expectedOutput = "Loaded image: download-png\n"
              + operationName + " with split applied to: download-png-" + operation + "-split\n"
              + "Loaded image: download-jpg\n"
              + operationName + " with split applied to: download-jpg-" + operation + "-split";

      assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));

      // Clear output stream for next operation
      outputStreamCaptor.reset();
    }
  }

  /**
   * Helper method to execute the same test command for PPM, PNG, and JPG formats.
   *
   * @param command         The image operation command (e.g., "flip vertical").
   * @param expectedMessage The expected message to appear in the output.
   */
  private void performTestForAllFormats(String command, String expectedMessage) {
    // Run command for PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand(command + " download-ppm download-ppm-output");

    // Run command for PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand(command + " download-png download-png-output");

    // Run command for JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand(command + " download-jpg download-jpg-output");

    String expectedOutput = "Loaded image: download-ppm\n"
            + expectedMessage + ": download-ppm-output\n" + "Loaded image: download-png\n"
            + expectedMessage + ": download-png-output\n" + "Loaded image: download-jpg\n"
            + expectedMessage + ": download-jpg-output";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Utility method to normalize line endings for platform-independent comparison.
   */
  private String normalizeOutput(String output) {
    return output.replaceAll("\\r\\n?", "\n").trim();
  }

  /**
   * Tests if the controller correctly parses and executes commands from an existing 'script.txt'
   * file using the 'run' command.
   */
  @Test
  public void testScriptParsingWithRunCommandAndFilePath() {

    Path scriptFilePath = Path.of("resources/test1.txt");

    controller.processCommand("run " + scriptFilePath.toAbsolutePath());

    String expectedOutput = "Executing command from script: load "
            + "resources/test_image/download.png download-png\n"
            + "Loaded image: download-png\n"
            + "Executing command from script: brighten 20 download-png download-png-brightened\n"
            + "Brightened image by 20: download-png-brightened\n"
            + "Executing command from script: save "
            + "resources/save_image/download-png-brightened.png download-png-brightened\n"
            + "Saved image: download-png-brightened to "
            + "resources/save_image/download-png-brightened.png\n"
            + "Script executed successfully.";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'blur' operation across all formats.
   */
  @Test
  public void testBlurCommand() {
    // Blur operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("blur download-ppm download-ppm-blurred");

    // Blur operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("blur download-png download-png-blurred");

    // Blur operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("blur download-jpg download-jpg-blurred");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "blur applied to: download-ppm-blurred\n"
            + "Loaded image: download-png\n"
            + "blur applied to: download-png-blurred\n"
            + "Loaded image: download-jpg\n"
            + "blur applied to: download-jpg-blurred";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests the 'sharpen' operation across all formats.
   */
  @Test
  public void testSharpenCommand() {
    // Sharpen operation on PPM
    controller.processCommand("load resources/test_image/download.ppm download-ppm");
    controller.processCommand("sharpen download-ppm download-ppm-sharpened");

    // Sharpen operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("sharpen download-png download-png-sharpened");

    // Sharpen operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("sharpen download-jpg download-jpg-sharpened");

    String expectedOutput = "Loaded image: download-ppm\n"
            + "sharpen applied to: download-ppm-sharpened\n"
            + "Loaded image: download-png\n"
            + "sharpen applied to: download-png-sharpened\n"
            + "Loaded image: download-jpg\n"
            + "sharpen applied to: download-jpg-sharpened";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Tests if multiple image operations can be cascaded correctly.
   */
  @Test
  public void testCascadingOperations() {
    // Load the original image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Apply a series of operations in cascade
    controller.processCommand("blur download-png download-png-blurred");
    controller.processCommand("brighten 20 download-png-blurred brightened-download-png-blurred");
    controller.processCommand("sharpen brightened-download-png-blurred "
            + "sharpened-brightened-download-png-blurred");

    // Save the final result after all cascaded operations
    controller.processCommand("save /Users/akshay/Desktop/"
            + "sharpened-brightened-download-png-blurred.png "
            + "sharpened-brightened-download-png-blurred");

    String expectedOutput = "Loaded image: download-png\n"
            + "blur applied to: download-png-blurred\n"
            + "Brightened image by 20: brightened-download-png-blurred\n"
            + "sharpen applied to: sharpened-brightened-download-png-blurred\n"
            + "Saved image: sharpened-brightened-download-png-blurred to "
            + "/Users/akshay/Desktop/sharpened-brightened-download-png-blurred.png";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testDownscaleCommand() {
    // Downscale operation on PNG
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("downscale 250 100 download-png download-png-downscaled");

    // Downscale operation on JPG
    controller.processCommand("load resources/test_image/download.jpg download-jpg");
    controller.processCommand("downscale 200 50 download-jpg download-jpg-downscaled");

    String expectedOutput = "Loaded image: download-png\n"
            + "Downscaled image download-png to 250x100: download-png-downscaled\n"
            + "Loaded image: download-jpg\n"
            + "Downscaled image download-jpg to 200x50: download-jpg-downscaled";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testBlurCommandWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply blur operation with mask
    controller.processCommand("blur download-png mask-image blurred-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "blur applied with mask to: blurred-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testSepiaCommandWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply sepia operation with mask
    controller.processCommand("sepia download-png mask-image sepia-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "sepia applied with mask to: sepia-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testGreyscaleCommandWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply greyscale operation with mask
    controller.processCommand("greyscale luma download-png mask-image greyscale-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Converted download-png to greyscale using luma with mask mask-image: greyscale-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testSharpenCommandWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply sharpen operation with mask
    controller.processCommand("sharpen download-png mask-image sharpened-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "sharpen applied with mask to: sharpened-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testRedComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply red component operation with mask
    controller.processCommand("red-component download-png mask-image red-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized red channel with mask: red-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testBlueComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply blue component operation with mask
    controller.processCommand("blue-component download-png mask-image blue-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized blue channel with mask: blue-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testGreenComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply green component operation with mask
    controller.processCommand("green-component download-png mask-image green-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized green channel with mask: green-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testValueComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply value component operation with mask
    controller.processCommand("value-component download-png mask-image value-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized value component with mask: value-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testLumaComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply luma component operation with mask
    controller.processCommand("luma-component download-png mask-image luma-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized luma component with mask: luma-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  @Test
  public void testIntensityComponentWithMask() {
    // Load source and mask images
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("load resources/test_image/download.png mask-image");

    // Apply intensity component operation with mask
    controller.processCommand("intensity-component download-png mask-image intensity-with-mask");

    String expectedOutput = "Loaded image: download-png\n"
            + "Loaded image: mask-image\n"
            + "Visualized intensity component with mask: intensity-with-mask";

    assertEquals(normalizeOutput(expectedOutput), normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * Restores the original standard output after each test case is run.
   */
  @After
  public void tearDown() {
    System.setOut(originalOut);  // Restore standard output
  }
}
