package view;

import java.util.Scanner;

/**
 * The ImageViewImpl class implements the {@link ImageView} interface, providing
 * a concrete implementation for rendering messages to the user and
 * obtaining user input in the context of image processing applications.
 * <p>
 * This class is responsible for interacting with the user through the console,
 * allowing commands to be entered and messages to be displayed.
 * </p>
 */
public class ImageViewImpl implements ImageView {
  private final Scanner scanner;

  /**
   * Constructs an ImageViewImpl instance, initializing the {@link Scanner} for
   * reading user input from the console.
   * <p>
   * This constructor creates a new Scanner object to read input from {@link System#in}.
   * </p>
   */
  public ImageViewImpl() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Prompts the user to enter a command and returns the input as a string.
   *
   * @return the user's input as a string.
   */
  @Override
  public String getUserInput() {
    System.out.print("Enter command: ");
    return scanner.nextLine();
  }

  /**
   * Renders a message to the user by printing it to the console.
   *
   * @param message the message to be displayed to the user.
   */
  @Override
  public void renderMessage(String message) {
    System.out.println(message);
  }
}
