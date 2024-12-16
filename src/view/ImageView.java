package view;

/**
 * The ImageView interface defines the methods for rendering messages
 * to the user and for obtaining user input in the context of image
 * processing applications.
 */
public interface ImageView {

  /**
   * Renders a message to the user, typically to provide feedback or
   * information about the current state or result of an operation.
   *
   * @param message the message to be rendered to the user.
   */
  void renderMessage(String message);

  /**
   * Retrieves user input, which could be used for various operations
   * such as specifying file paths, commands, or parameters.
   *
   * @return the user input as a String.
   */
  String getUserInput();
}
