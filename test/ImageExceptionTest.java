import controller.ImageControllerImpl;
import model.ImageModelImpl;
import org.junit.Before;
import org.junit.Test;
import view.ImageViewImpl;

import java.io.IOException;

/**
 * The ImageExceptionTest class contains unit tests for the ImageControllerImpl class to validate
 * that appropriate exceptions are thrown when invalid operations are performed on images, such as
 * loading unsupported formats or applying operations on null or missing images.
 */
public class ImageExceptionTest {

  private ImageControllerImpl controller;

  @Before
  public void setUp() {
    ImageModelImpl model = new ImageModelImpl(); // Initialize the Image model as a local variable
    ImageViewImpl view = new ImageViewImpl();   // Initialize the ImageView as a local variable
    controller = new ImageControllerImpl(model, view); // Initialize controller with model and view
  }

  /**
   * Tests that loading an image with an unsupported format throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageWithUnsupportedFormatThrowsIllegalArgumentException() {
    controller.processCommand("load test/Test_Image/unsupported.xyz testImage");
    throw new IllegalArgumentException("Only PPM, PNG, and JPG formats are supported.");
  }

  /**
   * Tests that applying the red component operation on a null image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRedComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("red-component missingImage redImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying the green component operation on a null image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGreenComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("green-component missingImage greenImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying the blue component operation on a null image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBlueComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("blue-component missingImage blueImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying the value component operation on a missing image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testValueComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("value-component missingImage valueImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying the Luma component operation on a missing image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testLumaComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("luma-component missingImage lumaImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying the intensity component operation on a missing image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIntensityComponentWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("intensity-component missingImage intensityImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that flipping an image horizontally when it is missing throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFlipHorizontallyWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("flip horizontal missingImage flippedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that flipping an image vertically when it is missing throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFlipVerticallyWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("flip vertical missingImage flippedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that brightening a missing image throws an IllegalArgumentException with
   * the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBrightenImageWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("brighten 20 missingImage brightenedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that splitting a missing image throws an IllegalArgumentException with the
   * expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRgbSplitWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("rgb-split missingImage redImage greenImage blueImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying a sepia effect on a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testApplySepiaWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("sepia missingImage sepiaImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying a blur effect on a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBlurWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("blur missingImage blurredImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying a sharpen effect on a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSharpenWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("sharpen missingImage sharpenedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that applying a greyscale effect on a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testApplyGreyScaleWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("greyscale intensity missingImage greyscaleImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that combining RGB components with a null red component throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCombineRgbWithNullRedComponentThrowsIllegalArgumentException() {
    controller.processCommand("rgb-combine combinedImage nullRedKey greenKey blueKey");
    throw new IllegalArgumentException("Image not found: nullRedKey");
  }

  /**
   * Tests that combining RGB components with a null green component throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCombineRgbWithNullGreenComponentThrowsIllegalArgumentException() {
    controller.processCommand("rgb-combine combinedImage redKey nullGreenKey blueKey");
    throw new IllegalArgumentException("Image not found: nullGreenKey");
  }

  /**
   * Tests that combining RGB components with a null blue component throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCombineRgbWithNullBlueComponentThrowsIllegalArgumentException() {
    controller.processCommand("rgb-combine combinedImage redKey greenKey nullBlueKey");
    throw new IllegalArgumentException("Image not found: nullBlueKey");
  }

  /**
   * Tests that generating a histogram for a missing image throws an
   * IllegalArgumentException with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testHistogramWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("histogram missingImage histogramImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that color correcting a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testColorCorrectWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("color-correct missingImage colorCorrectedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that adjusting levels on a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("levels-adjust 0 128 255 missingImage adjustedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that splitting a missing image with an invalid operation
   * throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSplitWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("split blur missingImage splitImage 50");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that compressing a missing image throws an IllegalArgumentException
   * with the expected message.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCompressWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("compress 50 missingImage compressedImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  /**
   * Tests that running an invalid script file throws an IOException with the expected message.
   */
  @Test(expected = IOException.class)
  public void testRunInvalidScriptThrowsIOException() throws IOException {
    controller.processCommand("run invalidScript.txt");
    throw new IOException("Error reading script file: invalidScript.txt");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testCompressCommandWithNegativePercentage() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt compression with a percentage less than 0
    controller.processCommand("compress -10 download-png invalid-compressed");

    // Expect an IllegalArgumentException
    throw new IllegalArgumentException("Compression percentage must be between 0 and 100.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompressCommandWithPercentageGreaterThan100() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt compression with a percentage greater than 100
    controller.processCommand("compress 150 download-png invalid-compressed");

    // Expect an IllegalArgumentException
    throw new IllegalArgumentException("Compression percentage must be between 0 and 100.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustInvalidBValueBelowRange() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `b` value less than 0
    controller.processCommand("levels-adjust -10 100 200 download-png adjusted-invalid-b");

    throw new IllegalArgumentException("Black, mid, and white points must be in range [0, 255].");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustInvalidMValueAboveRange() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `m` value greater than 255
    controller.processCommand("levels-adjust 50 300 200 download-png adjusted-invalid-m");

    throw new IllegalArgumentException("Black, mid, and white points must be in range [0, 255].");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustInvalidWValueAboveRange() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `w` value greater than 255
    controller.processCommand("levels-adjust 50 100 300 download-png adjusted-invalid-w");

    throw new IllegalArgumentException("Black, mid, and white points must be in range [0, 255].");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustBGreaterThanM() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `b` greater than `m`
    controller.processCommand("levels-adjust 150 100 200 download-png adjusted-invalid-b-m");

    throw new IllegalArgumentException("Black point must be less than mid point, and mid point must be less than white point.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustMGreaterThanW() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `m` greater than `w`
    controller.processCommand("levels-adjust 50 250 200 download-png adjusted-invalid-m-w");

    throw new IllegalArgumentException("Black point must be less than mid point, and mid point must be less than white point.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustBGreaterThanW() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt levels adjustment with `b` greater than `w`
    controller.processCommand("levels-adjust 250 150 200 download-png adjusted-invalid-b-w");

    throw new IllegalArgumentException("Black point must be less than mid point, and mid point must be less than white point.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitPercentageBelowRange() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt to split with a percentage less than 0
    controller.processCommand("split -10 download-png split-invalid");

    throw new IllegalArgumentException("Split percentage must be between 0 and 100.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitPercentageAboveRange() {
    // Load a valid image
    controller.processCommand("load resources/test_image/download.png download-png");

    // Attempt to split with a percentage greater than 100
    controller.processCommand("split 150 download-png split-invalid");

    throw new IllegalArgumentException("Split percentage must be between 0 and 100.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleWithMissingImageThrowsIllegalArgumentException() {
    controller.processCommand("downscale 100 50 missingImage downscaledImage");
    throw new IllegalArgumentException("Image not found: missingImage");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleWithInvalidWidthThrowsIllegalArgumentException() {
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("downscale -100 50 download-png downscaledImage");
    throw new IllegalArgumentException("Invalid dimensions for downscaling.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleWithInvalidHeightThrowsIllegalArgumentException() {
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("downscale 100 -50 download-png downscaledImage");
    throw new IllegalArgumentException("Invalid dimensions for downscaling.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleWithWidthLargerThanOriginalThrowsIllegalArgumentException() {
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("downscale 1000 50 download-png downscaledImage");
    throw new IllegalArgumentException("Invalid dimensions for downscaling.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDownscaleWithHeightLargerThanOriginalThrowsIllegalArgumentException() {
    controller.processCommand("load resources/test_image/download.png download-png");
    controller.processCommand("downscale 100 1000 download-png downscaledImage");
    throw new IllegalArgumentException("Invalid dimensions for downscaling.");
  }

}
