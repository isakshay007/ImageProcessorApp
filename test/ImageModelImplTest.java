import model.Image;
import model.ImageModelImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ImageModelImpl} class. Specifically tests various image operations
 * such as horizontal flip, blur, brightness adjustment, and more for PNG format.
 */
public class ImageModelImplTest {

  private ImageModelImpl model;

  @Before
  public void setUp() {
    model = new ImageModelImpl();
  }

  /**
   * Compares two images by comparing their pixel values.
   *
   * @param expectedRed   the expected red channel pixel data.
   * @param expectedGreen the expected green channel pixel data.
   * @param expectedBlue  the expected blue channel pixel data.
   * @param actualRed     the actual red channel pixel data.
   * @param actualGreen   the actual green channel pixel data.
   * @param actualBlue    the actual blue channel pixel data.
   */
  private void assertImageEquals(int[][] expectedRed, int[][] expectedGreen, int[][] expectedBlue,
                                 int[][] actualRed, int[][] actualGreen, int[][] actualBlue) {
    for (int i = 0; i < expectedRed.length; i++) {
      assertArrayEquals(expectedRed[i], actualRed[i]);
      assertArrayEquals(expectedGreen[i], actualGreen[i]);
      assertArrayEquals(expectedBlue[i], actualBlue[i]);
    }
  }

  @Test
  public void testHorizontalFlipPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.flip("horizontal", "download-png",
            "download-png-flipped-horizontal");
    model.load("test/test_image/Output_png/download-png-flipped-horizontal.png",
            "download-png-flipped-horizontal");

    Image actualImage = model.getImage("download-png-flipped-horizontal");
    Image expectedImage = model.getImage("download-png-flipped-horizontal");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testBrightenPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.brighten(25, "download-png", "download-png-brightened");
    model.load("test/test_image/Output_png/"
            + "download-png-brightened.png", "download-png-brightened");

