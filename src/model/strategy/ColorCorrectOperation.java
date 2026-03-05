package model.strategy;

import model.Image;

import static model.ImageUtils.clamp;

/**
 * Strategy that applies color correction to an image by aligning histogram peaks.
 */
public class ColorCorrectOperation implements ImageOperation {

  @Override
  public Image apply(Image image) {
    int[][] histograms = calculateHistograms(image);
    int[] peaks = findPeaks(histograms);
    int averagePeak = (peaks[0] + peaks[1] + peaks[2]) / 3;

    return adjustImageColors(image, peaks, averagePeak);
  }

  private int[][] calculateHistograms(Image image) {
    int[] redHist = new int[256];
    int[] greenHist = new int[256];
    int[] blueHist = new int[256];

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        redHist[image.getRedChannel()[i][j]]++;
        greenHist[image.getGreenChannel()[i][j]]++;
        blueHist[image.getBlueChannel()[i][j]]++;
      }
    }
    return new int[][]{redHist, greenHist, blueHist};
  }

  private int[] findPeaks(int[][] histograms) {
    return new int[]{findPeak(histograms[0]), findPeak(histograms[1]), findPeak(histograms[2])};
  }

  private int findPeak(int[] histogram) {
    int peak = 0;
    for (int i = 10; i < 245; i++) {
      if (histogram[i] > histogram[peak]) {
        peak = i;
      }
    }
    return peak;
  }

  private Image adjustImageColors(Image image, int[] peaks, int averagePeak) {
    int[][] red = adjustChannel(image.getRedChannel(), peaks[0], averagePeak);
    int[][] green = adjustChannel(image.getGreenChannel(), peaks[1], averagePeak);
    int[][] blue = adjustChannel(image.getBlueChannel(), peaks[2], averagePeak);

    return new Image(image.getWidth(), image.getHeight(), red, green, blue);
  }

  private int[][] adjustChannel(int[][] channel, int peak, int averagePeak) {
    int[][] adjustedChannel = new int[channel.length][channel[0].length];
    for (int i = 0; i < channel.length; i++) {
      for (int j = 0; j < channel[i].length; j++) {
        adjustedChannel[i][j] = clamp(channel[i][j] + (averagePeak - peak));
      }
    }
    return adjustedChannel;
  }
}
