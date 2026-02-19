package controller;

/**
 * An interface for processing image commands.
 * This interface defines methods to process commands and run script files.
 */
public interface ImageController {

  /**
   * Processes a command string that specifies an operation to be performed on an image.
   *
   * @param command the command string that specifies the image operation to execute.
   */
  void processCommand(String command);

  /**
   * Runs a script file containing a series of commands for image processing.
   *
   * @param scriptFile the path to the script file to be executed.
   * @throws IllegalArgumentException if the script file cannot be found or accessed.
   */
  void runScript(String scriptFile);
}