    Image actualImage = model.getImage("download-png-brightened");
    Image expectedImage = model.getImage("download-png-brightened");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testDarkenPNG() {
    model.load("resources/test_image/download.png", "download-png");
    // Apply darkening effect with a specified value, e.g., -25 to darken
    model.brighten(-25, "download-png", "download-png-darken");
    model.load("test/test_image/"
            + "Output_png/download-png-darken.png", "download-png-darken");

    Image actualImage = model.getImage("download-png-darken");
    Image expectedImage = model.getImage("download-png-darken");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testBlurPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.blur("download-png", "download-png-blurred");
    model.load("test/test_image/"
            + "Output_png/download-png-blurred.png", "download-png-blurred");

    Image actualImage = model.getImage("download-png-blurred");
    Image expectedImage = model.getImage("download-png-blurred");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSharpenPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.sharpen("download-png", "download-png-sharpened");
    model.load("test/test_image/"
            + "Output_png/download-png-sharpened.png", "download-png-sharpened");

    Image actualImage = model.getImage("download-png-sharpened");
    Image expectedImage = model.getImage("download-png-sharpened");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel()
            , expectedImage.getBlueChannel(), actualImage.getRedChannel()
            , actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSepiaPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.convertToSepia("download-png", "download-png-sepia");
    model.load("test/test_image/"
            + "Output_png/download-png-sepia.png", "download-png-sepia");

    Image actualImage = model.getImage("download-png-sepia");
    Image expectedImage = model.getImage("download-png-sepia");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel()
            , expectedImage.getBlueChannel(), actualImage.getRedChannel()
            , actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testGreyscalePNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.convertToGreyscale("intensity", "download-png",
            "download-png-greyscale");
    model.load("test/test_image/Output_png"
            + "/download-png-greyscale.png", "download-png-greyscale");

    Image actualImage = model.getImage("download-png-greyscale");
    Image expectedImage = model.getImage("download-png-greyscale");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testRGBSplitAndCombinePNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.rgbSplit("download-png", "download-png-red",
            "download-png-green", "download-png-blue");
    model.rgbCombine("download-png-combined", "download-png-red",
            "download-png-green", "download-png-blue");
    model.load("test/test_image/Output_png/"
            + "download-png-combined.png", "download-png-combined");

    Image actualImage = model.getImage("download-png-combined");
    Image expectedImage = model.getImage("download-png-combined");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testVerticalFlipPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.flip("vertical", "download-png",
            "download-png-flipped-vertical");
    model.load("test/test_image/Output_png/"
            + "download-png-flipped-vertical.png", "download-png-flipped-vertical");

    Image actualImage = model.getImage("download-png-flipped-vertical");
    Image expectedImage = model.getImage("download-png-flipped-vertical");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel()
            , expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testValueComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeComponent("value", "download-png",
            "download-png-value-component");
    model.load("test/test_image/Output_png/"
            + "download-png-value-component.png", "download-png-value-component");

    Image actualImage = model.getImage("download-png-value-component");
    Image expectedImage = model.getImage("download-png-value-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testLumaComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeComponent("luma", "download-png",
            "download-png-luma-component");
    model.load("test/test_image/Output_png"
            + "/download-png-luma-component.png", "download-png-luma-component");

    Image actualImage = model.getImage("download-png-luma-component");
    Image expectedImage = model.getImage("download-png-luma-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testIntensityComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeComponent("intensity", "download-png",
            "download-png-intensity-component");
    model.load("test/test_image/Output_png"
            + "/download-png-intensity-component.png",
            "download-png-intensity-component");

    Image actualImage = model.getImage("download-png-intensity-component");
    Image expectedImage = model.getImage("download-png-intensity-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testRedComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeChannel("red", "download-png",
            "download-png-red-component");
    model.load("test/test_image/Output_png/"
            + "download-png-red-component.png", "download-png-red-component");

    Image actualImage = model.getImage("download-png-red-component");
    Image expectedImage = model.getImage("download-png-red-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testGreenComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeChannel("green", "download-png",
            "download-png-green-component");
    model.load("test/test_image/"
            + "Output_png/download-png-green-component.png",
            "download-png-green-component");

    Image actualImage = model.getImage("download-png-green-component");
    Image expectedImage = model.getImage("download-png-green-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testBlueComponentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.visualizeChannel("blue", "download-png",
            "download-png-blue-component");
    model.load("test/test_image/Output_png/"
            + "download-png-blue-component.png", "download-png-blue-component");

    Image actualImage = model.getImage("download-png-blue-component");
    Image expectedImage = model.getImage("download-png-blue-component");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testHistogramPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.histogram("download-png", "download-histogram");
    model.load("test/test_image/Output_png/"
            + "download-histogram.png", "download-histogram");

    Image actualImage = model.getImage("download-histogram");
    Image expectedImage = model.getImage("download-histogram");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testColorCorrectionPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.colorCorrect("download-png", "colorcorrected.png");
    model.load("test/test_image/Output_png/"
            + "colorcorrected.png", "colorcorrected.png");

    Image actualImage = model.getImage("colorcorrected.png");
    Image expectedImage = model.getImage("colorcorrected.png");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testLevelsAdjustmentPNG() {
    model.load("resources/test_image/download.png", "download-png");
    model.levelsAdjust(30, 128, 220, "download-png",
            "levelsadjust.png");
    model.load("test/test_image/"
            + "Output_png/levelsadjust.png", "levelsadjust.png");

    Image actualImage = model.getImage("levelsadjust.png");
    Image expectedImage = model.getImage("levelsadjust.png");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationBlur() {
    // Load the input image
    model.load("resources/test_image/download.png", "download-png");
    // Perform the split operation
    model.splitOperation("blur", "download-png", "dest-blur",
            50, null);
    // Load the expected output image into the model
    model.load("test/test_image/Output_png/dest-blur.png",
            "dest-blur.png");

    // Retrieve actual and expected images
    Image actualImage = model.getImage("dest-blur.png");
    Image expectedImage = model.getImage("dest-blur.png");

    // Assert that the images are equal
    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationSharpen() {
    model.load("resources/test_image/download.png", "download-png");
    model.splitOperation("sharpen", "download-png", "dest-sharpen",
            50, null);
    model.load("test/test_image/Output_png/" +
            "dest-sharpen.png", "dest-sharpen.png");

    Image actualImage = model.getImage("dest-sharpen.png");
    Image expectedImage = model.getImage("dest-sharpen.png");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationSepia() {
    model.load("resources/test_image/download.png", "download-png");
    model.splitOperation("sepia", "download-png", "dest-sepia",
            50, null);
    model.load("test/test_image/Output_png/" +
            "dest-sepia.png", "expected-dest-sepia");

    Image actualImage = model.getImage("dest-sepia");
    Image expectedImage = model.getImage("expected-dest-sepia");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationGreyscale() {
    model.load("resources/test_image/download.png", "download-png");
    model.splitOperation("greyscale", "download-png",
            "dest-greyscale", 50, null);
    model.load("test/test_image/Output_png/" +
            "dest-greyscale.png", "expected-dest-greyscale");

    Image actualImage = model.getImage("dest-greyscale");
    Image expectedImage = model.getImage("expected-dest-greyscale");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationColorCorrect() {
    model.load("resources/test_image/download.png", "download-png");
    model.splitOperation("colorcorrect", "download-png",
            "dest-colorCorrect", 50, null);
    model.load("test/test_image/Output_png/" +
            "dest-colorcorrect.png", "expected-dest-colorCorrect");

    Image actualImage = model.getImage("dest-colorCorrect");
    Image expectedImage = model.getImage("expected-dest-colorCorrect");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testSplitOperationLevelsAdjust() {
    model.load("resources/test_image/download.png", "download-png");
    Map<String, Object> params = new HashMap<>();
    params.put("black", 10);
    params.put("mid", 128);
    params.put("white", 245);

    model.splitOperation("levels", "download-png",
            "dest-levels", 50, params);
    model.load("test/test_image/Output_png" +
            "/dest-levels.png", "dest-levels.png");

    Image actualImage = model.getImage("dest-levels.png");
    Image expectedImage = model.getImage("dest-levels.png");

    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }


  @Test
  public void testCompressionAtMultipleLevels() {
    // Load the base image
    model.load("resources/test_image/download.png", "download-png");

    // Test compression at 10%
    model.compress(10, "download-png", "compressed_image_10");
    model.load("test/test_image/Output_png/compression_image_10.png", "compressed_image_10");
    Image actualImage10 = model.getImage("compressed_image_10");
    Image expectedImage10 = model.getImage("compressed_image_10");
    assertImageEquals(expectedImage10.getRedChannel(), expectedImage10.getGreenChannel(),
            expectedImage10.getBlueChannel(), actualImage10.getRedChannel(),
            actualImage10.getGreenChannel(), actualImage10.getBlueChannel());


    // Test compression at 25%
    model.compress(50, "download-png", "compressed_image_50");
    model.load("test/test_image/Output_png/compression_image_50.png"
            , "compressed_image_50");
    Image actualImage50 = model.getImage("compressed_image_50");
    Image expectedImage50 = model.getImage("compressed_image_50");
    assertImageEquals(expectedImage50.getRedChannel(), expectedImage50.getGreenChannel(),
            expectedImage50.getBlueChannel(), actualImage50.getRedChannel(),
            actualImage50.getGreenChannel(), actualImage50.getBlueChannel());

    // Test compression at 95%
    model.compress(95, "download-png", "compressed_image_95");
    model.load("test/test_image/Output_png/"
            + "compression_image_95.png", "compressed_image_95");
    Image actualImage95 = model.getImage("compressed_image_95");
    Image expectedImage95 = model.getImage("compressed_image_95");
    assertImageEquals(expectedImage95.getRedChannel(), expectedImage95.getGreenChannel(),
            expectedImage95.getBlueChannel(), actualImage95.getRedChannel(),
            actualImage95.getGreenChannel(), actualImage95.getBlueChannel());
  }

  @Test
  public void testDownscaleOperation() {
    // Load the base image
    model.load("resources/test_image/download.png", "download-png");

    // Define the new dimensions for the downscaled image
    int newWidth = 250;  // Example new width
    int newHeight = 100; // Example new height

    // Perform the downscale operation
    model.downscaleImage( newWidth, newHeight,"download-png", "downscaled-png-downscale");

    // Load the expected output image
    model.load("test/test_image/Output_png/download-png-downscale.png", "downscaled-png-downscale");

    // Retrieve the actual and expected images
    Image actualImage = model.getImage("downscaled-png-downscale");
    Image expectedImage = model.getImage("downscaled-png-downscale");

    // Compare the channels of the actual and expected images
    assertImageEquals(expectedImage.getRedChannel(), expectedImage.getGreenChannel(),
            expectedImage.getBlueChannel(), actualImage.getRedChannel(),
            actualImage.getGreenChannel(), actualImage.getBlueChannel());
  }

  @Test
  public void testApplyWithMask_Blur() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Define a 3x3 blur kernel
    float[][] blurKernel = {
            {1 / 9f, 1 / 9f, 1 / 9f},
            {1 / 9f, 1 / 9f, 1 / 9f},
            {1 / 9f, 1 / 9f, 1 / 9f}
    };

    // Apply the blur operation with a mask
    model.executeOperationWithMask("blur", blurKernel, null, "download-png", "mask-image", "masked-blurred");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-blurred.png", "masked-blurred");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-blurred");
    Image expectedImage = model.getImage("masked-blurred");
    assertImageEquals(expectedImage, actualImage);
  }


  @Test
  public void testApplyWithMask_Sharpen() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Define a sharpen kernel
    float[][] sharpenKernel = {
            {-1 / 8f, -1 / 8f, -1 / 8f},
            {-1 / 8f, 2f, -1 / 8f},
            {-1 / 8f, -1 / 8f, -1 / 8f}
    };

    // Apply the sharpen operation with a mask
    model.executeOperationWithMask("sharpen", sharpenKernel, null, "download-png", "mask-image", "masked-sharpened");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-sharpened.png", "masked-sharpened");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-sharpened");
    Image expectedImage = model.getImage("masked-sharpened");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Sepia() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the sepia operation with a mask
    model.executeOperationWithMask("sepia", null, null, "download-png", "mask-image", "sepia-image");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-sepia.png", "sepia-image");

    // Compare the actual and expected images
    Image actualImage = model.getImage("sepia-image");
    Image expectedImage = model.getImage("sepia-image");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Red() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the red component operation with a mask
    model.executeOperationWithMask("component", null, "red", "download-png", "mask-image", "masked-red");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-red.png", "masked-red");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-red");
    Image expectedImage = model.getImage("masked-red");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Green() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the green component operation with a mask
    model.executeOperationWithMask("component", null, "green", "download-png", "mask-image", "masked-green");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-green.png", "masked-green");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-green");
    Image expectedImage = model.getImage("masked-green");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Blue() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the blue component operation with a mask
    model.executeOperationWithMask("component", null, "blue", "download-png", "mask-image", "masked-blue");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-blue.png", "masked-blue");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-blue");
    Image expectedImage = model.getImage("masked-blue");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Luma() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the luma component operation with a mask
    model.executeOperationWithMask("component", null, "luma", "download-png", "mask-image", "luma-image");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-luma-component.png", "masked-luma-component");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-luma-component");
    Image expectedImage = model.getImage("masked-luma-component");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Value() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");
    // Apply the value component operation with a mask
    model.executeOperationWithMask("component", null, "value", "download-png", "mask-image", "value-image");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-value-component.png", "masked-value-component");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-value-component");
    Image expectedImage = model.getImage("masked-value-component");
    assertImageEquals(expectedImage, actualImage);
  }

  @Test
  public void testApplyWithMask_Component_Intensity() {
    // Load source and mask images
    model.load("resources/test_image/download.png", "download-png");
    model.load("resources/test_image//mask-image.png", "mask-image");

    // Apply the intensity component operation with a mask
    model.executeOperationWithMask("component", null, "intensity", "download-png", "mask-image", "masked-intensity-component");

    // Load the expected output image
    model.load("test/test_image/Output_png/masked-intensity-component.png", "masked-intensity-component");

    // Compare the actual and expected images
    Image actualImage = model.getImage("masked-intensity-component");
    Image expectedImage = model.getImage("masked-intensity-component");
    assertImageEquals(expectedImage, actualImage);
  }


  /**
   * Asserts that two {@code Image} objects are equal by comparing their dimensions
   * and individual pixel values across the red, green, and blue channels.
   *
   * @param expected the expected {@code Image} object; must not be {@code null}.
   * @param actual   the actual {@code Image} object to compare against the expected;
   *                 must not be {@code null}.
   * @throws AssertionError if either the {@code expected} or {@code actual} image is {@code null}.
   * @throws AssertionError if the width or height of the {@code expected} and {@code actual}
   *                        images do not match.
   * @throws AssertionError if any pixel value in the red, green, or blue channels of the
   *                        {@code expected} image does not match the corresponding pixel
   *                        value in the {@code actual} image.
   */
  private void assertImageEquals(Image expected, Image actual) {
    assertNotNull("Expected image is null", expected);
    assertNotNull("Actual image is null", actual);
    assertEquals("Image widths do not match", expected.getWidth(), actual.getWidth());
    assertEquals("Image heights do not match", expected.getHeight(), actual.getHeight());

    int[][] expectedRed = expected.getRedChannel();
    int[][] actualRed = actual.getRedChannel();
    int[][] expectedGreen = expected.getGreenChannel();
    int[][] actualGreen = actual.getGreenChannel();
    int[][] expectedBlue = expected.getBlueChannel();
    int[][] actualBlue = actual.getBlueChannel();

    for (int i = 0; i < expected.getHeight(); i++) {
      for (int j = 0; j < expected.getWidth(); j++) {
        assertEquals("Red channel mismatch at pixel (" + i + ", " + j + ")",
                expectedRed[i][j], actualRed[i][j]);
        assertEquals("Green channel mismatch at pixel (" + i + ", " + j + ")",
                expectedGreen[i][j], actualGreen[i][j]);
        assertEquals("Blue channel mismatch at pixel (" + i + ", " + j + ")",
                expectedBlue[i][j], actualBlue[i][j]);
      }
    }
  }

}
